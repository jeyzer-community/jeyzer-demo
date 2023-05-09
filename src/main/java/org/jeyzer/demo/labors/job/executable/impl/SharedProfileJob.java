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
import org.jeyzer.demo.shared.DemoSharedListener;
import org.jeyzer.demo.shared.DemoSharedPublisher;

public class SharedProfileJob extends ExecutableJob implements DemoSharedListener {

	public SharedProfileJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		DemoSharedPublisher publisher = new DemoSharedPublisher(this);
		publisher.publish();
	}

	@Override
	public void callback() {
		try {
			hold();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
