package org.jeyzer.demo.features.tasks;

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




import java.util.ArrayList;
import java.util.List;

import org.jeyzer.annotations.Function;
import org.jeyzer.annotations.Operation;
import org.jeyzer.demo.event.codes.FeatureEventCode;
import org.jeyzer.demo.features.Feature;
import org.jeyzer.publish.event.JzrStandardEvent;

public class MemoryConsumingTask extends Feature {

	private static final String FEATURE_NAME = "Memory";
	
	private List<Long> elements = new ArrayList<>();
	
	@Override
	public void show() throws InterruptedException {
		publishThreadApplicativeEvent(
				new JzrStandardEvent(
						FeatureEventCode.JZR_FEA_08,
						"Task consuming high amount of memory is now started. Any immediate memory alert notification could therefore be observed."
						)
				);
		consumeMemory();
	}
	
	@Override
	public String getName() {
		return FEATURE_NAME;
	}
	
	@Function(name="Memory consuming section : please look at Memory panel for more details")
	@Operation(name="Memory creation : fill list with random long numbers")
	protected void consumeMemory() throws InterruptedException{
		log("MemoryConsumingTask", "starts");
		for (long i=1; i<3000000; i++){ //around 300 Mb
			this.elements.add(i);
		}
		hold();
		this.elements.clear();
		System.gc();
		log("MemoryConsumingTask", "ends");
	}
}
