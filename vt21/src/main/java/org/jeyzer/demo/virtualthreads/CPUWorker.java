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

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class CPUWorker implements Runnable {

    static LongAdder longAdder = new LongAdder();
	private CountDownLatch startSignal;
	private String executorName;
	
	public CPUWorker(CountDownLatch latch, String name) {
		this.startSignal = latch;
		this.executorName = name;
	}
	
	@Override
	public void run() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
	    	try {
				startSignal.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	for (; ; ) {
	            executor.submit(() -> {
	                longAdder.increment();
	                return work();
	            });
	            
	            if (longAdder.longValue() >= 1000) {
	            	System.out.println("All virtual CPU threads created by executor " + executorName);
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

    private Object work() {
        if (longAdder.longValue() % 1000 == 0) {
            System.out.println("Current Thread count: "+ longAdder.longValue() + "  threads in executor " +  executorName);
        }
        this.sleep(10);
        if ("1".equals(executorName))
        	consumeCPU1();
        else
        	consumeCPU2();
        return null;
    }
	
    protected void sleep(long time) {
        try {
            Thread.sleep(Duration.ofSeconds(time));
        } catch (InterruptedException e) {
        	System.err.print("Interrupted");
        }
    }
    
	private void consumeCPU1() {
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

	private void consumeCPU2() {
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
	
}
