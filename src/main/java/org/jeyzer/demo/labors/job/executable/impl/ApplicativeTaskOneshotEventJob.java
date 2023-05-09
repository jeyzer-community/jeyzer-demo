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
import org.jeyzer.demo.event.codes.LaborEventCode;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;
import org.jeyzer.publish.event.JzrStandardEvent;

public class ApplicativeTaskOneshotEventJob extends ExecutableJob {

	public ApplicativeTaskOneshotEventJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			raiseApplicativeTaskOneshotEvent();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	@Function
	private void raiseApplicativeTaskOneshotEvent() throws InterruptedException {
		this.jmonitor.fireLocalThreadEvent(new JzrStandardEvent(
				LaborEventCode.JZR_LAB_03,
				"Thread local incident : unauthorized access for admin user. Please check the authentication server state.")
				);
		this.hold();
	}
}
