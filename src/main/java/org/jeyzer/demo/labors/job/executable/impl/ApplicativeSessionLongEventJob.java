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
import org.jeyzer.publish.event.JzrEvent;
import org.jeyzer.publish.event.JzrStandardEvent;

public class ApplicativeSessionLongEventJob extends ExecutableJob {

	public ApplicativeSessionLongEventJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		JzrEvent event = raiseApplicativeSessionLongEvent();
		closeApplicativeSessionLongEvent(event);
	}

	@Function
	private JzrEvent raiseApplicativeSessionLongEvent() {
		JzrStandardEvent event = new JzrStandardEvent(
				LaborEventCode.JZR_LAB_06,
				"Remote service GH is not accessible. Please check its availability.");
		this.jmonitor.startGlobalEvent(event);
		
		waitSomeTime();
		
		return event;
	}

	private void waitSomeTime() {
		try {
			this.hold();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	private void closeApplicativeSessionLongEvent(JzrEvent event) {
		this.jmonitor.terminateEvent(event);
	}
}
