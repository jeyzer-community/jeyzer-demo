package org.jeyzer.demo.virtualthreads;

import java.io.File;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

/**
 * Not called in the demo - just for reference
 * 
 * Source adapted and derived from :
 *  https://github.com/rokon12/project-loom-slides-and-demo-code
 *  
 * Images are downloaded from the picsum.photos web site which is accessing the Unsplash image repository.
 * Unsplash images can be used freely. See https://unsplash.com/license
 * 
 * The number of downloaded images is controlled through the JEYZER_DEMO_VIRTUAL_THREADS_IMAGE_DOWNLOADER_FETCH_LIMIT environment variable.
 * Please use a low value such as 1000 to not harm the image server.
 * With high values like 5000, you may experiment connection issues and get stale threads.
 */
public class ImageDownloader extends TestSequence{
    private static String IMAGE_DESTINATION_FOLDER = "../../work/images";

    private void downloadImages() throws IOException {
    	File dir = new File(IMAGE_DESTINATION_FOLDER);
    	dir.mkdirs();
    	
    	int limit = 1000; // Ok value
    	// Make it configurable. 
    	// With 5000, connections may fail and we encounter then unmounted threads that never get released
    	String value = System.getenv("JEYZER_DEMO_VIRTUAL_THREADS_IMAGE_DOWNLOADER_FETCH_LIMIT");
    	if (value != null) {
    		limit = Integer.valueOf(value);
    	}
    	
        var ulrs = IntStream.range(0, limit)
                .mapToObj(index -> "https://picsum.photos/500/300?random=" + index)
                .toList();

        downloadWithThreadPool(ulrs);

        try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}

        downloadWithVirtualThread(ulrs);

    }

    //Total time taken = 4378 ms 1000
    // Total time taken = 3959 ms with 100 images
    private void downloadWithVirtualThread(List<String> urls) {
        System.out.println("starting downloading " + urls.size() + " with VirtualThread");

        cleanDestination();
        Instant start;

        try (ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor()) {
            start = Instant.now();
            var futures = new ArrayList<Future<?>>();
            for (String url : urls) {
                Future<?> future = threadPool.submit(() -> saveImage(url));
                futures.add(future);
            }

            for (var future : futures) {
                future.get();
            }

            long total = Duration.between(start, Instant.now()).toMillis();
            System.out.println("Total time taken by virtual threads = " + total + " ms");

			File dir = new File(IMAGE_DESTINATION_FOLDER);
			System.out.println("Number of images dowloaded in the directory = " + dir.list().length);
            
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    //Total time taken = 6037 ms for 100 images 10 threads
    //Total time taken = 21554 ms  10 threads
    //Total time taken = 27062 ms 100 Threads
    private void downloadWithThreadPool(List<String> urls) {
    	int poolSize = 1000;
    	
        System.out.println("starting downloading " + urls.size() + " with FixedThreadPool of size " + poolSize);
        cleanDestination();
        Instant start;

        try (ExecutorService threadPool = Executors.newFixedThreadPool(poolSize)) {
            start = Instant.now();
            var futures = new ArrayList<Future<?>>();
            for (String url : urls) {
                Future<?> future = threadPool.submit(() -> saveImage(url));
                futures.add(future);
            }

            for (var future : futures) {
                future.get();
            }

            long total = Duration.between(start, Instant.now()).toMillis();
            System.out.println("Total time taken by native threads = " + total + " ms");
			
			File dir = new File(IMAGE_DESTINATION_FOLDER);
			System.out.println("Number of images dowloaded in the directory = " + dir.list().length);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void cleanDestination() {
        if (Files.exists(Path.of(IMAGE_DESTINATION_FOLDER))) {
            try {
                Files.walk(Path.of(IMAGE_DESTINATION_FOLDER))
                        .filter(p -> !p.endsWith(IMAGE_DESTINATION_FOLDER))
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (Throwable e) {
                                throw new RuntimeException(e);
                            }
                        });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveImage(String url) {
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, Paths.get(IMAGE_DESTINATION_FOLDER + "/" + UUID.randomUUID() + ".jpg"));
	    } catch (java.net.SocketException e) {
	    	StringBuilder fullStack = new StringBuilder();
	    	for (StackTraceElement ste : e.getStackTrace()) {
	    		fullStack.append(ste + "\n");
	    	}
	    	System.out.println("Download socket error for thread " + Thread.currentThread().getId() + " on URL : " + url + " Exception is :\n" + fullStack);
        } catch (IOException e) {
	    	StringBuilder fullStack = new StringBuilder();
	    	for (StackTraceElement ste : e.getStackTrace()) {
	    		fullStack.append(ste + "\n");
	    	}
	    	System.out.println("Download IO error for thread " + Thread.currentThread().getId() + " on URL : " + url + " Exception is :\n" + fullStack);
//            throw new RuntimeException(e);
        }
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
		try {
			downloadImages();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
