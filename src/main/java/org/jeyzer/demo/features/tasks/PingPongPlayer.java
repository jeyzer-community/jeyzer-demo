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
public class PingPongPlayer extends Feature {
	
	private static final String FEATURE_NAME = "Ping pong";
	
	private static int execCount = 0;
	
	public enum PLAYER_TYPE { PING, PONG; }
	
	private PLAYER_TYPE type;
	
	@Exclude
	public PingPongPlayer(PLAYER_TYPE type){
		this.type =type;  
	}
	
	@Override
	public void show() throws InterruptedException {
		
		// set distinct event message, otherwise subsequent event may be ignored
		publishThreadApplicativeEvent(
				new JzrStandardEvent(
						FeatureEventCode.JZR_FEA_10, 
						"Ping pong player " + this.type.name() + " executing hit " + execCount++ + ". Watch the match !")
				);
		
		if (PLAYER_TYPE.PING.equals(type))
			doPing();
		else
			doPong();
	}
	
	@Override
	@Function
	public String getName() {
		return FEATURE_NAME;
	}
	
	@Function(name="Plays ping (blue)")
	private void doPing() throws InterruptedException{
		log("PingPongPlayer", "starts ping");
		hold();
		log("PingPongPlayer", "ends ping");
	}

	@Operation(name="Plays pong (green)")
	private void doPong() throws InterruptedException{
		log("PingPongPlayer", "starts pong");
		hold();
		log("PingPongPlayer", "ends pong");
	}	
	
	// Inner class annotation tests
	@Exclude
	public static class PingPongReferee{
		
		@Function
		public PingPongReferee(){
			// whatever code here
		}
		
		@Function
		public void watch(){
			// whatever code here
		}
	}
}
