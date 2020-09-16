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

import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;
import org.jeyzer.demo.labors.job.executable.JobThreadFactory;

public class GlobalThreadLimitJob extends ExecutableJob {

	private AtomicInteger subCount = new AtomicInteger(1);
	private List<Thread> gtlThreads = new ArrayList<>();
	private ThreadFactory factory = new JobThreadFactory(this.def.getThreadName()+ "-sub", subCount);
	
	public GlobalThreadLimitJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			createThreads();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			for (Thread t : gtlThreads)
				t.interrupt();
		}
	}
	
	@Function
	private void createThreads() throws InterruptedException {
		for (int i=0; i<605; i++) {
			Thread t = factory.newThread(new SubGlobalThreadLimit());
			gtlThreads.add(t);
			t.start();
		}
			
		hold(); // will interrupt			
	}
	
	public final class SubGlobalThreadLimit implements Runnable{

		@Override
		@Executor(name="Sleeping executor")
		public void run() {
			try {
				Thread.sleep(5000000000L); // sleep forever
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	};
}
