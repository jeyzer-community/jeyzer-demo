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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * Source adapted and derived from :
 *  https://github.com/rokon12/project-loom-slides-and-demo-code
 */
public class ReentrantLockSequence extends TestSequence {
	
    private void virtualReentrantLockSequence() throws InterruptedException {
        Lock lock = new ReentrantLock();
        
        List<Thread> threads = IntStream.range(0, 5)
                .mapToObj(index ->
                        Thread.ofVirtual().unstarted(() -> {
                            System.out.println("Started " + Thread.currentThread());
                            lock.lock();
                            try {
                                sleep(5L);
                            } finally {
                                lock.unlock();
                            }

                            System.out.println("Done" + Thread.currentThread());
                        })).toList();

        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }
    }
    
	@Override
	public void introduction() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Starting Reentrant Lock Sequence");
        System.out.println("-----------------------------------------------------");
		this.sleep(6L);
	}

	@Override
	public void executeSequence() {
		try {
			virtualReentrantLockSequence();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
