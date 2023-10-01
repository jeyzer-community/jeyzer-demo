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
public class ThousandsVirtualThreadsCPU extends TestSequence {
    static LongAdder longAdder = new LongAdder();

    private void virtualThreadsByThousandsSequence() throws InterruptedException {
    	
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (; ; ) {
                executor.submit(() -> {
                    longAdder.increment();
                    return work();
                });
                
                if (longAdder.longValue() >= 1000) {
                	System.out.println("All virtual CPU threads created");
                	this.sleep(10);
                    return;
                }
                
                try {
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
        }
    }

    
	private void consumeCPU() {
		int value1;
		int value2;
		int count = 0;
		
		while (true){ // will be interrupted externally
			value1 = (int) (Math.random() * 100) + 1;
			value2 = (int) (Math.random() * 200) + 1;
			double result  = (double)value1/value2;
			count++;
			if (count == 100000) {
				break;
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
        consumeCPU();
        return null;
    }
    
	@Override
	public void introduction() {
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Starting 1 executor running 1K CPU working virtual threads");
        System.out.println("-------------------------------------------------------------------------");

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
