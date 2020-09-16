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

public class ActiveUniqueThreadJob extends ExecutableJob {

	private AtomicInteger subCount = new AtomicInteger(1);
	private Thread failoverThread;
	private ThreadFactory factory = new JobThreadFactory("AUT - fail_over_thread", subCount);
	
	public ActiveUniqueThreadJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			createFailoverThread();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			failoverThread.interrupt();
		}
	}
	
	@Function
	private void createFailoverThread() throws InterruptedException {
		failoverThread = factory.newThread(new SubActiveUniqueThreadJob());
		failoverThread.start();
			
		hold(); // will interrupt			
	}
	
	public final class SubActiveUniqueThreadJob implements Runnable{

		@Override
		@Executor(name="Failover executor")
		public void run() {
			interfaceFailover();
		}

		private void interfaceFailover() {
			failOver();
		}
		
		@Function
		public void failOver() {
			try {
				Thread.sleep(5000000000L); // sleep forever
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
