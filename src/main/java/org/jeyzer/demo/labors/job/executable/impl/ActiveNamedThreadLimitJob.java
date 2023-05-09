package org.jeyzer.demo.labors.job.executable.impl;

/*-
 * ---------------------------LICENSE_START---------------------------
 * Jeyzer Demo
 * --
 * Copyright (C) 2020 - 2023 Jeyzer
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

import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;
import org.jeyzer.demo.labors.job.executable.JobThreadFactory;

public class ActiveNamedThreadLimitJob extends ExecutableJob {

	private AtomicInteger subCount = new AtomicInteger(1);
	private List<Thread> antlThreads = new ArrayList<>();
	private List<SubActiveNamedThreadLimitWorker> workers = new ArrayList<>();
	private ThreadFactory factory = new JobThreadFactory("ANTL - worker_thread", subCount);
	
	public ActiveNamedThreadLimitJob(ExecutableJobDefinition def) {
		super(def);
	}
	
	@Override
	public void init() {
		super.init();
		createWorkerThreads();
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		activateWorkerThreads();
	}
	
	@Function
	private void activateWorkerThreads() {
		// let's activate all the workers 
		for (SubActiveNamedThreadLimitWorker worker : workers)
			synchronized (worker) {
				worker.notifyAll();
			}
		
		try {
			hold();
		} catch (InterruptedException e) {
			// nothing to do. Let's exit and keep the workers back to idle
			Thread.currentThread().interrupt();
		}
	}

	private void createWorkerThreads() {
		for (int i=0; i<5; i++) {
			SubActiveNamedThreadLimitWorker worker = new SubActiveNamedThreadLimitWorker(this.def.getStartLifeTics());
			workers.add(worker);
			
			Thread t = factory.newThread(worker);
			antlThreads.add(t);
			t.start();
		}
	}
	
	public final class SubActiveNamedThreadLimitWorker implements Runnable{

		private int startLifeTics;
		
		public SubActiveNamedThreadLimitWorker(int startLifeTics) {
			this.startLifeTics = startLifeTics;
		}

		@Override
		@Executor(name="Pool thread")
		public void run() {
			try {
				doNothing();
				
				// let's be active
				subwork();

				// back to idle
				doNothingAgain();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		private void doNothingAgain() throws InterruptedException {
			synchronized(this) {
				this.wait();
			}
		}

		@Function
		private void doNothing() throws InterruptedException {
			synchronized(this) {
				this.wait();
			}
		}

		private void subwork() throws InterruptedException {
			processWork();
		}

		@Function
		private void processWork() throws InterruptedException {
			Thread.sleep(HOLD_TIC_DURATION * this.startLifeTics);
		}
	}
}
