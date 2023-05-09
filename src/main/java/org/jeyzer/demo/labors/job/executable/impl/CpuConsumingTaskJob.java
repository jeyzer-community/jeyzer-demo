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


import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;
import org.jeyzer.demo.shared.DemoHelper;

public class CpuConsumingTaskJob extends ExecutableJob {
	
	private double result;

	public CpuConsumingTaskJob(ExecutableJobDefinition def) {
		super(def);
	}
	
	public double getResult() {
		return result;
	}
	
	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		consumeCPU();
	}

	@Function(name="CPU consumer")
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
