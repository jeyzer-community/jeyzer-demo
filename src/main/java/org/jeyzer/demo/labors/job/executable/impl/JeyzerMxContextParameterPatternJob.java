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
import org.jeyzer.publish.JeyzerPublisher;

public class JeyzerMxContextParameterPatternJob extends ExecutableJob {
	
	public static final String GATEWAY_STATE_KEY = "gateway-state";

	public JeyzerMxContextParameterPatternJob(ExecutableJobDefinition def) {
		super(def);
	}
	
	@Override
	public void init() {
		super.init();
		JeyzerPublisher.instance().setDynamicProcessContextParam(GATEWAY_STATE_KEY, "Open");
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			closeGatewayState();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	@Function
	private void closeGatewayState() throws InterruptedException {
		JeyzerPublisher.instance().setDynamicProcessContextParam(GATEWAY_STATE_KEY, "Closed");
		this.hold();
	}
}
