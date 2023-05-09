package org.jeyzer.demo.features.mx.jeyzer;

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


import org.jeyzer.demo.features.Feature;
import org.jeyzer.publish.JzrActionContext;

public abstract class Plane extends Feature {

	public static final String JZR_MX_FLIGHT_HOURS_FIELD = "flight-hours";
	
	protected String flightId;
	private String actionPrincipal;
	private String flightHours;
	
	protected JzrActionContext jzrActionContext;
	
	public Plane(String flightId, String actionPrincipal, String flightHours) {
		this.flightId = flightId;
		this.actionPrincipal = actionPrincipal;
		this.flightHours = flightHours;
	}

	@Override
	public void show() throws InterruptedException {
		jzrActionContext =  createContext(this.flightId, this.actionPrincipal);
		jzrActionContext.setContextParam(JZR_MX_FLIGHT_HOURS_FIELD, flightHours);
		
		start(jzrActionContext);
		flight();
		stop();
	}

	protected abstract void flight() throws InterruptedException;
	
}
