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



import java.util.concurrent.atomic.AtomicInteger;

import org.jeyzer.demo.event.codes.FeatureEventCode;
import org.jeyzer.publish.JeyzerPublisher;
import org.jeyzer.publish.JzrActionContext;
import org.jeyzer.publish.JzrActionHandler;
import org.jeyzer.publish.JzrMonitorHandler;
import org.jeyzer.publish.event.JzrEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Feature {
	
	protected static final Logger logger = LoggerFactory.getLogger(Feature.class);
	
	private static final AtomicInteger idCount = new AtomicInteger(0);
	
	protected JzrActionHandler jhandler;
	
	protected JzrMonitorHandler jmonitor = JeyzerPublisher.instance().getMonitorHandler(
			                                  FeatureDemo.SOURCE_NAME, FeatureEventCode.SERVICE);
	
	public abstract void show() throws InterruptedException;
	
	public abstract String getName();

	protected void start (JzrActionContext cxt){
		JeyzerPublisher publisher = JeyzerPublisher.instance();
		
		// Publish an action
		jhandler = publisher.getActionHandler();
		jhandler.startAction(cxt);
	}

	// Can be overriden
	protected boolean isOneshotEvent() {
		return true;
	}

	protected void stop (){
		jhandler.closeAction();
	}
	
	protected JzrActionContext createContext(String user, String action) {
		JzrActionContext ctx = new JzrActionContext();
		ctx.setUser(user);
		ctx.setFunctionPrincipal(action);
		ctx.setId(user + "-" + Integer.toString(idCount.incrementAndGet()));
		return ctx;
	}
	
	protected void hold() throws InterruptedException{
		Thread.sleep(5000);
	}
	
	protected void publishThreadApplicativeEvent(JzrEvent event) {
		boolean result;
		if (isOneshotEvent())
			result = jmonitor.fireLocalThreadEvent(event);
		else
			result = jmonitor.startLocalThreadEvent(event);
		
		if (!result)
			logger.warn("Warning - Failed to publish applicative event : " + event.getMessage());
	}
	
	protected void terminateThreadApplicativeEvent(JzrEvent event) {
		boolean result = jmonitor.terminateEvent(event);
		if (!result)
			logger.warn("Warning - Failed to terminate applicative event : " + event.getMessage());
	}
	
	protected void log(String caller, String message) {
		logger.info(caller + " : "+ message);
	}
}
