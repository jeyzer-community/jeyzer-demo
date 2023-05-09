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



import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;
import org.jeyzer.demo.labors.job.executable.JobThreadFactory;
import org.jeyzer.demo.shared.DemoHelper;

public class CpuConsumingProcessJob extends ExecutableJob {

	private AtomicInteger subCount = new AtomicInteger(1);
	private List<Thread> cpuThreads = new ArrayList<>();
	private ThreadFactory factory = new JobThreadFactory(this.def.getThreadName()+ "-sub", subCount);
	
	public CpuConsumingProcessJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			createCpuConsumingThreads();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			for (Thread t : cpuThreads)
				t.interrupt();
		}
	}
	
	@Function
	private void createCpuConsumingThreads() throws InterruptedException {
		OperatingSystemMXBean osmxBean = ManagementFactory.getOperatingSystemMXBean();
		// Try to take 50% of system CPU
		int cpuMax = (int) (osmxBean.getAvailableProcessors() * 0.75);

		for (int i=0; i<cpuMax; i++) {
			Thread t = factory.newThread(new SubCpuConsumingProcessJob());
			cpuThreads.add(t);
			t.start();
		}
		
		hold();
	}
	
	public final class SubCpuConsumingProcessJob implements Runnable{

		private double result;
		
		@Override
		@Executor(name="CPU consumer")
		public void run() {
			consumeCPU();
		}
		
		public double getResult() {
			return result;
		}

		@Function
		private void consumeCPU() {
			int value1;
			int value2;
			int count = 0;
			
			while (true){ // will be interrupted externally
				value1 = DemoHelper.getNextRandomInt();
				value2 = DemoHelper.getNextRandomInt();
				result  = (double)value1/value2;
				count++;
				if (count == 10000) {
					if (Thread.interrupted())
						break;
					count = 0;
				}
			}
		}
	}
}
