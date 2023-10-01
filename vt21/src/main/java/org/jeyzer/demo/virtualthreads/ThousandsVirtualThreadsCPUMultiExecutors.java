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

import java.util.concurrent.CountDownLatch;

/**
 * Source adapted and derived from :
 *  https://github.com/rokon12/project-loom-slides-and-demo-code
 */
public class ThousandsVirtualThreadsCPUMultiExecutors extends TestSequence {

    private void virtualThreadsByThousandsSequence() throws InterruptedException {
    	
    	// Let the 2 executors start at the same time
    	CountDownLatch startSignal = new CountDownLatch(1);
    	
    	Thread t1 = new Thread(new CPUWorker(startSignal, "1"), "Worker CPU native");
    	Thread t2 = new Thread(new CPUWorker(startSignal, "2"), "Worker CPU native");
        
//    	System.out.println("Start 2 threads");
    	t1.start();
    	t2.start();
    	
    	this.sleep(5);
//    	System.out.println("Signal now released");
        startSignal.countDown();
        
        t1.join();
        t2.join();
    	System.out.println("Join done");
    }
    
    
	@Override
	public void introduction() {
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Starting 2 executors running each 1K CPU working virtual threads");
        System.out.println("--------------------------------------------------------------------------");

		this.sleep(6L);
	}

	@Override
	public void executeSequence() {
		try {
			virtualThreadsByThousandsSequence();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
