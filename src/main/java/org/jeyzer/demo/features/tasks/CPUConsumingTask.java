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
import org.jeyzer.annotations.Operation;
import org.jeyzer.demo.event.codes.FeatureEventCode;
import org.jeyzer.demo.features.Feature;
import org.jeyzer.demo.shared.DemoHelper;
import org.jeyzer.publish.event.JzrStandardEvent;

public class CPUConsumingTask extends Feature {

	private static final String FEATURE_NAME = "CPU";
	
	private double result;
	
	public double getResult() {
		return result;
	}
	
	@Override
	public void show() throws InterruptedException {
		publishThreadApplicativeEvent(
				new JzrStandardEvent(
						FeatureEventCode.JZR_FEA_01,
						"Task consuming high amount of CPU is now started. Any immediate CPU alert notification could therefore be observed."
				)
		);
		consumeCPU();
	}

	@Override
	public String getName() {
		return FEATURE_NAME;
	}

	@Function(name="CPU consuming section : please look at CPU panel for more details")
	@Operation(name="CPU creation : do mathematical operations")
	private void consumeCPU() {
		int value1;
		int value2;
		
		log("CPUConsumingTask", "starts");

		long start = System.currentTimeMillis();
		int count = 0;
		
		while (true){
			value1 = DemoHelper.getNextRandomInt();
			value2 = DemoHelper.getNextRandomInt();
			result  = (double)value1/value2;
			count++;
			if (count == 100000){
				long current = System.currentTimeMillis();
				if ((current - start)> 10000){ // 10 sec
					break;
				}
				count = 0;
			}
		}
		
		log("CPUConsumingTask", "ends");
	}
}
