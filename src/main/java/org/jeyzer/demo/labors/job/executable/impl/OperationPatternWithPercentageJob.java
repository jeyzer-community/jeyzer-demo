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


import java.util.Random;

import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.annotations.Operation;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;

public class OperationPatternWithPercentageJob extends ExecutableJob {

	private Random random = new Random();
	
	public OperationPatternWithPercentageJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {		
		while(true) {
			try {
				processData();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
			
			if (Thread.interrupted())
				return;
		}
	}
	
	@Function
	private void processData() throws InterruptedException {
		callRandomOperation();
	}


	@Operation(name="Unexpected exception generation")
	private void throwException() throws Exception {
		Thread.sleep(10); // let's cheat a bit
		throw new Exception("Deep applicative exception with massive memory cost.");
	}
	
	private void doOther() throws InterruptedException {
		Thread.sleep(10);
	}
	
	private void callRandomOperation() throws InterruptedException {
		int value = random.nextInt(20);
		
		if (value <12) {
			int level = 0;
			try {
				deepWork(level);
			} catch (InterruptedException e) {
				throw e;
			} catch (Exception e) {
				// ignore the applicative one
			}
			
			if (Thread.interrupted())
				throw new InterruptedException();
		}
		else {
			doOther();
		}
	}

	@Operation
	private void deepWork(int level) throws Exception {
		level++;
		if (level == 500)
			throwException();
		else
			deepWork(level);
	}
}
