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



import java.util.Map;

import org.jeyzer.demo.shared.DemoHelper;
import org.jeyzer.publish.JeyzerPublisher;

public class FlightPlan {
	
	// dynamic process
	private static final String JZR_MX_FLIGHT_WIND_FORCES = "flight-wind-forces";
	private static final String JZR_MX_FUEL_CONSUMED = "fuel-consumed";
	private static final String JZR_MX_ALL_FLIGHTS_MAX_ALTITUDE = "flight-max-altitude";

	private int plannedFlights;
	private int currentFlightCounter;
	
	public FlightPlan(int plannedFlights) {
		this.plannedFlights = plannedFlights;
		this.currentFlightCounter = plannedFlights;
	}

	public synchronized void reset() {
		JeyzerPublisher mgr = JeyzerPublisher.instance();
		mgr.removeDynamicProcessContextParam(JZR_MX_FLIGHT_WIND_FORCES);
		mgr.removeDynamicProcessContextParam(JZR_MX_ALL_FLIGHTS_MAX_ALTITUDE);
	}
	
	public synchronized void updateMXFlightFigures(int altitude, int fuel) {
		// reset
		if (currentFlightCounter == 0) {
			currentFlightCounter = plannedFlights;
			reset();
		}
		
		JeyzerPublisher mgr = JeyzerPublisher.instance();
		Map<String, String> flightParams = mgr.getDynamicProcessContextParams();
		
		// update fuel, cumulative
		int previousfuelTotal = flightParams.get(JZR_MX_FUEL_CONSUMED) != null ? Integer.parseInt(flightParams.get(JZR_MX_FUEL_CONSUMED)) : 0;
		mgr.setDynamicProcessContextParam(JZR_MX_FUEL_CONSUMED, Integer.toString(previousfuelTotal + fuel));
		
		int maxAlt = flightParams.get(JZR_MX_ALL_FLIGHTS_MAX_ALTITUDE) != null ? Integer.parseInt(flightParams.get(JZR_MX_ALL_FLIGHTS_MAX_ALTITUDE)) : 0;
		if (altitude > maxAlt)
			mgr.setDynamicProcessContextParam(JZR_MX_ALL_FLIGHTS_MAX_ALTITUDE, Integer.toString(altitude));
			
		// update wind force
		int windForce = DemoHelper.getNextRandomInt(10);
		String allWindForces = flightParams.get(JZR_MX_FLIGHT_WIND_FORCES);
		if (allWindForces == null){
			// first one
			allWindForces = Integer.toString(windForce);
		}else{
			// let's append on right side
			allWindForces += " / " + Integer.toString(windForce);
		}
		mgr.setDynamicProcessContextParam(JZR_MX_FLIGHT_WIND_FORCES, allWindForces);
		
		this.currentFlightCounter--;
	}
}