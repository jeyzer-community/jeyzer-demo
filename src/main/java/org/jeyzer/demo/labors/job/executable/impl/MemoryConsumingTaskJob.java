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


import java.util.ArrayList;
import java.util.List;

import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;

public class MemoryConsumingTaskJob extends ExecutableJob {
	
	private List<Long> elements = new ArrayList<>();

	public MemoryConsumingTaskJob(ExecutableJobDefinition def) {
		super(def);
	}
	
	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			consumeMemory();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Function(name="Memory consumer")
	private void consumeMemory() throws InterruptedException {
		for (long i=1; i<3000000; i++){ //around 300 Mb
			this.elements.add(i);
		}
		hold();
		this.elements.clear();
		System.gc();
	}
}
