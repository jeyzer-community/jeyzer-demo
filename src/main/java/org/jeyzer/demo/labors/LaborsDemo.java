package org.jeyzer.demo.labors;

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


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.jeyzer.annotations.Exclude;
import org.jeyzer.demo.event.codes.LaborEventCode;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobAdjuster;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;
import org.jeyzer.demo.labors.job.executable.ExecutableJobScheduler;
import org.jeyzer.demo.labors.job.executable.impl.PlaneMXImpl;
import org.jeyzer.demo.labors.job.system.SystemJob;
import org.jeyzer.demo.labors.job.system.SystemJobDefinition;
import org.jeyzer.publish.JeyzerPublisher;
import org.jeyzer.publish.JeyzerPublisherInit;
import org.jeyzer.publish.JzrMonitorHandler;
import org.jeyzer.publish.event.JzrStandardEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LaborsDemo {
	
	private static final Logger logger = LoggerFactory.getLogger(LaborsDemo.class);
	
	private static final String JEYZER_PROFILE = "demo-labors";
	private static final String NODE_NAME      = "Demo labors";
	
	private static final String MANIFEST_IMPL_TITLE   = "Implementation-Title";
	private static final String MANIFEST_IMPL_VERSION = "Implementation-Version";
	private static final String MANIFEST_IMPL_BUILD   = "Implementation-Build";
	
	// applicative event source name
	public static final String SOURCE_NAME = NODE_NAME;
	
	public static final int EVENTS_INFO_LIMIT = 1200;
	public static final int EVENTS_WARN_LIMIT = 1100;

	public static final String LIST_HELP = "help";
	public static final String LIST_PARAM = "list";
	
	public static final String JOBS_PARAM = "jobs=";
	
	public static final String SCOPE_PARAM = "scope=";
	public static final String SCOPE_VALUE_ALL = "all";
	public static final String SCOPE_VALUE_TEST = "test"; // take the last 3
	
	private JzrMonitorHandler jmonitor;
	
	@Exclude
	public static void main(String[] args) {
		LaborsDemo demo = new LaborsDemo();

		 if (!checkParams(args))
			 return;

		if (parseListParam(args)) {
			demo.list();
			return;
		}
		
		List<Integer> jobs = parseJobsParam(args);
		if (!jobs.isEmpty()) {
			// let's execute the specified jobs
			demo.demo(jobs.size(), jobs);
			return;
		}

		// execute random, or all, or last created jobs
		int jobCount = parseScopeParam(args);
		demo.demo(jobCount, jobs);
	}
		
	public void demo(int jobCount, List<Integer> specificJobs) {
		boolean testMode = jobCount == Integer.MIN_VALUE; // no dummy jobs and use only last created jobs
		
		displayTitle();
		
		initPublisher();
		PlaneMXImpl.instance(); // Init the MX side to prevent from errors if test is not loaded
		
		JobGenerator generator = new JobGenerator(jobCount, testMode, specificJobs);
		applySystemJobs(generator);
		
		List<ExecutableJob> executablesJobs = generator.getExecutableJobs();
		fireSystemEventsForExecutableJobs(executablesJobs);
		
		ExecutableJobAdjuster adjuster = new ExecutableJobAdjuster();
		adjuster.adjustJobs(executablesJobs);
		
		ExecutableJobScheduler scheduler = new ExecutableJobScheduler(
				generator.getExecutableJobs(),
				this.jmonitor,
				testMode
				);
		scheduler.start();
	}
	
	public void list() {
		logger.info("Executable jobs :");
		logger.info("");
		ExecutableJobDefinition[] execJobs = ExecutableJobDefinition.values();
		for (int i=1; i<execJobs.length; i++) {
			logger.info("\t" + i + " - " + execJobs[i].toString());
		}
		
		logger.info("");
		logger.info("System jobs :");
		logger.info("");		
		SystemJobDefinition[] systemJobs = SystemJobDefinition.values();
		for (int i=0; i<systemJobs.length; i++) {
			logger.info("\t" + (i+1) + " - " + systemJobs[i].toString());
		}
	}

	private void fireSystemEventsForExecutableJobs(List<ExecutableJob> executablesJobs) {
		for (ExecutableJob job : executablesJobs) {
			if (job.isSystemEventBased()) {
				JzrStandardEvent event = new JzrStandardEvent(
						LaborEventCode.JZR_LAB_01,
						"Executable job : " + job.getEventName());			
				this.jmonitor.fireSystemEvent(event);				
			}
		}
	}

	private void applySystemJobs(JobGenerator generator) {
		
		for (SystemJob job : generator.getSystemJobs()) {
			JzrStandardEvent event = new JzrStandardEvent(
					LaborEventCode.JZR_LAB_01,
					"System job : " + job.getEventName());
			
			if (job.isFullSessionScope())
				this.jmonitor.fireSystemEvent(event);
			else
				this.jmonitor.fireGlobalEvent(event);
			
			job.work();
		}
	}

	private void displayTitle() {
		// https://www.kammerl.de/ascii/AsciiSignature.php  thin font
		logger.info("");
		logger.info("============================================================================================================");
		logger.info("");
		logger.info("        |                             |         |                           |                               ");
		logger.info("        |,---.,   .,---,,---.,---.    |    ,---.|---.,---.,---.,---.    ,---|,---.,-.-.,---.                ");
		logger.info("        ||---'|   | .-' |---'|        |    ,---||   ||   ||    `---.    |   ||---'| | ||   |                ");
		logger.info("    `---'`---'`---|'---'`---'`        `---'`---^`---'`---'`    `---'    `---'`---'` ' '`---'                ");
		logger.info("              `---'                                                                                         ");
		logger.info("============================================================================================================");
	}
	
	private void initPublisher() {
		Properties props = new Properties();
		props.put(JeyzerPublisherInit.EVENTS_INFO_LIMIT_PROPERTY, Integer.toString(EVENTS_INFO_LIMIT));
		props.put(JeyzerPublisherInit.EVENTS_WARNING_LIMIT_PROPERTY, Integer.toString(EVENTS_WARN_LIMIT));
		props.put(JeyzerPublisherInit.PUBLISHER_ENABLE_JZR_RECORDER_COLLECTION_EVENT_PROPERTY, Boolean.FALSE.toString());
		JeyzerPublisher.instance().init(props);
		
		List<String> attributeNames = new ArrayList<>();
		attributeNames.add(MANIFEST_IMPL_TITLE);
		attributeNames.add(MANIFEST_IMPL_VERSION);
		attributeNames.add(MANIFEST_IMPL_BUILD);
		Map<String, String> attributes = readManifestAttributes(attributeNames);
		
		JeyzerPublisher mgr = JeyzerPublisher.instance();
		mgr.setProcessName(attributes.get(MANIFEST_IMPL_TITLE));
		mgr.setProcessVersion(attributes.get(MANIFEST_IMPL_VERSION));
		mgr.setProcessBuildNumber(attributes.get(MANIFEST_IMPL_BUILD));
		mgr.setProfileName(JEYZER_PROFILE);
		mgr.setNodeName(NODE_NAME);
		
		jmonitor = JeyzerPublisher.instance().getMonitorHandler(SOURCE_NAME, LaborEventCode.SERVICE);
	}
	
	private Map<String, String> readManifestAttributes(List<String> names) {
		Map<String, String> attributes = new HashMap<>();
		
		try {
			Class<LaborsDemo> clazz = LaborsDemo.class;
			String className = clazz.getSimpleName() + ".class";
			String classPath = clazz.getResource(className).toString();
			if (!classPath.startsWith("jar")) {
				// Class not from JAR
				return attributes;
			}
			String manifestPath = classPath.substring(0,
					classPath.lastIndexOf('!') + 1)
					+ "/META-INF/MANIFEST.MF";
			Manifest manifest = new Manifest(new URL(manifestPath).openStream());
			Attributes attr = manifest.getMainAttributes();
			for (String name : names){
				String value = attr.getValue(name);
				// can be null
				attributes.put(name, value);
			}
		} catch (IOException e) {
			logger.error("Failed to read Manifest", e);
		}
		
		return attributes;
	}
	
	private static int parseScopeParam(String[] args) {
		String arg = args[0].substring(SCOPE_PARAM.length());
		if (SCOPE_VALUE_ALL.equalsIgnoreCase(arg))
			return Integer.MAX_VALUE;
		else if (SCOPE_VALUE_TEST.equalsIgnoreCase(arg))
			return Integer.MIN_VALUE;
		else
			return Integer.parseInt(arg);
	}
	
	private static boolean parseListParam(String[] args) {
		return args.length != 0 ? (LIST_PARAM.equals(args[0]) ? true : false) : false;
	}

	private static boolean checkParams(String[] args) {
		if (args.length == 0 || args.length >= 2 || LIST_HELP.equalsIgnoreCase(args[0])) {
			displayMenu();
			return false;
		}
		
		String param = args[0].toLowerCase();
		if (!param.startsWith(JOBS_PARAM) && !param.startsWith(SCOPE_PARAM) && !param.startsWith(LIST_PARAM)) {
			displayMenu();
			return false;
		}
		
		return true;
	}

	private static void displayMenu() {
		System.out.println("Demo labors command help. Use one of those options :");
		System.out.println("\t scope=<value> : number of random jobs to execute");
		System.out.println("\t\t\t Use the \"all\" value to execute all the jobs (takes 1h+)");
		System.out.println("\t\t\t Use the \"test\" value to execute the last 2 created executable and system jobs");
		System.out.println("\t jobs : comma separated list of job names");
		System.out.println("\t list : list all the executable and system job names");
		System.out.println("\t help : displays the current help");
	}

	private static List<Integer> parseJobsParam(String[] args) {
		List<Integer> jobs = new ArrayList<>();
		int execJobCounter = 0;
		
		if (!args[0].startsWith(JOBS_PARAM))
			return jobs; // empty
				
		String[] jobSet = args[0].substring(JOBS_PARAM.length()).split(",");
		for (int i=0; i<jobSet.length ;i++) {
			String jobName = jobSet[i].toUpperCase();
			
			try {
				ExecutableJobDefinition jobDef = ExecutableJobDefinition.valueOf(jobName);
				if (jobDef.ordinal()>0) { // exclude dummy jobs
					jobs.add(jobDef.ordinal());	
					execJobCounter++;
				}
				continue;
			}
			catch (IllegalArgumentException e) { 
				// do nothing, try the system job now
			}
			
			try {
				SystemJobDefinition jobDef = SystemJobDefinition.valueOf(jobName);
				jobs.add(jobDef.ordinal() + 1000);
				continue;
			}
			catch (IllegalArgumentException e) {
			}
			
			logger.error("Unknown job name : " + jobName + ". Please check the job list.");
			System.exit(-1);
		}
		
		if (execJobCounter == 0) {
			logger.error("Specified jobs must have at least one executable job. Please check the job list.");
			System.exit(-1);
		}
		
		if (jobs.isEmpty()) {
			logger.error("No jobs provided. Please check the job list.");
			System.exit(-1);
		}
		
		return jobs;
	}
}
