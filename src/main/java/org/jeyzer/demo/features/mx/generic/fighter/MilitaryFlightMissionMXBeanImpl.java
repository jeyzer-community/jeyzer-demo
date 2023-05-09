package org.jeyzer.demo.features.mx.generic.fighter;

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



public class MilitaryFlightMissionMXBeanImpl implements MilitaryFlightMissionMXBean {
	
	private MilitaryFlightPlan plane;
	
	public MilitaryFlightMissionMXBeanImpl(MilitaryFlightPlan plane){
		this.plane = plane;
	}
	
	public String getFormation(){
		return this.plane.getFormation().name();
	}
	
	public String getModel(){
		return this.plane.getModel().name();
	}
	
	public String getAction(){
		return this.plane.getAction().name();
	}
}
