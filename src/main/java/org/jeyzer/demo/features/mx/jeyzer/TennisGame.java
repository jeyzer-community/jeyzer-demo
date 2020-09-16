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



import java.util.Map;

import org.jeyzer.demo.shared.DemoHelper;
import org.jeyzer.publish.JeyzerPublisher;

public class TennisGame {
	
	// dynamic process
	private static final String JZR_MX_GAME_SCORE = "tennis-score";
	private static final String JZR_MX_GAME_BALLS_PLAYED = "tennis-balls-played";
	private static final String JZR_MX_GAME_SERVES = "tennis-serves";

	private boolean initialized = false;
	
	private boolean updatedByPlayer1 = false;
	
	public synchronized void reset() {
		if (!initialized){
			// initialize dynamic values
			JeyzerPublisher mgr = JeyzerPublisher.instance();
			mgr.setDynamicProcessContextParam(JZR_MX_GAME_SCORE, "0/0");
			mgr.setDynamicProcessContextParam(JZR_MX_GAME_BALLS_PLAYED, "0");
			mgr.setDynamicProcessContextParam(JZR_MX_GAME_SERVES, "0");
			initialized = true;
		}
	}
	
	public synchronized void updateMXGameFigures(int serves, int points) {
		JeyzerPublisher mgr = JeyzerPublisher.instance();

		int balls = serves + DemoHelper.getNextRandomInt(10);		

		// update balls, cumulative
		int previousvalue = 0;
		Map<String, String> gameParams = mgr.getDynamicProcessContextParams();
		if (gameParams.get(JZR_MX_GAME_BALLS_PLAYED) != null)
			previousvalue = Integer.parseInt(gameParams.get(JZR_MX_GAME_BALLS_PLAYED));
		mgr.setDynamicProcessContextParam(JZR_MX_GAME_BALLS_PLAYED, (Integer.toString(previousvalue + balls)));
		
		// update game serves, non cumulative
		previousvalue = 0;
		if (gameParams.get(JZR_MX_GAME_SERVES) != null && updatedByPlayer1)
			previousvalue = Integer.parseInt(gameParams.get(JZR_MX_GAME_SERVES));		
		mgr.setDynamicProcessContextParam(JZR_MX_GAME_SERVES, Integer.toString(previousvalue + serves));
		updatedByPlayer1 = !updatedByPlayer1; // flip value
			
		// update game score
		String score = null;
		if (gameParams.get(JZR_MX_GAME_SCORE) != null)
			score = gameParams.get(JZR_MX_GAME_SCORE);
		if (score == null){
			// first one let's set left side
			score = Integer.toString(points);
		}else{
			if (score.indexOf('/') != -1)
				// previous score, let's reset
				score = Integer.toString(points);
			else
				// let's set right side
				score += " / " + Integer.toString(points);
		}
		mgr.setDynamicProcessContextParam(JZR_MX_GAME_SCORE, score);
	}
	
}
