package org.jeyzer.demo.virtualthreads;

/*-
 * ---------------------------LICENSE_START---------------------------
 * Jeyzer Demo 21 Virtual Threads
 * --
 * Copyright (C) 2020 - 2023 Jeyzer
 * --
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 * ----------------------------LICENSE_END----------------------------
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Code courtesy provided by José Paumard
 * It has been adapted to integrate the virtual threads demo
 * Inspired initially from https://github.com/rokon12/project-loom-slides-and-demo-code
 * 
 * Images are downloaded from the picsum.photos web site which is accessing the Unsplash image repository.
 * Unsplash images can be used freely. See https://unsplash.com/license
 * 
 * The number of downloaded images is controlled through the JEYZER_DEMO_VIRTUAL_THREADS_IMAGE_DOWNLOADER_FETCH_LIMIT environment variable.
 * Please use a low value such as 1000 to not harm the image server.
 * With high values like 5000, you may experiment connection issues and get stale threads.
 */

public class ImageDownloader2 extends TestSequence{

    record Image(int index, byte[] image) {
        Image {
            image = Arrays.copyOf(image, image.length);
        }
    }

    private static String URL_ROOT = "https://picsum.photos/500/300?random=";
    private static int EXPECTED_IMAGE_SIZE = 50 * 1024;

	private void downloadImages() {
    	int limit = 1000; // Ok value
    	// Make it configurable. 
    	// With 5000, connections may fail and we encounter then unmounted threads that never get released
    	String value = System.getenv("JEYZER_DEMO_VIRTUAL_THREADS_IMAGE_DOWNLOADER_FETCH_LIMIT");
    	if (value != null) {
    		limit = Integer.valueOf(value);
    	}
		
        var imageIndexes = IntStream.range(0, limit).toArray();

        downloadWithPlatformThreads(imageIndexes);
        sleep(10);
        downloadWithVirtualThreads(imageIndexes);
        sleep(10);
        downloadWithStructuredTaskScope(imageIndexes);
	}

    //Total time taken = 4378 ms 1000
    // Total time taken = 3959 ms with 100 images
    // José
    //     10  images ->  480ms - 520ms  | 48ms / image
    //    100  images -> 1550ms - 1600ms | 16ms / image
    //   1000  images -> 2700ms - 3800ms |  3ms / image
    public static void downloadWithStructuredTaskScope(int[] imageIndexes) {
        System.out.println("starting downloading " + imageIndexes.length + " with virtual StructuredTaskScope");

        try (var scope = new StructuredTaskScope<Image>();) {
            var start = Instant.now();
            var results = Arrays.stream(imageIndexes)
                    .mapToObj(ImageDownloader2::saveImageTask)
                    .map(scope::fork)
                    .toList();
            var total = Duration.between(start, Instant.now()).toMillis();
            System.out.println("Time taken to submit the tasks = " + total + " ms");
            scope.join();

            var images =
                    results.stream()
                            .map(StructuredTaskScope.Subtask::get)
                            .toList();

            total = Duration.between(start, Instant.now()).toMillis();
            
            printResults(images, total);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    //Total time taken = 6037 ms for 100 images 10 threads
    //Total time taken = 21554 ms  10 threads
    //Total time taken = 27062 ms 100 Threads
    // José
    //     10  images                ->  550ms          | 55ms   / image
    //    100  images |   10 threads -> 1000ms - 1100ms | 11ms   / image
    //    100  images |  100 threads -> 1000ms - 1100ms | 11ms   / image
    //   1000  images |   10 threads -> 5500ms - 5700ms |  5,7ms / image
    //   1000  images |  100 threads -> 2100ms - 2500ms |  2,5ms / image
    //   1000  images | 1000 threads -> 5300ms - 5800ms |  5,3ms / image
   public static void downloadWithPlatformThreads(int[] imageIndexes) {
        System.out.println("starting downloading " + imageIndexes.length + " with FixedThreadPool");

        try (var executorService = Executors.newFixedThreadPool(imageIndexes.length)) {
            Instant start;
            start = Instant.now();

            var images = readImages(imageIndexes, executorService);

            long total = Duration.between(start, Instant.now()).toMillis();
            
            printResults(images, total);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    // José
    //     10  images ->  550ms          | 55ms   / image
    //    100  images ->  800ms -  900ms |  8ms   / image
    //   1000  images -> 3500ms - 4500ms |  3,5ms / image
    public static void downloadWithVirtualThreads(int[] imageIndexes) {
        System.out.println("starting downloading " + imageIndexes.length + " with VirtualThreadPerTaskExecutor");

        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            Instant start;
            start = Instant.now();

            var images = readImages(imageIndexes, executorService);

            long total = Duration.between(start, Instant.now()).toMillis();

            printResults(images, total);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static void printResults(List<Image> images, long total) {
        int countSuccess = 0;
        int countError = 0;            
        for (int i=0; i< images.size(); i++) {
        	if (images.get(i) != null)
        		countSuccess++;
        	else 
        		countError++;
        }
        
        System.out.println("# images in success = " + countSuccess);
        System.out.println("# images in error   = " + countError);            
        System.out.println("Total time taken = " + total + " ms");
	}

	private static List<Image> readImages(int[] imageIndexes, ExecutorService threadPool) throws InterruptedException, ExecutionException {
        var futures = Arrays.stream(imageIndexes)
                .mapToObj(ImageDownloader2::saveImageTask)
                .map(threadPool::submit)
                .toList();

        for (var future : futures) {
            future.get();
        }

        var images =
                futures.stream().map(Future::resultNow).toList();
        return images;
    }

    private static Callable<Image> saveImageTask(int imageIndex) {
        return () -> {
            try (InputStream in = new URL(URL_ROOT + imageIndex).openStream()) {
                var out = new ByteArrayOutputStream(EXPECTED_IMAGE_SIZE);
                long size = in.transferTo(out);
                var image = out.toByteArray();
                return new Image(imageIndex, image);
            } catch (IOException e) {
            	System.out.println("Connection error");
            	return null;
            }
        };
    }

	@Override
	public void introduction() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Starting Image Downloader Sequence");
        System.out.println("-----------------------------------------------------");
		this.sleep(6L);
	}

	@Override
	public void executeSequence() {
		downloadImages();
	}
}
