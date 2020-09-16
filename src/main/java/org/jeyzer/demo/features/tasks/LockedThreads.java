package org.jeyzer.demo.features.tasks;

/*-
 * ---------------------------LICENSE_START---------------------------
 * Jeyzer Demo
 * --
 * Copyright (C) 2020 Jeyzer SAS
 * --
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 * ----------------------------LICENSE_END----------------------------
 */



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.jeyzer.annotations.Exclude;
import org.jeyzer.annotations.Function;
import org.jeyzer.annotations.Operation;
import org.jeyzer.demo.event.codes.FeatureEventCode;
import org.jeyzer.demo.features.Feature;
import org.jeyzer.publish.event.JzrStandardEvent;

public class LockedThreads extends Feature {
	 
	private static final String FEATURE_NAME = "Lock";
	
	 private static int count = 0;
	
	 public class LockedThreadFactory implements ThreadFactory {
		  @Override
		  public Thread newThread(Runnable r) {
			 Thread t = new Thread(r);
			 t.setName("Lock demo thread " + increaseCount());
		     return t;
		   }
		 }
	
	@Override
	public void show() throws InterruptedException {
		publishThreadApplicativeEvent(
				new JzrStandardEvent(FeatureEventCode.JZR_FEA_07));
		lockThreads();
	}
	
	@Override
	public String getName() {
		return FEATURE_NAME;
	}

	@Exclude(name="Lock setup thread")
	private void lockThreads() throws InterruptedException {
		log("LockThreads", "starts");
		
		ExecutorService service = Executors.newFixedThreadPool(5, new LockedThreadFactory());
		for (int i=0; i<5; i++){
			service.submit(new Runnable(){
				@Override
				// @Executor(name="Lock feature thread") 
				// Anonymous annotation is not supported at compile time
				// Once it will be, add the ElementType.TYPE_USE to the Executor annotation definitions
				// For now, add it manually in the profile
				public void run() {
					try {
						executedActionLevel1();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			});
			hold();
		}
		
		service.shutdownNow();
		
		log("LockThreads", "ends");
	}
	
	public void executedActionLevel1() throws InterruptedException {
		executedActionLevel2();
	}

	@Function(name="Thread waiting for the synchronized lock to be released")
	@Operation(name="Method action level2 is synchronized")
	public synchronized void executedActionLevel2() throws InterruptedException {
		executedActionLevel3();
	}
	
	public void executedActionLevel3() throws InterruptedException {
		executedLockedAction();
	}
	
	@Function(name="Thread owning the synchronized lock section", priority=900)
	public synchronized void executedLockedAction() throws InterruptedException{
		executeAction();
	}

	private void executeAction() throws InterruptedException {
		Thread.sleep(30000);
	}
	
	private static synchronized int increaseCount(){
		return count++;
	}
}
