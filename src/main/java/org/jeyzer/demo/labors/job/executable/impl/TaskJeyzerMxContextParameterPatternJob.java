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
import org.jeyzer.publish.JzrActionContext;
import org.jeyzer.publish.JzrActionHandler;

public class TaskJeyzerMxContextParameterPatternJob extends ExecutableJob {
	
	public static final String ACTIVITY_STATE_KEY = "activity-state";

	public TaskJeyzerMxContextParameterPatternJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			publishActivityState();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	@Function
	private void publishActivityState() throws InterruptedException {
		JzrActionContext ctx = new JzrActionContext();
		ctx.setUser("user");
		ctx.setFunctionPrincipal("Publish activity state");
		ctx.setId("222");
		ctx.setContextParam(ACTIVITY_STATE_KEY, "suspended");
		
		JzrActionHandler handler = JeyzerPublisher.instance().getActionHandler();		
		handler.startAction(ctx);
		
		this.hold();
	}
}
