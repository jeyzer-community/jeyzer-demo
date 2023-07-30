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
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;

public class ExecutorPresenceJob extends ExecutableJob {

	public ExecutorPresenceJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Important executor")
	public void executeJob() {
		executeImportantThing();
	}

	public void executeImportantThing() {
		try {
			hold();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
