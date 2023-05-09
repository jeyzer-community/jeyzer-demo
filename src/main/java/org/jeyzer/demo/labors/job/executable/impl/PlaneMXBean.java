package org.jeyzer.demo.labors.job.executable.impl;

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


public interface PlaneMXBean {
	
	/**
	 * The MX plane object name
	 */
	public static final String MXBEAN_PLANE_NAME = "org.jeyzer.demo:type=DemoPlane";
	
	public long getAltitude();
	
	public String getState();
	
	public long getAge();
	
	public String getModel();

}
