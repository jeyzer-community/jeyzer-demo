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

public class FunctionInPrincipalPercentageJob extends ExecutableJob {

	public FunctionInPrincipalPercentageJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			increaseSpeed(); // will be interrupted
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	@Function
	private void increaseSpeed() throws InterruptedException {
		initSecondaryEngine();
		runSecondaryEngine();
	}

	@Function
	private void initSecondaryEngine() throws InterruptedException {
		int max = (int)(this.getJobDefinition().getStartLifeTics() * 0.3 + DemoHelper.getNextRandomInt(3));
		
		for (int i=0; i<max; i++)
			oneTicHold();
	}
	
	@Function
	private void runSecondaryEngine() throws InterruptedException {
		hold(); // hold until interruption
	}
}
