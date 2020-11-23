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

public class UniqueThreadJob extends ExecutableJob {

	private AtomicInteger subCount = new AtomicInteger(1);
	private Thread undesiredThread;
	private ThreadFactory factory = new JobThreadFactory("UT - undesired_thread", subCount);
	
	public UniqueThreadJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			createUndesiredThread();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			undesiredThread.interrupt();
		}
	}
	
	@Function
	private void createUndesiredThread() throws InterruptedException {
		undesiredThread = factory.newThread(new SubUniqueThreadJob());
		undesiredThread.start();
			
		hold(); // will interrupt			
	}
	
	public final class SubUniqueThreadJob implements Runnable{

		@Override
		@Executor(name="Undesired thread")
		public void run() {
			try {
				undesiredThreadExecution();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		@Function
		private void undesiredThreadExecution() throws InterruptedException {
			// inactive
			Thread.sleep(50000000000000000L);
		}
	}
}
