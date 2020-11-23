package org.jeyzer.demo.features.mx.generic.fighter;

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

public class FighterAircraft extends Feature {
	
	private static final Logger logger = LoggerFactory.getLogger(FighterAircraft.class);
	
	private static final String FEATURE_NAME = "Generic MX";
	
	@Override
	public void show() throws InterruptedException {
		MilitaryFlightPlan game = new MilitaryFlightPlan();
		
		publishThreadApplicativeEvent(
				new JzrStandardEvent(FeatureEventCode.JZR_FEA_11));
		
		log("FighterAircraft", "starts");
		
		MilitaryFlightMissionMXBean bean = new MilitaryFlightMissionMXBeanImpl(game);
		register(bean);
		
		for(int i=0; i<4; i++){
			executeAction(game);
		}
		
		log("FighterAircraft", "ends");
	}
	
	private void executeAction(MilitaryFlightPlan game) throws InterruptedException {
		switch (game.getAction()){
		case CISORS: 
			cisor();
			break;
		case IMMELMANN:
			immelmann();
			break;
		case YOYO:
			yoyo();
			break;
		case BARREL:
			barrel();
			break;
		default:
			immelmann();
		}
		log("FighterAircraft", "updates FighterAircraft action");
		game.updateAction();
	}

	private void barrel() throws InterruptedException {
		hold();
	}

	private void yoyo() throws InterruptedException {
		hold();
	}

	private void immelmann() throws InterruptedException {
		hold();
	}

	private void cisor() throws InterruptedException {
		hold();
	}

	@Override
	public String getName() {
		return FEATURE_NAME;
	}
	
	void register(MilitaryFlightMissionMXBean bean){
		ObjectName mxbeanName = null;
		try {
			mxbeanName = new ObjectName(MilitaryFlightMissionMXBean.FLIGHT_MISSION_MXBEAN_NAME);
		} catch (MalformedObjectNameException e) {
			logger.warn("Warning : Failed to start the MX Military Flight mission. Error is : " + e.getMessage());
		}
	    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
	    
	    try {
			mbs.registerMBean(bean, mxbeanName);
		} catch (Exception e) {
			logger.warn("Warning : Failed to start the MX Military Flight mission. Error is : " + e.getMessage());
		}
	}
}
