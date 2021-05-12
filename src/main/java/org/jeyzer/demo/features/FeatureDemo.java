package org.jeyzer.demo.features;

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
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.jeyzer.annotations.Exclude;
import org.jeyzer.demo.dup1.DummyDemo1;
import org.jeyzer.demo.dup2.DummyDemo2;
import org.jeyzer.demo.dup3.DummyDemo3;
import org.jeyzer.demo.event.codes.DemoEventCode;
import org.jeyzer.demo.features.discovery.AirbusPlane;
import org.jeyzer.demo.features.discovery.AntonovPlane;
import org.jeyzer.demo.features.discovery.BoeingPlane;
import org.jeyzer.demo.features.discovery.McDonnellDouglasPlane;
import org.jeyzer.demo.features.discovery.TupolevPlane;
import org.jeyzer.demo.features.event.DemoEventManager;
import org.jeyzer.demo.features.mx.generic.feature.FeaturesPublisher;
import org.jeyzer.demo.features.mx.generic.fighter.FighterAircraft;
import org.jeyzer.demo.features.mx.jeyzer.Airliner;
import org.jeyzer.demo.features.mx.jeyzer.FlightPlan;
import org.jeyzer.demo.features.mx.jeyzer.Airliner.AirlinerRunnable;
import org.jeyzer.demo.features.tasks.CPUConsumingTask;
import org.jeyzer.demo.features.tasks.Deadlock;
import org.jeyzer.demo.features.tasks.DysfunctionalCodeSequence;
import org.jeyzer.demo.features.tasks.FrozenCode;
import org.jeyzer.demo.features.tasks.HiatusEnd;
import org.jeyzer.demo.features.tasks.HiatusStart;
import org.jeyzer.demo.features.tasks.LockedThreads;
import org.jeyzer.demo.features.tasks.MemoryConsumingTask;
import org.jeyzer.demo.features.tasks.MemoryExhaustingTask;
import org.jeyzer.demo.features.tasks.WingLightTester;
import org.jeyzer.demo.features.tasks.WingLightTester.WING_TYPE;
import org.jeyzer.demo.shared.DemoHelper;
import org.jeyzer.demo.shared.DemoSharedLoader;
import org.jeyzer.publish.JeyzerPublisher;
import org.jeyzer.publish.event.JzrStandardEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeatureDemo {
	
	private static final Logger logger = LoggerFactory.getLogger(FeatureDemo.class);
	
	private static final String JEYZER_PROFILE = "demo-features";
	private static final String NODE_NAME      = "Demo features";

	// applicative event source name
	public static final String SOURCE_NAME = NODE_NAME;
	
	private static final String MANIFEST_IMPL_TITLE   = "Implementation-Title";
	private static final String MANIFEST_IMPL_VERSION = "Implementation-Version";
	private static final String MANIFEST_IMPL_BUILD   = "Implementation-Build";
	
	public void demo() {
		
		loadProcessVersionJars();
		loadSharedDemoJar();
		
		displayTitle();
		
		FeaturesPublisher.instance(); // initialize
		
		// MX features static data : publish up front
		publishFlightInfo();
		
		DemoHelper.updateJVMHeapDumpPathFlag();
		
		// Demo event manager used to publish applicative events.
		// Applicative events are collected by the Jeyzer Recorder.
		DemoEventManager demoEventManager = new DemoEventManager();
		demoEventManager.test();
		
		demoEventManager.publishApplicativeEvent(
				new JzrStandardEvent(
						DemoEventCode.JZR_DEM_01,
						"We would like to inform you that the Features demo has started.")
				);
		
		try {
			Thread.sleep(8000); // give some time to plug any JMX client
		} catch (InterruptedException e1) {
			Thread.currentThread().interrupt();
			demoEventManager.publishApplicativeEvent(new JzrStandardEvent(DemoEventCode.JZR_DEM_03));
		}

		// standard features
		List<Feature> features = createDemoFeatures();
		Object semaphore = new Object();
		FeatureRunnable run0 = new FeatureRunnable(features, 0, semaphore);
		FeatureRunnable run1 = new FeatureRunnable(features, 1, semaphore);
		Thread t0 = new Thread(run0);
		Thread t1 = new Thread(run1);
		t0.setName("Demo thread 1");
		t1.setName("Demo thread 2");

		// MX features
		List<Feature> profilingFeatures1 = new ArrayList<>();
		List<Feature> profilingFeatures2 = new ArrayList<>();
		
		FlightPlan plan1 = new FlightPlan(2);
		profilingFeatures1.add(new Airliner(plan1, "Japan Airlines Flight 987", features.size() + 16, "3500"));
		profilingFeatures2.add(new Airliner(plan1, "British Airways Flight 223", features.size() + 16, "2900"));

		FlightPlan plan2 = new FlightPlan(2);
		profilingFeatures1.add(new Airliner(plan2, "KLM Flight 496", features.size() + 16, "2800"));
		profilingFeatures2.add(new Airliner(plan2, "Lufthansa Flight 777", features.size() + 16, "3000"));
		
		AirlinerRunnable run2 = new AirlinerRunnable(profilingFeatures1); 
		AirlinerRunnable run3 = new AirlinerRunnable(profilingFeatures2);
		
		Thread t2 = new Thread(run2);
		Thread t3 = new Thread(run3);
		t2.setName("Demo thread 3");
		t3.setName("Demo thread 4");

		// start all threads
		t0.start();
		t1.start();
		t2.start();
		t3.start();	
		
		try {
			t0.join();
		} catch (InterruptedException e) {
			logger.error("Demo interrupted", e);
			Thread.currentThread().interrupt();
			demoEventManager.publishApplicativeEvent(new JzrStandardEvent(DemoEventCode.JZR_DEM_03));
		}
		try {
			t1.join();
		} catch (InterruptedException e) {
			logger.error("Demo interrupted", e);
			Thread.currentThread().interrupt();
			demoEventManager.publishApplicativeEvent(new JzrStandardEvent(DemoEventCode.JZR_DEM_03));
		}
		try {
			t2.join();
		} catch (InterruptedException e) {
			logger.error("Demo interrupted", e);
			Thread.currentThread().interrupt();
			demoEventManager.publishApplicativeEvent(new JzrStandardEvent(DemoEventCode.JZR_DEM_03));
		}
		try {
			t3.join();
		} catch (InterruptedException e) {
			logger.error("Demo interrupted", e);
			Thread.currentThread().interrupt();
			demoEventManager.publishApplicativeEvent(new JzrStandardEvent(DemoEventCode.JZR_DEM_03));
		}
		
		demoEventManager.publishApplicativeEvent(
				new JzrStandardEvent(
						DemoEventCode.JZR_DEM_02,
						"Thank you for your attention. Features demo will now stop.")
				);
	}

	private void displayTitle() {
		logger.info("");
		logger.info("============================================================================================================");
		logger.info("");
		logger.info("        |                             ,---.          |                                |                     ");
		logger.info("        |,---.,   .,---,,---.,---.    |__. ,---.,---.|--- .   .,---.,---.,---.    ,---|,---.,-.-.,---.      ");
		logger.info("        ||---'|   | .-' |---'|        |    |---',---||    |   ||    |---'`---.    |   ||---'| | ||   |      ");
		logger.info("    `---'`---'`---|'---'`---'`        `    `---'`---^`---'`---'`    `---'`---'    `---'`---'` ' '`---'      ");
		logger.info("              `---'                                                                                         ");
		logger.info("============================================================================================================");
	}

	private List<Feature> createDemoFeatures() {
		List<Feature> features = new ArrayList<>();
		
		features.add(new WingLightTester(WING_TYPE.LEFT));
		features.add(new WingLightTester(WING_TYPE.RIGHT));
		features.add(new WingLightTester(WING_TYPE.LEFT));
		features.add(new WingLightTester(WING_TYPE.RIGHT));
		features.add(new WingLightTester(WING_TYPE.LEFT));
		features.add(new WingLightTester(WING_TYPE.RIGHT));
		features.add(new FrozenCode());
		features.add(new MemoryConsumingTask());
		features.add(new DysfunctionalCodeSequence());
		features.add(new CPUConsumingTask());
		features.add(new HiatusStart());
		features.add(new HiatusEnd());
		features.add(new BoeingPlane());
		features.add(new TupolevPlane());
		features.add(new AirbusPlane());
		features.add(new AntonovPlane());
		features.add(new McDonnellDouglasPlane());
		features.add(new FighterAircraft()); // MX generic
		features.add(new LockedThreads());
		features.add(new Deadlock());
		features.add(new MemoryExhaustingTask());
		
		return features;
	}

	private void publishFlightInfo() {
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
		
		// static values
		mgr.addStaticProcessContextParam(Airliner.JZR_MX_DEPARTURE_AIRPORT, "London");
		mgr.addStaticProcessContextParam(Airliner.JZR_MX_DESTINATION_AIRPORT, "Tokyo");
		mgr.addStaticProcessContextParam(Airliner.JZR_MX_FLIGHT_LINE_TYPE, "commercial");
	}	

	private Map<String, String> readManifestAttributes(List<String> names) {
		Map<String, String> attributes = new HashMap<>();
		
		try {
			Class<FeatureDemo> clazz = FeatureDemo.class;
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
			logger.error("Failed to read Manifest");
			logger.error("Demo interrupted", e);
		}
		
		return attributes;
	}
	
	private void loadProcessVersionJars() {
		DummyDemo1 demo1 = new DummyDemo1();
		demo1.load();
		DummyDemo2 demo2 = new DummyDemo2();
		demo2.load();
		DummyDemo3 demo3 = new DummyDemo3();
		demo3.load();
	}
	
	private void loadSharedDemoJar() {
		DemoSharedLoader loader = new DemoSharedLoader();
		loader.loadSharedDemoLibrary();
	}
	
	@Exclude
	public static void main(String[] args) {
		FeatureDemo demo = new FeatureDemo();
		demo.demo();
	}
}
