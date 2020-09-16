package org.jeyzer.demo.features.tasks;

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


import org.jeyzer.annotations.Function;
import org.jeyzer.demo.event.codes.FeatureEventCode;
import org.jeyzer.demo.features.Feature;
import org.jeyzer.publish.event.JzrStandardEvent;

public class HiatusStart extends Feature {

	private static final String FEATURE_NAME = "Hiatus begin";
	
	@Override
	public void show() throws InterruptedException {
		publishThreadApplicativeEvent(
				new JzrStandardEvent(FeatureEventCode.JZR_FEA_05));
		hiatusStart();
	}
	
	@Override
	public String getName() {
		return FEATURE_NAME;
	}

	@Function(name="Hiatus detected right after this cell : threads dumps were not generated")
	private void hiatusStart() throws InterruptedException {
		log("HiatusStart", "starts");
		Thread.sleep(15000); // remove manually thread dumps after 5 sec
		log("HiatusStart", "ends");
	}
}
