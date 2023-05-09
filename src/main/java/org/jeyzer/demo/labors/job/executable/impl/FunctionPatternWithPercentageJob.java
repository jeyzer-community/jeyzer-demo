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

public class FunctionPatternWithPercentageJob extends ExecutableJob {

	public FunctionPatternWithPercentageJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			while(true) {
				activityXyz();		
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}	
	}
	
	@Function
	private void activityXyz() throws InterruptedException {
		int step = 0;
		
		while(true) {
			if (step <= 6)
				preprocess();
			else if (step <= 8)
				process();
			else
				postprocess();
			step++;
			if (Thread.interrupted())
				throw new InterruptedException();
		}
	}
	
	@Function
	private void preprocess() throws InterruptedException {
		oneTicHold();
	}
	
	@Function
	private void process() throws InterruptedException {
		oneTicHold();
	}
	
	@Function
	private void postprocess() throws InterruptedException {
		oneTicHold();
	}
}
