package org.jeyzer.demo.features.discovery;

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


import org.jeyzer.demo.event.codes.FeatureEventCode;
import org.jeyzer.demo.features.Feature;
import org.jeyzer.publish.event.JzrStandardEvent;

public class BoeingPlane extends Feature {

	private static final String FEATURE_NAME = "Discovery";
	
	@Override
	public void show() throws InterruptedException {
		publishThreadApplicativeEvent(
				new JzrStandardEvent(FeatureEventCode.JZR_FEA_15));
		maintenance();
	}
	
	@Override
	public String getName() {
		return FEATURE_NAME;
	}

	private void maintenance() throws InterruptedException {
		log("BoeingPlane", "starts");
		hold(); 
		log("BoeingPlane", "ends");	 
	}

}
