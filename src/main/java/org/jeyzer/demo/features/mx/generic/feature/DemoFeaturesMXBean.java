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


public interface DemoFeaturesMXBean {
	
	public static final String DEMO_MXBEAN_NAME = "org.jeyzer.demo:type=DemoFeatures";
	
	public String getDemoName();
	
	public String getVersion();
	
	public String getPlayedFeatures();
	
}
