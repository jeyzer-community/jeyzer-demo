package org.jeyzer.demo.features.mx.generic.volley;

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

import org.jeyzer.demo.event.codes.FeatureEventCode;
import org.jeyzer.demo.features.Feature;
import org.jeyzer.publish.event.JzrStandardEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VolleyBallPlayer extends Feature {
	
	private static final Logger logger = LoggerFactory.getLogger(VolleyBallPlayer.class);
	
	private static final String FEATURE_NAME = "Generic MX";
	
	@Override
	public void show() throws InterruptedException {
		VolleyBallGame game = new VolleyBallGame();
		
		publishThreadApplicativeEvent(
				new JzrStandardEvent(FeatureEventCode.JZR_FEA_11));
		
		log("VolleyBallPlayer", "starts");
		
		VolleyBallMXBean bean = new VolleyBallMXBeanImpl(game);
		register(bean);
		
		for(int i=0; i<4; i++){
			executeAction(game);
		}
		
		log("VolleyBallPlayer", "ends");
	}
	
	private void executeAction(VolleyBallGame game) throws InterruptedException {
		switch (game.getAction()){
		case Attack: 
			attack();
			break;
		case Pass:
			pass();
			break;
		case Serve:
			serve();
			break;
		case Set:
			set();
			break;
		default:
			pass();
		}
		log("VolleyBallPlayer", "updates VolleyBallPlayer action");
		game.updateAction();
	}

	private void set() throws InterruptedException {
		hold();
	}

	private void serve() throws InterruptedException {
		hold();
	}

	private void pass() throws InterruptedException {
		hold();
	}

	private void attack() throws InterruptedException {
		hold();
	}

	@Override
	public String getName() {
		return FEATURE_NAME;
	}
	
	void register(VolleyBallMXBean bean){
		ObjectName mxbeanName = null;
		try {
			mxbeanName = new ObjectName(VolleyBallMXBean.VOLLEY_MXBEAN_NAME);
		} catch (MalformedObjectNameException e) {
			logger.warn("Warning : Failed to start Volley Ball MX player. Error is : " + e.getMessage());
		}
	    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
	    
	    try {
			mbs.registerMBean(bean, mxbeanName);
		} catch (Exception e) {
			logger.warn("Warning : Failed to start Volley Ball MX player. Error is : " + e.getMessage());
		}
	}
}
