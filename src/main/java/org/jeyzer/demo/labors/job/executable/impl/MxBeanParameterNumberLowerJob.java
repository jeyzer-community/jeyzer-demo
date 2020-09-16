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
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;

public class MxBeanParameterNumberLowerJob extends ExecutableJob {
	
	private PlaneMXImpl plane;
	
	public MxBeanParameterNumberLowerJob(ExecutableJobDefinition def) {
		super(def);
	}
	
	@Override
	public void init() {
		super.init();
		this.plane = PlaneMXImpl.instance();
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			flyLower();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			this.plane.setAltitude(500L); // back to normal
		}
	}
	
	@Function
	private void flyLower() throws InterruptedException {
		this.plane.setAltitude(90L); // too low !!
		hold();
	}
}
