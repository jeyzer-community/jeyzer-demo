package org.jeyzer.demo.features.mx.generic.volley;

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



public class VolleyBallMXBeanImpl implements VolleyBallMXBean {
	
	private VolleyBallGame game;
	
	public VolleyBallMXBeanImpl(VolleyBallGame game){
		this.game = game;
	}
	
	public String getFormationType(){
		return this.game.getType().toString();
	}
	
	public String getGameVariant(){
		return this.game.getVariant().name();
	}
	
	public String getAction(){
		return this.game.getAction().name();
	}
}
