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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * Source adapted and derived from :
 *  https://github.com/rokon12/project-loom-slides-and-demo-code
 */
public class ReentrantLockDeadlockSequence extends TestSequence{
	
	private static boolean first = true;
	
    private void virtualReentrantLockdeadlockSequence() throws InterruptedException {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();

        List<Thread> threads = IntStream.range(0, 2)
                .mapToObj(index ->
                        Thread.ofVirtual().unstarted(() -> {
                        	boolean needLock2;
                        	
                            System.out.println(Thread.currentThread() + " - Started");
                            
                            if (first) {
                            	first = false;
                            	needLock2 = true;
                            	lock1.lock();
                            }
                            else {
                            	needLock2 = false;
                            	lock2.lock();
                            }

                            try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
                            
                            System.out.println(Thread.currentThread() + " - Accessing 2nd lock");
                            
                            if (needLock2) {
                            	try {
									lock2.tryLock(10, TimeUnit.SECONDS);
	                                lock1.unlock();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
                            }
                            else {
									lock1.lock();
									lock2.unlock();
									lock1.unlock();
                            }
                            
//                            try {
//                                sleep();
//                            } finally {
//                                lock1.unlock();
//                            }

                            System.out.println(Thread.currentThread()+ " - Done");
                        })).toList();

        threads.forEach(Thread::start);

        try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        for (Thread thread : threads) {
            thread.join();
        }
    }

	@Override
	public void introduction() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Starting Reentrant Lock Deadlock Sequence");
        System.out.println("-----------------------------------------------------");
		this.sleep(6L);
	}

	@Override
	public void executeSequence() {
		try {
			virtualReentrantLockdeadlockSequence();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
