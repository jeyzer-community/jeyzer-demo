package org.jeyzer.demo.labors.job.executable.impl;

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


import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaneMXImpl implements PlaneMXBean {
	
	private static final Logger logger = LoggerFactory.getLogger(PlaneMXImpl.class);

	private static final PlaneMXImpl plane = new PlaneMXImpl();
	
	// session related
	private long altitude = 3000L; // normal value
	private String state = "fine";

	// system related
	private final long age = 24;
	private final String model = "AXW 367b";
	
	private PlaneMXImpl() {
		registerMxBean();
	}

	public static PlaneMXImpl instance() {
		return plane;
	}
	
	@Override
	public long getAltitude() {
		return this.altitude;
	}
	
	public void setAltitude(long value) {
		this.altitude = value;
	}
	
	@Override
	public String getState() {
		return this.state;
	}
	
	public void setState(String value) {
		this.state = value;
	}
	
	@Override
	public long getAge() {
		return this.age;
	}

	@Override
	public String getModel() {
		return this.model;
	}
	
	private void registerMxBean() {
		ObjectName mxbeanName = null;
		try {
			mxbeanName = new ObjectName(MXBEAN_PLANE_NAME);
		} catch (MalformedObjectNameException e) {
			logger.error("Warning : Failed to start the MxBeanParameterNumberHigherJob. Error is : " + e.getMessage());
		}
	    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	    
	    try {
			mbs.registerMBean(this, mxbeanName);
		} catch (Exception e) {
			logger.error("Warning : Failed to register the MxBeanParameterNumberHigherJob. Error is : " + e.getMessage());
		}	    
	}
}
