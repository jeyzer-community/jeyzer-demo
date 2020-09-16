package org.jeyzer.demo.features.mx.jeyzer;

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


import java.util.ArrayList;
import java.util.List;

import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.annotations.Operation;
import org.jeyzer.demo.event.codes.FeatureEventCode;
import org.jeyzer.demo.features.Feature;
import org.jeyzer.demo.features.mx.generic.feature.FeaturesPublisher;
import org.jeyzer.demo.shared.DemoHelper;
import org.jeyzer.publish.event.JzrEvent;
import org.jeyzer.publish.event.JzrStandardEvent;

public class LongTennisPlayer extends Player {
	
	private static final String FEATURE_NAME = ""; // no need
	
	public static class TennisRunnable implements Runnable{

		private List<Feature> features = new ArrayList<>();
		
		public TennisRunnable(List<Feature> features){
			this.features = features;
		}
		
		@Override
		@Executor(name="MX Demo thread")
		public void run() {
			for (Feature player : features){
				
				FeaturesPublisher.instance().registerActiveFeature(player.getName());
				
				try {
					player.show();
				} catch (InterruptedException e) {
					logger.error("Demo interrupted", e);
					Thread.currentThread().interrupt();
				}
				
				FeaturesPublisher.instance().unregisterActiveFeature(player.getName());
			}
		}
	}
	
	private static final String JZR_MX_ACTION_PINCIPAL = "Plays tennis";
	
	private TennisGame game;
	
	// static process
	public static final String JZR_MX_LOCATION = "tennis-location";
	public static final String JZR_MX_COURT = "tennis-court";
	public static final String JZR_MX_GROUND = "tennis-ground";
	
	// dynamic thread
	public static final String JZR_MX_PLAYER_SERVES = "tennis-serves";
	public static final String JZR_MX_PLAYER_POINTS = "tennis-points";
	
	private int playCount;
	
	public LongTennisPlayer(TennisGame game, String user, int playCount, String level) {
		super(user, JZR_MX_ACTION_PINCIPAL, level);
		this.game = game;
		this.playCount = playCount;
	}

	@Override
	public void play() throws InterruptedException {
		longRunningTask();
	}
	
	@Override
	public String getName() {
		return FEATURE_NAME;
	}
	
	@Override
	protected boolean isOneshotEvent() {
		return false;
	}

	@Function(name="Long running tennis player")
	private void longRunningTask() throws InterruptedException {
		log("LongTennisPlayer", "\t\t Starts long tennis player");

		JzrEvent event = new JzrStandardEvent(
				FeatureEventCode.JZR_FEA_12, 
				"Tennis player " + this.user + " started the match."
				);
		publishThreadApplicativeEvent(event);
		
		game.reset();
		
		for (int i=0; i<playCount; i++){
			updateMXFigures();
			callRandomRun();
		}

		log("LongTennisPlayer", "\t\t Ends long tennis player");
		
		terminateThreadApplicativeEvent(event);
	}

	private void updateMXFigures() {
		int serves = DemoHelper.getNextRandomInt(5) +5;
		int points = DemoHelper.getNextRandomInt(6);

		// =========================
		// Process MX figures
		// =========================
		game.updateMXGameFigures(serves, points);
		
		// =========================
		// Thread MX figures
		// =========================
		// update player serves
		this.jhandler.setContextParameter(JZR_MX_PLAYER_SERVES, Integer.toString(serves));
		// update player score
		this.jhandler.setContextParameter(JZR_MX_PLAYER_POINTS, Integer.toString(points));
	}

	private void callRandomRun() throws InterruptedException{
		switch(DemoHelper.getNextRandomInt(3)) {
			case 0:   
				runLeft();
				break;
			case 1: 
				runRight();
				break;
			default:
				runMiddle();
		}
	}	
	
	private void callRandomShot(String run) throws InterruptedException{
		switch(DemoHelper.getNextRandomInt(10)) {
			case 0: case 1: case 2: case 3: 
			case 4: case 5:  
				forehandShot(run);
				break;
			case 6: case 7: 
				gripShot(run);
				break;
			case 8: 
				backhandShot(run);
				break;
			default:
				serveShot(run);
		}
	}

	@Override
	public void hold() throws InterruptedException{
		Thread.sleep(2500);
	}
	
	@Operation(contentionType="Shot")
	private void backhandShot(String run) throws InterruptedException {
		log("LongTennisPlayer", "\t\t Tennis player " + user + " runs " + run + " and plays backhand shot");
		hold();
	}

	@Operation(contentionType="Shot")
	private void serveShot(String run) throws InterruptedException {
		log("LongTennisPlayer", "\t\t Tennis player " + user + " runs " + run + " and plays serve shot");
		hold();
	}

	@Operation(contentionType="Shot")
	private void gripShot(String run) throws InterruptedException {
		log("LongTennisPlayer", "\t\t Tennis player " + user + " runs " + run + " and plays grip shot");
		hold();
	}

	@Operation(contentionType="Shot")
	private void forehandShot(String run) throws InterruptedException {
		log("LongTennisPlayer", "\t\t Tennis player " + user + " runs " + run + " and plays forehand shot");
		hold();
	}

	@Function
	private void runLeft() throws InterruptedException {
		callRandomShot("left");
	}

	@Function
	private void runMiddle() throws InterruptedException {
		callRandomShot("middle");
	}

	@Function
	private void runRight() throws InterruptedException {
		callRandomShot("right");
	}

}
