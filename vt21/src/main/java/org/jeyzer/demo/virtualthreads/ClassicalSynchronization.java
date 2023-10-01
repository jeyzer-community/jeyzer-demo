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

import java.util.List;
import java.util.stream.IntStream;

/**
 * Source adapted and derived from :
 *  https://github.com/rokon12/project-loom-slides-and-demo-code
 */
public class ClassicalSynchronization extends TestSequence {
	
    private void virtualClassicalSynchronizationSequence() throws InterruptedException {
        Object lock = new Object();
        
        List<Thread> threads = IntStream.range(0, 16)
                .mapToObj(index ->
                        Thread.ofVirtual().unstarted(() -> {
                            if (index == 0) System.out.println(Thread.currentThread());
                            synchronized (lock){
                                sleep(5);
                            }
                            if (index == 0) System.out.println(Thread.currentThread());
                        })).toList();

        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }
    }

	@Override
	public void introduction() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Starting Classical Synchronization Sequence");
        System.out.println("-----------------------------------------------------");        
		this.sleep(6L);
	}

	@Override
	public void executeSequence() {
		try {
			virtualClassicalSynchronizationSequence();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
