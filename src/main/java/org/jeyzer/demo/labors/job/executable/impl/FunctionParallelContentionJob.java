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


import java.io.IOException;

import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;

public class FunctionParallelContentionJob extends ExecutableJob {

	public FunctionParallelContentionJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			sendMesssage();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	@Function
	private void sendMesssage() throws InterruptedException {
		try {
			connectToClient();
		} catch (IOException e) {
			handleIoException();
		}
	}

	@Function
	private void handleIoException() throws InterruptedException {
		hold(); // simulate cost
	}

	private void connectToClient() throws IOException {
		throw new IOException();
	}
}
