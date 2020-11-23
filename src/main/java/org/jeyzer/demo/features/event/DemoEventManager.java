package org.jeyzer.demo.features.event;

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
import java.util.List;
import java.util.Properties;

import org.jeyzer.demo.event.codes.DemoEventCode;
import org.jeyzer.demo.features.FeatureDemo;
import org.jeyzer.mx.event.JzrEventLevel;
import org.jeyzer.publish.JeyzerPublisher;
import org.jeyzer.publish.JeyzerPublisherInit;
import org.jeyzer.publish.JzrMonitorHandler;
import org.jeyzer.publish.event.JzrEvent;
import org.jeyzer.publish.event.JzrStandardEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoEventManager {
	
	private static final Logger logger = LoggerFactory.getLogger(DemoEventManager.class);

	public static final int EVENTS_INFO_LIMIT = 1200;
	public static final int EVENTS_WARN_LIMIT = 1100;
	
	private JzrMonitorHandler jmonitor;
	
	public DemoEventManager() {
		initPublisher();
		jmonitor = JeyzerPublisher.instance().getMonitorHandler(FeatureDemo.SOURCE_NAME, DemoEventCode.SERVICE);
	}

	public boolean publishApplicativeEvent(JzrEvent event) {
		return jmonitor.fireGlobalEvent(event);
	}
	
	public boolean publishSystemEvent(JzrEvent event) {
		return jmonitor.fireSystemEvent(event);
	}
	
	public void unpublishApplicativeEvent(JzrEvent event) {
		this.jmonitor.cancelEvent(event);
	}
	
	public void test() {
		testPublisherLimit();
		testDuplicateEvent();
		testSystemEvent();
		testPublisherService();
	}

	private void initPublisher() {
		Properties props = new Properties();
		props.put(JeyzerPublisherInit.EVENTS_INFO_LIMIT_PROPERTY, Integer.toString(EVENTS_INFO_LIMIT));
		props.put(JeyzerPublisherInit.EVENTS_WARNING_LIMIT_PROPERTY, Integer.toString(EVENTS_WARN_LIMIT));
		props.put(JeyzerPublisherInit.PUBLISHER_ENABLE_JZR_RECORDER_COLLECTION_EVENT_PROPERTY, Boolean.FALSE.toString());
		JeyzerPublisher.instance().init(props);
	}
	
	private void testPublisherLimit() {
		if (!JeyzerPublisher.instance().isActive())
			return;
		
		List<JzrEvent> events = new ArrayList<>(EVENTS_INFO_LIMIT + 1);
		
		for (int i=1; i<EVENTS_INFO_LIMIT + 1; i++) {
			JzrEvent event = new JzrStandardEvent(DemoEventCode.JZR_DEM_06, "Test event " + i);
			if (publishApplicativeEvent(event))
				events.add(event);
		}
		
		// Here a publisher internal event should be generated.
		
		// remove the polluting events
		for (JzrEvent event : events)
			unpublishApplicativeEvent(event);
	}
	
	private void testDuplicateEvent() {
		if (!JeyzerPublisher.instance().isActive())
			return;
		
		List<JzrEvent> events = new ArrayList<>(EVENTS_WARN_LIMIT + 1);
		
		for (int i=1; i<EVENTS_WARN_LIMIT + 1; i++) {
			JzrEvent event = new JzrStandardEvent(DemoEventCode.JZR_DEM_07, "Duplicate flooding test event.");
			if (publishApplicativeEvent(event))
				events.add(event); // should come here only once.
		}
		
		if (events.size() > 1)
			logger.warn("Warning : duplicate applicative event detection is broken. Please report the issue.");
	}

	private void testSystemEvent() {
		String country = System.getProperty("user.country");
		
		if (! "MA".equals(country))
			publishSystemEvent(new JzrStandardEvent(
					DemoEventCode.JZR_DEM_05,
					"Process started with wrong user country : " + country + ". Expected one is MA.")
					);
	}

	private void testPublisherService() {
		JeyzerPublisher publisher = JeyzerPublisher.instance();
		
		// this will generate internal publisher events
		publisher.suspendEventCollection(JzrEventLevel.INFO);
		publisher.suspendDataCollection();
		
		// This info event should not be published
		publishApplicativeEvent(new JzrStandardEvent(DemoEventCode.JZR_DEM_04));
		
		// this will generate internal publisher events
		if (!publisher.isEventCollectionActive(JzrEventLevel.INFO))
			publisher.resumeEventCollection(JzrEventLevel.INFO);
		if (!publisher.isDataCollectionActive())
			publisher.resumeDataCollection();
	}
}
