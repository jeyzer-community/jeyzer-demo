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
import org.jeyzer.demo.labors.job.executable.ExecutableJobScheduler;
import org.jeyzer.demo.labors.job.executable.impl.PlaneMXImpl;
import org.jeyzer.demo.labors.job.system.SystemJob;
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
	
	public static final String SCOPE_PARAM = "scope=";
	public static final String SCOPE_VALUE_ALL = "all";
	public static final String SCOPE_VALUE_TEST = "test"; // take the last 3
	public static final String SCOPE_DEFAULT_VALUE = "3";
	
	private JzrMonitorHandler jmonitor;
	
	public void demo(int jobCount) {
		boolean testMode = jobCount == Integer.MIN_VALUE; // no dummy jobs and use only last created jobs
		
		displayTitle();
		initPublisher();
		PlaneMXImpl.instance(); // Init the MX side to prevent from errors if test is not loaded
		
		JobGenerator generator = new JobGenerator(jobCount, testMode);
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
		props.put(JeyzerPublisherInit.PUBLISHER_ENABLE_JZR_RECORDER_COLLECTION_EVENT_PROPERTY, "false");
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
	
	private static int parseParam(String[] args) {
		String arg = args.length != 0 ? args[0].substring(SCOPE_PARAM.length()) : SCOPE_DEFAULT_VALUE;
		if (SCOPE_VALUE_ALL.equalsIgnoreCase(arg))
			return Integer.MAX_VALUE;
		else if (SCOPE_VALUE_TEST.equalsIgnoreCase(arg))
			return Integer.MIN_VALUE;
		else
			return Integer.parseInt(arg);
	}

	@Exclude
	public static void main(String[] args) {
		int jobCount = parseParam(args);
		LaborsDemo demo = new LaborsDemo();
		demo.demo(jobCount);
	}

}
