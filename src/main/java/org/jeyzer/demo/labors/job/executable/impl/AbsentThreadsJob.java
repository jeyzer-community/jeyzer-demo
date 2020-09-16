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


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;
import org.jeyzer.demo.labors.job.executable.JobThreadFactory;

public class AbsentThreadsJob extends ExecutableJob {

	private AtomicInteger subCount = new AtomicInteger(1);
	private ThreadFactory factory = new JobThreadFactory("AT - vital_thread", subCount);
	private Thread vitalThread;
	
	public AbsentThreadsJob(ExecutableJobDefinition def) {
		super(def);
	}
	
	@Override
	public void init() {
		super.init();
		createVitalThread();
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		pauseVitalThread();
		
		// recreate the vital thread
		createVitalThread();
	}
	
	@Function
	private void pauseVitalThread() {
		this.vitalThread.interrupt();
		
		for (int i=0; i < this.def.getStartLifeTics()-1; i++) {
			try {
				oneTicHold();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@Function
	private void createVitalThread() {
		vitalThread = factory.newThread(new SubAbsentThreads());
		vitalThread.start();
	}
	
	public final class SubAbsentThreads implements Runnable{

		@Override
		@Executor(name="Vital thread")
		public void run() {
			doCriticalWork();
		}

		@Function
		public void doCriticalWork(){
			try {
				synchronized(this) {
					this.wait();
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
