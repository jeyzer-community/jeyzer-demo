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

import org.jeyzer.demo.event.codes.FeatureEventCode;
import org.jeyzer.demo.features.Feature;
import org.jeyzer.publish.event.JzrStandardEvent;


public class DysfunctionalCodeSequence extends Feature {

	private static final String FEATURE_NAME = "Bug Pattern";
	
	@Override
	public void show() throws InterruptedException {
		publishThreadApplicativeEvent(
				new JzrStandardEvent(
						FeatureEventCode.JZR_FEA_03, 
						"Problem detected. Please contact your support giving this reference : " + FeatureEventCode.JZR_FEA_03.getAbbreviation())
				);
		dysfunctionalCode1();
	}
	
	@Override
	public String getName() {
		return FEATURE_NAME;
	}

	private void dysfunctionalCode1() throws InterruptedException {
		dysfunctionalCode2();
	}

	private void dysfunctionalCode2() throws InterruptedException {
		log("DysfunctionalCodeSequence", "starts");
		hold();
		hold();
		log("DysfunctionalCodeSequence", "ends");
	}
}
