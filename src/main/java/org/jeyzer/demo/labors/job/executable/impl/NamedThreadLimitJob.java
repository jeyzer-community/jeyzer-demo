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

public class NamedThreadLimitJob extends ExecutableJob {

	private AtomicInteger subCount = new AtomicInteger(1);
	private List<Thread> antlThreads = new ArrayList<>();
	private ThreadFactory factory = new JobThreadFactory("NTL - client_connection_thread", subCount);
	
	public NamedThreadLimitJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			createInactiveThreads();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		finally {
			for (Thread t : antlThreads)
				t.interrupt();
		}
	}
	
	@Function
	private void createInactiveThreads() throws InterruptedException {
		for (int i=0; i<101; i++) {
			Thread t = factory.newThread(new SubNamedThreadLimitJob());
			antlThreads.add(t);
			t.start();
		}
		
		hold();
	}
	
	public final class SubNamedThreadLimitJob implements Runnable{

		@Override
		@Executor(name="Inactive client")
		public void run() {
			handleClientInactiveConnection();
		}

		@Function
		private void handleClientInactiveConnection() {
			try {
				Thread.sleep(50000000000L);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
