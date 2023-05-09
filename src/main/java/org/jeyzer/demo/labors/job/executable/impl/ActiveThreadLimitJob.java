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

public class ActiveThreadLimitJob extends ExecutableJob {

	private AtomicInteger subCount = new AtomicInteger(1);
	private List<Thread> gtlThreads = new ArrayList<>();
	private ThreadFactory factory = new JobThreadFactory(this.def.getThreadName()+ "-sub", subCount);
	
	public ActiveThreadLimitJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			createActiveThreads();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			for (Thread t : gtlThreads)
				t.interrupt();
		}
	}
	
	@Function
	private void createActiveThreads() throws InterruptedException {
		for (int i=0; i<505; i++) {
			Thread t = factory.newThread(new SubActiveThreadLimit());
			gtlThreads.add(t);
			t.start();
		}
			
		hold(); // will interrupt			
	}
	
	public final class SubActiveThreadLimit implements Runnable{

		@Override
		@Executor(name="Active executor")
		public void run() {
			activeThread();
		}

		@Function
		private void activeThread() {
			action1();
		}
		
		public void action1() {
			action12();
		}
		
		public void action12() {
			action123();
		}
		
		public void action123() {
			action1234();
		}
		
		public void action1234() {
			action12345();
		}
		
		public void action12345() {
			try {
				Thread.sleep(5000000000L); // sleep forever
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
