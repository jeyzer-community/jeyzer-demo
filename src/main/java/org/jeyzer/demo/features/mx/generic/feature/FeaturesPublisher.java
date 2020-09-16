package org.jeyzer.demo.features.mx.generic.feature;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeaturesPublisher implements DemoFeaturesMXBean{
	
	private static final Logger logger = LoggerFactory.getLogger(FeaturesPublisher.class);

	public static final String DEMO_FEATURE_NAME = "Demo-Features";
	public static final String DEMO_FEATURE_VERSION = "2.0";
	
	private static final FeaturesPublisher instance = new FeaturesPublisher();
	
	private boolean init = false;
	private Set<String> features = new HashSet<>();
	
	private FeaturesPublisher(){
	}
	
	public static FeaturesPublisher instance(){
		if (!instance.init){
			instance.init = true;
			instance.register();
		}
			
		return instance;
	}

	@Override
	public String getVersion() {
		return DEMO_FEATURE_VERSION;
	}
	
	@Override
	public String getDemoName() {
		return DEMO_FEATURE_NAME;
	}

	@Override
	public String getPlayedFeatures() {
		StringBuilder value = new StringBuilder();
		
		Iterator<String> iter = features.iterator();
		while (iter.hasNext()){
			value.append(iter.next());
		}
		
		return value.toString();
	}
	
	public void registerActiveFeature(String feature){
		features.add(feature);
	}
	
	public void unregisterActiveFeature(String feature){
		features.remove(feature);
	}
	
	private void register(){
		ObjectName mxbeanName = null;
		try {
			mxbeanName = new ObjectName(DEMO_MXBEAN_NAME);
		} catch (MalformedObjectNameException e) {
			logger.warn("Warning : Failed to start Features Publisher. Error is : " + e.getMessage());
		}
	    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
	    
	    try {
			mbs.registerMBean(this, mxbeanName);
		} catch (Exception e) {
			logger.warn("Warning : Failed to start Features Publisher. Error is : " + e.getMessage());
		}
	}
	
}
