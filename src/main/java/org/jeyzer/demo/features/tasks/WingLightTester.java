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


import org.jeyzer.annotations.Exclude;
import org.jeyzer.annotations.ExcludeThreadName;
import org.jeyzer.annotations.ExecutorThreadName;
import org.jeyzer.annotations.Function;
import org.jeyzer.annotations.Operation;
import org.jeyzer.demo.event.codes.FeatureEventCode;
import org.jeyzer.demo.features.Feature;
import org.jeyzer.publish.event.JzrStandardEvent;

// sample annotations test
@ExecutorThreadName(name="Thread executor", patternRegex="ThreadNameExecutor.*")
@ExcludeThreadName(name="Thread to exclude", pattern="ExcludedThreadName",size=10)
@Operation(priority=901, lowStack=true)
public class WingLightTester extends Feature {
	
	private static final String FEATURE_NAME = "Wing light tester";
	
	private static int execCount = 0;
	
	public enum WING_TYPE { LEFT, RIGHT; }
	
	private WING_TYPE type;
	
	@Exclude
	public WingLightTester(WING_TYPE type){
		this.type =type;  
	}
	
	@Override
	public void show() throws InterruptedException {
		
		// set distinct event message, otherwise subsequent event may be ignored
		publishThreadApplicativeEvent(
				new JzrStandardEvent(
						FeatureEventCode.JZR_FEA_10, 
						"Wing light" + this.type.name() + " blink count event #" + execCount++)
				);
		
		if (WING_TYPE.LEFT.equals(type))
			lightYellow();
		else
			lightGreen();
	}
	
	@Override
	@Function
	public String getName() {
		return FEATURE_NAME;
	}
	
	@Function(name="Left wing light (yellow)")
	private void lightYellow() throws InterruptedException{
		log("WingLightTester", "starts yellow blinking");
		hold();
		log("WingLightTester", "ends yellow blinking");
	}

	@Function(name="Rights wing light (green)")
	private void lightGreen() throws InterruptedException{
		log("WingLightTester", "starts green blinking");
		hold();
		log("WingLightTester", "ends green blinking");
	}	
	
	// Inner class annotation tests
	@Exclude
	public static class EmergencyLightTester{
		
		@Function
		public EmergencyLightTester(){
			// whatever code here
		}
		
		@Function
		public void light(){
			// whatever code here
		}
	}
}
