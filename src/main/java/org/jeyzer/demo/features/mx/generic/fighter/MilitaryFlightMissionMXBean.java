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




public interface MilitaryFlightMissionMXBean {
	
	public static final String FLIGHT_MISSION_MXBEAN_NAME = "org.jeyzer.demo:type=MilitaryFlightMission";
	
	public String getFormation();
	
	public String getModel();
	
	public String getAction();
	
}
