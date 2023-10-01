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

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * Source adapted and derived from :
 *  https://github.com/rokon12/project-loom-slides-and-demo-code
 */
public class ThousandsVirtualThreads extends TestSequence {
    static LongAdder longAdder = new LongAdder();

    private void virtualThreadsByThousandsSequence() throws InterruptedException {
    	
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (; ; ) {
                executor.submit(() -> {
                    longAdder.increment();
                    return work();
                });
                
                if (longAdder.longValue() > 0 && longAdder.longValue() >= 10000) {
                	System.out.println("All virtual threads created");
                    break;
                }
                
                try {
					TimeUnit.MILLISECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
        }
    }  
    
    private Object work() {
        if (longAdder.longValue() % 1000 == 0) {
            System.out.printf(
                    Locale.US, "Current Thread count: %,d\tthreads%n",
                    longAdder.longValue()
            );
        }
        this.sleep(10);
        return null;
    }
    
	@Override
	public void introduction() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Starting 10K sleeping virtual threads ");
        System.out.println("-----------------------------------------------------");

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
