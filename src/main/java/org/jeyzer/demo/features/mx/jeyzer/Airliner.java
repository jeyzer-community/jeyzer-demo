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

public class Airliner extends Plane {

	private static final String FEATURE_NAME = ""; // no need

	public static class AirlinerRunnable implements Runnable {

		private List<Feature> features = new ArrayList<>();

		public AirlinerRunnable(List<Feature> features) {
			this.features = features;
		}

		@Override
		@Executor(name = "MX Demo thread")
		public void run() {
			for (Feature aircraft : features) {

				FeaturesPublisher.instance().registerActiveFeature(aircraft.getName());

				try {
					aircraft.show();
				} catch (InterruptedException e) {
					logger.error("Demo interrupted", e);
					Thread.currentThread().interrupt();
				}

				FeaturesPublisher.instance().unregisterActiveFeature(aircraft.getName());
			}
		}
	}

	private static final String JZR_MX_ACTION_PINCIPAL = "Flies";

	private FlightPlan fightPlan;
	private int fuelAdjust;

	// static process
	public static final String JZR_MX_DEPARTURE_AIRPORT = "departure-airport";
	public static final String JZR_MX_DESTINATION_AIRPORT = "destination-airport";
	public static final String JZR_MX_FLIGHT_LINE_TYPE = "flight-line-type";

	// dynamic thread
	public static final String JZR_MX_FLIGHT_MAX_ALTITUDE = "flight-max-altitude";
	public static final String JZR_MX_FUEL_CONSUMED = "fuel-consumed";

	private int distance;

	public Airliner(FlightPlan fightPlan, String flightId, int distance, String flightHours) {
		super(flightId, JZR_MX_ACTION_PINCIPAL, flightHours);
		this.fightPlan = fightPlan;
		this.distance = distance;
	}

	@Override
	public void flight() throws InterruptedException {
		longRangeFlight();
	}

	@Override
	public String getName() {
		return FEATURE_NAME;
	}

	@Override
	protected boolean isOneshotEvent() {
		return false;
	}

	@Function(name = "Long range flight")
	private void longRangeFlight() throws InterruptedException {
		log("Airliner", "\t\t Starts long range flight");

		JzrEvent event = new JzrStandardEvent(FeatureEventCode.JZR_FEA_12,
				"Plane " + this.flightId + " is now flying over the Atlantic.");
		publishThreadApplicativeEvent(event);

		fightPlan.reset();

		for (int i = 0; i < distance; i++) {
			callRandomMode();
			updateMXFigures();
		}

		log("Airliner", "\t\t Ends long range flight");

		terminateThreadApplicativeEvent(event);
	}

	private void updateMXFigures() {
		int altitude = DemoHelper.getNextRandomInt(45000);
		int fuel = DemoHelper.getNextRandomInt(100) + ( this.fuelAdjust > 0 ? this.fuelAdjust : 0 );

		// =========================
		// Process MX figures
		// =========================
		fightPlan.updateMXFlightFigures(altitude, fuel);

		// =========================
		// Thread MX figures
		// =========================
		// update max altitude
		this.jhandler.setContextParameter(JZR_MX_FLIGHT_MAX_ALTITUDE, Integer.toString(altitude));
		// update fuel
		this.jhandler.setContextParameter(JZR_MX_FUEL_CONSUMED, Integer.toString(fuel));
	}

	private void callRandomMode() throws InterruptedException {
		switch (DemoHelper.getNextRandomInt(3)) {
		case 0:
			assisted();
			break;
		case 1:
			manual();
			break;
		default:
			autopilot();
		}
	}

	private void callRandomFuelConsumption(String flightMode) throws InterruptedException {
		switch (DemoHelper.getNextRandomInt(10)) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			cruise(flightMode);
			break;
		case 6:
		case 7:
			accelerate(flightMode);
			break;
		case 8:
			decelerate(flightMode);
			break;
		default:
			putGas(flightMode);
		}
	}

	@Override
	public void hold() throws InterruptedException {
		Thread.sleep(2500);
	}

	@Operation(contentionType = "Fuel consumption")
	private void decelerate(String flightMode) throws InterruptedException {
		log("Airliner", "\t\t Plane " + flightId + " flies in " + flightMode + " mode and decelarates");
		hold();
		this.fuelAdjust = -25;
	}

	@Operation(contentionType = "Fuel consumption")
	private void putGas(String flightMode) throws InterruptedException {
		log("Airliner", "\t\t Plane " + flightId + " flies in " + flightMode + " mode and puts gas");
		hold();
		this.fuelAdjust = 50;
	}

	@Operation(contentionType = "Fuel consumption")
	private void accelerate(String flightMode) throws InterruptedException {
		log("Airliner", "\t\t Plane " + flightId + " flies in " + flightMode + " mode and accelerates");
		hold();
		this.fuelAdjust = 25;
	}

	@Operation(contentionType = "Fuel consumption")
	private void cruise(String flightMode) throws InterruptedException {
		log("Airliner", "\t\t Plane " + flightId + " flies in " + flightMode + " mode at constant speed");
		hold();
		this.fuelAdjust = 0;
	}

	@Function
	private void assisted() throws InterruptedException {
		callRandomFuelConsumption("assisted");
	}

	@Function
	private void autopilot() throws InterruptedException {
		callRandomFuelConsumption("auto");
	}

	@Function
	private void manual() throws InterruptedException {
		callRandomFuelConsumption("manual");
	}

}
