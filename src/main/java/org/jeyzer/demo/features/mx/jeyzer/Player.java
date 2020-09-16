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


import org.jeyzer.demo.features.Feature;
import org.jeyzer.publish.JzrActionContext;

public abstract class Player extends Feature {

	public static final String JZR_MX_LEVEL_FIELD = "level";
	
	protected String user;
	private String actionPrincipal;
	private String level;
	
	protected JzrActionContext jzrActionContext;
	
	public Player(String user, String actionPrincipal, String level) {
		this.user = user;
		this.actionPrincipal = actionPrincipal;
		this.level = level;
	}

	@Override
	public void show() throws InterruptedException {
		jzrActionContext =  createContext(this.user, this.actionPrincipal);
		jzrActionContext.setContextParam(JZR_MX_LEVEL_FIELD, level);
		
		start(jzrActionContext);
		play();
		stop();
	}

	protected abstract void play() throws InterruptedException;
	
}
