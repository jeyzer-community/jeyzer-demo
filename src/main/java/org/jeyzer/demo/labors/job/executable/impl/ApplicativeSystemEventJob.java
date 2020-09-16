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
import org.jeyzer.demo.event.codes.LaborEventCode;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;
import org.jeyzer.publish.event.JzrStandardEvent;

public class ApplicativeSystemEventJob extends ExecutableJob {

	public ApplicativeSystemEventJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			raiseApplicativeSystemEvent();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	@Function
	private void raiseApplicativeSystemEvent() throws InterruptedException {
		this.jmonitor.fireSystemEvent(new JzrStandardEvent(
				LaborEventCode.JZR_LAB_07,
				"Process service KF cannot be started. Error is : NotSupportedOSException. Please contact support.")
				);
		this.hold();
	}
}
