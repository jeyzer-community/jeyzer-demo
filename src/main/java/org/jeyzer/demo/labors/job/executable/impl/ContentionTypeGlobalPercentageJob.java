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
import org.jeyzer.annotations.Operation;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;

public class ContentionTypeGlobalPercentageJob extends ExecutableJob {

	public ContentionTypeGlobalPercentageJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			receivePayload();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Function
	private void receivePayload() throws InterruptedException {
		waitForMessagePayloadReception();
	}
	
	@Operation(contentionType="Waiting") // must be one listed in default_setup.xml if non CPU consumer
	private void waitForMessagePayloadReception() throws InterruptedException {
		hold();
	}
}
