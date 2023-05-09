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
import org.jeyzer.mx.event.JzrEventLevel;
import org.jeyzer.publish.JeyzerPublisher;

public class JeyzerPublisherEventJob extends ExecutableJob {

	public JeyzerPublisherEventJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		disableApplicativeEventPublishing();
		enableApplicativeEventPublishing();
	}

	@Function
	private void disableApplicativeEventPublishing() {
		JeyzerPublisher.instance().suspendEventCollection(JzrEventLevel.INFO);
		waitSomeTime();
	}
	
	private void waitSomeTime() {
		try {
			this.hold();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	private void enableApplicativeEventPublishing() {
		JeyzerPublisher.instance().resumeEventCollection(JzrEventLevel.INFO);
	}
}
