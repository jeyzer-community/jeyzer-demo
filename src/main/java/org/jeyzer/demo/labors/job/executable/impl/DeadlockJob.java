package org.jeyzer.demo.labors.job.executable.impl;

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


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.annotations.Locker;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;
import org.jeyzer.demo.labors.job.executable.JobThreadFactory;

public class DeadlockJob extends ExecutableJob {

	private Lock shared1 = new ReentrantLock(); 
	private Lock shared2 = new ReentrantLock();
	
	private AtomicInteger subCount = new AtomicInteger(1);
	private List<Thread> dlThreads = new ArrayList<>();
	private ThreadFactory factory = new JobThreadFactory(this.def.getThreadName()+ "-sub", subCount);
	
	public DeadlockJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			createDeadlock();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			for (Thread t : dlThreads)
				t.interrupt();
		}
	}
	
	@Function
	private void createDeadlock() throws InterruptedException {
		for (int i=0; i<2; i++) {
			Thread t = factory.newThread(new SubDeadlock());
			dlThreads.add(t);
			t.start();
		}
		
		hold();
	}
	
	public final class SubDeadlock implements Runnable{

		@Override
		@Executor(name="Deadlock participant")
		public void run() {
			accessSharedResources();
		}

		@Function(name="Access shared resource")
		@Locker(name="Shared resource locking")
		public void accessSharedResources(){
			Lock a;
			Lock b;
			
			if (Thread.currentThread().getName().contains("#1")){
				a = shared1;
				b = shared2;
			}else{
				a = shared2;
				b = shared1;
			}
			
			try {
				a.lockInterruptibly();
				synchronized(a) {
					a.wait(2000);
				}
				
				b.lockInterruptibly(); // deadlock happens here. Will be interrupted at job's end.
				// do some action..
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
