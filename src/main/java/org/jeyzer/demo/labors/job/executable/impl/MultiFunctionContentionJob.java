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

public class MultiFunctionContentionJob extends ExecutableJob {

	private AtomicInteger subCount = new AtomicInteger(1);
	private List<Thread> failoverThreads = new ArrayList<>();
	private ThreadFactory factory = new JobThreadFactory(this.def.getThreadName()+ "-sub", subCount);
	
	public MultiFunctionContentionJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			createFailoverThreads();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			for (Thread t : failoverThreads)
				t.interrupt();
		}
	}
	
	@Function
	private void createFailoverThreads() throws InterruptedException {
		for (int i=0; i<25; i++) {
			Thread t = factory.newThread(new SubMultiFunctionContention(i));
			failoverThreads.add(t);
			t.start();
		}
		
		hold();
	}
	
	public final class SubMultiFunctionContention implements Runnable{
		
		private int index;
		
		public SubMultiFunctionContention(int index) {
			this.index = index;
		}

		@Override
		@Executor(name="Failover executor")
		public void run() {
			try {
				enterFailoverSituation();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		private void enterFailoverSituation() throws InterruptedException {
			if (index <10)
				addConnection();
			else if (index < 15)
				stopConnectionInJmx();
			else
				ioExceptionHandling();
		}

		@Function
		private void ioExceptionHandling() throws InterruptedException {
			hold();
		}

		@Function
		private void stopConnectionInJmx() throws InterruptedException {
			hold();
		}

		@Function
		private void addConnection() throws InterruptedException {
			hold();
		}
	}
}
