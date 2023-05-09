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
import org.jeyzer.publish.event.JzrEvent;
import org.jeyzer.publish.event.JzrStandardEvent;

public class ApplicativeTaskLongEventJob extends ExecutableJob {

	public ApplicativeTaskLongEventJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		JzrEvent event = raiseApplicativeTaskLongEvent();
		closeApplicativeTaskLongEvent(event);
	}

	@Function
	private JzrEvent raiseApplicativeTaskLongEvent() {
		JzrStandardEvent event = new JzrStandardEvent(
				LaborEventCode.JZR_LAB_04,
				"Thread local resource KL is not accessible. Please check its availability.");
		this.jmonitor.startLocalThreadEvent(event);
		
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
	
	private void closeApplicativeTaskLongEvent(JzrEvent event) {
		this.jmonitor.terminateEvent(event);
	}
}
