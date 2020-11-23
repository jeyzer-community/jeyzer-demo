package org.jeyzer.demo.features.discovery;

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


import org.jeyzer.demo.event.codes.FeatureEventCode;
import org.jeyzer.demo.features.Feature;
import org.jeyzer.publish.event.JzrStandardEvent;

public class AntonovPlane extends Feature {

	private static final String FEATURE_NAME = "Discovery";
	
	@Override
	public void show() throws InterruptedException {
		publishThreadApplicativeEvent(
				new JzrStandardEvent(FeatureEventCode.JZR_FEA_14));
		land();
	}
	
	@Override
	public String getName() {
		return FEATURE_NAME;
	}

	private void land() throws InterruptedException {
		log("AntonovPlane", "starts");
		hold(); 
		log("AntonovPlane", "ends");	 
	}
}