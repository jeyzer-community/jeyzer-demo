package org.jeyzer.demo.labors.job;

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


import java.util.concurrent.atomic.AtomicInteger;

import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.annotations.Operation;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;
import org.jeyzer.demo.shared.DemoHelper;

public class DummyJob extends ExecutableJob {

	public static final AtomicInteger dummyThreadCount = new AtomicInteger(1);
	
	public DummyJob(ExecutableJobDefinition def) {
		super(def);
	}
	
	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			callRandomFunction();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}		
	}

	@Override
	public String getEventName() {
		return "Dummy job";
	}
	
	@Override
	protected AtomicInteger getThreadCount() {
		return dummyThreadCount;
	}
	
	private void callRandomFunction() throws InterruptedException{
		switch(DemoHelper.getNextRandomInt(3)) {
			case 0:   
				perform();
				break;
			case 1: 
				focus();
				break;
			default:
				analyze();
		}
	}
	
	private void callRandomOperation(String action) throws InterruptedException{
		switch(DemoHelper.getNextRandomInt(10)) {
			case 0: case 1: case 2: case 3: 
			case 4: case 5:  
				commit(action);
				break;
			case 6: case 7: 
				compute(action);
				break;
			case 8: 
				read(action);
				break;
			default:
				scan(action);
		}
	}
	
	@Operation
	private void commit(String action) throws InterruptedException {
		hold();
	}

	@Operation
	private void compute(String action) throws InterruptedException {
		hold();
	}

	@Operation
	private void read(String action) throws InterruptedException {
		hold();
	}

	@Operation
	private void scan(String action) throws InterruptedException {
		hold();
	}

	@Function
	private void perform() throws InterruptedException {
		callRandomOperation("execute");
	}

	@Function
	private void analyze() throws InterruptedException {
		callRandomOperation("analyze");
	}

	@Function
	private void focus() throws InterruptedException {
		callRandomOperation("focus");
	}
}
