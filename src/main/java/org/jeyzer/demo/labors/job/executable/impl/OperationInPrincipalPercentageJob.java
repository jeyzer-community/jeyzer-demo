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


import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.annotations.Operation;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;
import org.jeyzer.demo.shared.DemoHelper;

public class OperationInPrincipalPercentageJob extends ExecutableJob {

	public OperationInPrincipalPercentageJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			while (true)
				processBigData(); // will be interrupted
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	@Function
	private void processBigData() throws InterruptedException {
		callRandomOperation();
	}

	@Operation
	private void readData() throws InterruptedException {
		oneTicHold();
	}
	
	@Operation
	private void permuteData() throws InterruptedException {
		oneTicHold();
	}
	
	private void callRandomOperation() throws InterruptedException{
		switch(DemoHelper.getNextRandomInt(10)) {
			case 0: case 1: case 2: case 3: 
			case 4: case 5: case 6: case 7: 
				readData();
				break;
			default:
				permuteData();
		}
	}
}
