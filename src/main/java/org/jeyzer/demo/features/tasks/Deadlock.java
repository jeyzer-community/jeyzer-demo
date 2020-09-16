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

import org.jeyzer.annotations.Function;
import org.jeyzer.annotations.Locker;
import org.jeyzer.annotations.Operation;
import org.jeyzer.demo.event.codes.FeatureEventCode;
import org.jeyzer.demo.features.Feature;
import org.jeyzer.publish.event.JzrStandardEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Deadlock extends Feature {
	
	private static final Logger logger = LoggerFactory.getLogger(Deadlock.class);

	private static final String FEATURE_NAME = "Deadlock";
	
	private static int count = 0;

	private SharedResource shared1 = new SharedResource(); 
	private SharedResource shared2 = new SharedResource();

	public class DeadlockFactory implements ThreadFactory {
		  @Override
		  public Thread newThread(Runnable r) {
			 Thread t = new Thread(r);
			 t.setName("Deadlock demo thread " + increaseCount());
		     return t;
		   }
		 }
	
	public static class SharedResource{
		public void hello(){
			logger.info("Resource locked : " + this + "\n");
		}
	}
	
	@Override
	public void show() throws InterruptedException {
		publishThreadApplicativeEvent(
				new JzrStandardEvent(FeatureEventCode.JZR_FEA_02));
		lockThreads();
	}
	
	@Override
	public String getName() {
		return FEATURE_NAME;
	}

	private void lockThreads() {
		log("Deadlock", "starts");
		
		ExecutorService service = Executors.newFixedThreadPool(2, new DeadlockFactory());
		for (int i=0; i<2; i++){
			
			service.submit(new Runnable(){
				@Override
				// @Executor(name="Deadlock feature thread")
				// Anonymous annotation is not supported at compile time
				// Once it will be, add the ElementType.TYPE_USE to the Executor annotation definitions
				// For now, add it manually in the profile
				public void run() {
					accessSharedResources();
				}
			});
		}
		
		waitBeforeShutdown();
		
		// Java says : Attempts to stop all actively executing tasks, halts the processing of waiting tasks, and returns a list of the tasks that were awaiting execution.
		// Doesn't stop the threads here and returns immediately.
		service.shutdownNow();
		
		log("DeadLock", "ends");
	}

	@Function(name="Waiting before ExecutorService forceShutdown")
	private void waitBeforeShutdown() {
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Function(name="Thread in deadlock")
	@Operation(name="Access to already locked resource")
	@Locker(name="Shared resource locking")
	public void accessSharedResources(){
		
		SharedResource a;
		SharedResource b;
		
		if ("Deadlock demo thread 1".equals(Thread.currentThread().getName())){
			a = shared1;
			b = shared2;
		}else{
			a = shared2;
			b = shared1;
		}
		
		synchronized(a){
		
			a.hello();
			
			try {
				a.wait(2000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			log("Deadlock", "trying to lock resource : " + b);
			
			synchronized(b){
				b.hello();
			}
		}
	}	
	
	private static synchronized int increaseCount(){
		return count++;
	}
}
