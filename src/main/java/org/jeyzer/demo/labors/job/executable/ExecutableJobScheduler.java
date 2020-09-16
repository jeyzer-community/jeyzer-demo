package org.jeyzer.demo.labors.job.executable;

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


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import org.jeyzer.demo.event.codes.LaborEventCode;
import org.jeyzer.demo.labors.job.DummyJob;
import org.jeyzer.demo.shared.DemoHelper;
import org.jeyzer.publish.JzrMonitorHandler;
import org.jeyzer.publish.event.JzrStandardEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExecutableJobScheduler extends TimerTask {
	
	private static final Logger logger = LoggerFactory.getLogger(ExecutableJobScheduler.class);

	private final Queue<List<ExecutableJob>> scheduledJobs = new LinkedList<>();
	private final List<ExecutableJob> liveJobs = new ArrayList<>();
	private final JzrMonitorHandler jmonitor;
	private final boolean testMode;
	
	private boolean previousEmpty;
	
	public ExecutableJobScheduler(List<ExecutableJob> executableJobs, JzrMonitorHandler jmonitor, boolean testMode) {
		this.testMode = testMode;
		this.jmonitor = jmonitor;
		prepareJobs(executableJobs);
	}
	
	public void start() {
		Timer timer = new Timer("Job scheduler");
		long startDelay = this.testMode ? 1000 : 10000;
		timer.scheduleAtFixedRate(this, startDelay, 5000); // 10 sec start delay, every 5 sec
		
		logger.info("Above jobs are now scheduled for execution :         ");
		logger.info(" - System jobs get fired all in one go at start.");
		logger.info(" - Executable jobs get executed once at a time, amongst dummy random jobs.");
		logger.info("   Scheduler will wake up every 5 seconds to execute those.");
		logger.info("");
		logger.info("");
		logger.info("Scheduler is now active :");
		logger.info("");
	}
	
	@Override
	public void run() {
		updateLiveJobs();
		List<ExecutableJob> newJobs = scheduledJobs.poll();
		if (newJobs != null && !newJobs.isEmpty()) {
			printJobSection();
			liveJobs.addAll(newJobs);
			for (ExecutableJob job : newJobs)
				startJob(job);	
		}
		else {
			if (liveJobs.isEmpty() && scheduledJobs.isEmpty())
				System.exit(0); // time to stop
			printEmptySection();
		}
	}

	private void printEmptySection() {
		if (!previousEmpty) {
			logger.info("============================================");
			previousEmpty = true;
		}
		logger.info(".");
	}

	private void printJobSection() {
		if (previousEmpty) 
			previousEmpty = false;
		logger.info("============================================");
	}

	private void startJob(ExecutableJob job) {
		logger.info(job.getJobDefinition().getStickerName() + " job");
		
		JzrStandardEvent event = null;
		if (!(job instanceof DummyJob) && !job.isSystemEventBased()) {
			event = new JzrStandardEvent(
					LaborEventCode.JZR_LAB_02,
					"Executable job : " + job.getEventName());
			this.jmonitor.startGlobalEvent(event);
		}
		
		job.setJzrMonitorHandler(this.jmonitor);
		job.setLaborEvent(event); // event to close possibly
		job.work(); // start it
	}

	private void updateLiveJobs() {
		List<ExecutableJob> jobsToRemove = new ArrayList<>();
		
		for(ExecutableJob job : liveJobs) {
			if (!job.lifeTic())
				jobsToRemove.add(job);
		}
		
		liveJobs.removeAll(jobsToRemove);
	}

	private void prepareJobs(List<ExecutableJob> executableJobs) {
		int jobAdjust = 0;
		for (ExecutableJob job : executableJobs) {
			
			// intermediary dummy jobs
			addDummyJobTics(jobAdjust);
			
			// executable job
			addRealJobTic(job);

			jobAdjust = job.getLifeTics();
		}
		
		// one last time
		addDummyJobTics(jobAdjust);
	}

	private void addRealJobTic(ExecutableJob job) {
		List<ExecutableJob> jobs = new ArrayList<>();
		jobs.add(job);
		addDummyJobs(jobs);
	}

	private void addDummyJobTics(int jobAdjust) {
		if (testMode)
			return; // do not create dummy jobs
		
		for (int i=0; i< 3 + jobAdjust; i++)
			addDummyJobs(new ArrayList<ExecutableJob>());
	}

	private void addDummyJobs(List<ExecutableJob> jobs) {
		int dummyCount = getRandomDummyCount();
		for (int i=0; i<dummyCount; i++) {
			DummyJob job = new DummyJob(ExecutableJobDefinition.DUMMY);
			job.setLifeTics(getRandomLifeTics());
			jobs.add(job);
		}
		
		this.scheduledJobs.offer(jobs);
	}

	private int getRandomDummyCount() {
		switch(DemoHelper.getNextRandomInt(8)) {
		case 0:   
			return 3;
		case 1: case 2: 
			return 2;
		case 3: case 4: 
			return 1;
		default:
			return 0;
		}
	}
	
	private int getRandomLifeTics() {
		switch(DemoHelper.getNextRandomInt(10)) {
		case 0:
			return 4;
		case 1: case 2:
			return 3;
		case 3: case 4: case 5:
			return 2;
		default:
			return 1;
		}
	}
}
