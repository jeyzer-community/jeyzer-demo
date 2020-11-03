package org.jeyzer.demo.labors;

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


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;
import org.jeyzer.demo.labors.job.system.SystemJob;
import org.jeyzer.demo.labors.job.system.SystemJobDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JobGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger(JobGenerator.class);

	private static final int ALL_SYSTEM_JOBS = SystemJobDefinition.values().length;
	private static final int ALL_EXECUTABLE_JOBS = ExecutableJobDefinition.values().length - 1; // exclude dummy
	private static final int ALL_JOBS = ALL_SYSTEM_JOBS + ALL_EXECUTABLE_JOBS;
	
	private static final int SYSTEM_TEST_JOBS = 2;
	private static final int EXECUTABLE_TEST_JOBS = 2;
	private static final int TEST_JOBS = SYSTEM_TEST_JOBS + EXECUTABLE_TEST_JOBS;
	
	private final boolean testMode;
	private final int jobCount;
	private final List<SystemJob> systemJobs = new ArrayList<>();
	private final List<ExecutableJob> executableJobs = new ArrayList<>();
	
	public JobGenerator(int jobCount, boolean testMode, List<Integer> forceSpecificJobs) {
		this.testMode = testMode;
		this.jobCount = (jobCount == Integer.MAX_VALUE || jobCount > ALL_JOBS) ? ALL_JOBS : (jobCount == Integer.MIN_VALUE)? TEST_JOBS : jobCount;
		while (!generateJobs(forceSpecificJobs)); // must have at least 1 executable job
		
		declareJobs();
		initJobs();
	}

	private void declareJobs() {
		List<String> refs = new ArrayList<>();
		
		for(SystemJob systemJob : this.systemJobs) {
			refs.add(systemJob.getEventName());
		}
		
		for(ExecutableJob executableJob : this.executableJobs) {
			refs.add(executableJob.getEventName());
		}
		
		System.setProperty("demo-labors.jobs", refs.toString());
	}

	private void initJobs() {
		for (SystemJob sysJob : systemJobs)
			sysJob.init();
		for (ExecutableJob execJob : executableJobs)
			execJob.init();
	}

	public List<SystemJob> getSystemJobs() {
		return systemJobs;
	}

	public List<ExecutableJob> getExecutableJobs() {
		return executableJobs;
	}
	
	private boolean generateJobs(List<Integer> forceSpecificJobs) {
		boolean executablePresent = false;
		
		systemJobs.clear();
		executableJobs.clear();
		
		// The last 2 created executable and system jobs OR the random/all jobs OR the given jobs
		List<Integer> idList = forceSpecificJobs.isEmpty() ? (testMode ? buildTestIdList() : buildIdList()) : forceSpecificJobs;
        for (int i=0; i<jobCount; i++) {
        	int id = idList.get(i);
        	if (id>=1000)
        		generateSystemJob(id-1000);
        	else {
        		generateExecutableJob(id);
        		executablePresent = true;
        	}
        }
        
        dumpJobs();
        
        return executablePresent;
	}
	
	private List<Integer> buildIdList() {
		List<Integer> ids = new ArrayList<>(ALL_JOBS);
		
		for (int i=1; i<ALL_EXECUTABLE_JOBS+1; i++) // do not include dummy which is in 1st position
            ids.add(Integer.valueOf(i)); 
        
        for (int i=0; i<ALL_SYSTEM_JOBS; i++)
        	ids.add(Integer.valueOf(1000 + i));
        
        if (this.jobCount != ALL_JOBS) // do not shuffle if we cover all cases
        	Collections.shuffle(ids);

		return ids;
	}

	private List<Integer> buildTestIdList() {
		List<Integer> ids = new ArrayList<>(TEST_JOBS);
		
		for (int i=ALL_EXECUTABLE_JOBS; i >= ALL_EXECUTABLE_JOBS - EXECUTABLE_TEST_JOBS + 1; i--) // start from last
            ids.add(Integer.valueOf(i)); 
        
		for (int i=ALL_SYSTEM_JOBS-1; i>= ALL_SYSTEM_JOBS - SYSTEM_TEST_JOBS; i--) // start from last
        	ids.add(Integer.valueOf(1000 + i));
		
		return ids;
	}

	private void dumpJobs() {
		logger.info("");
		logger.info("Available jobs : " + ALL_JOBS);
		logger.info("");
		logger.info("System jobs loaded :");
		if (systemJobs.isEmpty())
			logger.info(" (None)");
		else
			for (SystemJob sysJob : systemJobs)
				logger.info(" - " + sysJob.getEventName());
		logger.info("Executable jobs loaded :");
		for (ExecutableJob execJob : executableJobs)
			logger.info(" - " + execJob.getEventName());
		logger.info("");
	}

	private void generateSystemJob(int id) {
   		SystemJobDefinition def = SystemJobDefinition.values()[id];
		try {
			SystemJob sysJob = new SystemJob(def);
			systemJobs.add(sysJob);
		} catch (Exception e) {
			logger.error("Failed to instanciate system job " + def.getClassName(), e);
		}
	}
	
	private void generateExecutableJob(int id) {
		ExecutableJobDefinition def = ExecutableJobDefinition.values()[id];
		try {
			Constructor<?> constructor = Class.forName(def.getClassName()).getConstructor(ExecutableJobDefinition.class);
			ExecutableJob sysJob = (ExecutableJob)constructor.newInstance(def);
			executableJobs.add(sysJob);
		} catch (Exception e) {
			logger.error("Failed to instanciate executable job " + def.getClassName(), e);
		}
	}
}
