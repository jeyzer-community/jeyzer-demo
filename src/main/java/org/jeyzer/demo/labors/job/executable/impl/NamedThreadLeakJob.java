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


import java.util.Timer;
import java.util.TimerTask;

import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;

public class NamedThreadLeakJob extends ExecutableJob {

	private List<Timer> timers = new ArrayList<>();
	
	public NamedThreadLeakJob(ExecutableJobDefinition def) {
		super(def);
	}

	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			createTimers();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}finally{
			for (Timer timer : timers) {
				timer.cancel();
				timer.purge();	
			}
			timers.clear();
		}
	}
	
	@Function
	private void createTimers() throws InterruptedException {
		while (true) {
			// add 3 timers on every iteration
			for (int i=0; i<3; i++) {
				Timer timer = new Timer();
				timer.schedule(new NamedThreadLeakTimer(),50L, 5000L);
				timers.add(timer);
			}
			
			oneTicHold(); // will interrupt
			
			if (Thread.interrupted())
				return;
		}
	}
	
	public final class NamedThreadLeakTimer extends TimerTask{

		@Override
		@Executor(name="Timer action executor")
		public void run() {
			timerAction();
		}

		@Function
		private void timerAction() {
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
