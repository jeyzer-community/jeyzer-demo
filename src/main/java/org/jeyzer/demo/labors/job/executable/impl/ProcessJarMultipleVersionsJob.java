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
import org.jeyzer.demo.dup1.DummyDemo1;
import org.jeyzer.demo.dup2.DummyDemo2;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;

public class ProcessJarMultipleVersionsJob extends ExecutableJob {

	public ProcessJarMultipleVersionsJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		loadDuplicates();
	}
	
	private void loadDuplicates() {
		DummyDemo1 demo1 = new DummyDemo1();
		demo1.load();
		DummyDemo2 demo2 = new DummyDemo2();
		demo2.load();
	}
}
