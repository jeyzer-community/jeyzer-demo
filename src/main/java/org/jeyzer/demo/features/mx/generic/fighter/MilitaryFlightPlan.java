package org.jeyzer.demo.features.mx.generic.fighter;

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

import org.jeyzer.demo.shared.DemoHelper;

public class MilitaryFlightPlan {
	
	public enum FORMATION { 
		ECHELON,
		FINGER_TIP,
		BATTLE_SPREAD,
		ROUTE,
		WALL,
		LADDER,
		BOX,
		FLUID,
		TRAIL;
	}
	
	public enum MODEL { 
		RAFALE,
		F16,
		TYPHOON,
		JAS39,
		MIG29,
		F14,
		SU27,
		J15,   // China
		MIG35,
		TEJAS; // India
	}
	
	public enum ACTION { 
		YOYO,
		IMMELMANN,
		BARREL,
		CISORS
	}
	
	// static
	private FORMATION formation;
	private MODEL model;
	
	// dynamic
	private ACTION action;
	
	public MilitaryFlightPlan(){
		formation = generateFormation();
		model = generateModel();
		action = ACTION.YOYO;
	}

	private MODEL generateModel() {
		switch (DemoHelper.getNextRandomInt(9)){
			case 0: return MODEL.RAFALE;
			case 1: return MODEL.F16;
			case 2: return MODEL.TYPHOON;
			case 3: return MODEL.JAS39;
			case 4: return MODEL.MIG29;
			case 5: return MODEL.F14;
			case 6: return MODEL.SU27;
			case 7: return MODEL.J15;
			case 8: return MODEL.MIG35;
			case 9: return MODEL.TEJAS;
			default : return MODEL.RAFALE;
		}
	}
	
	private FORMATION generateFormation() {
		switch (DemoHelper.getNextRandomInt(2)){
			case 0: return FORMATION.ECHELON;
			case 1: return FORMATION.ROUTE;
			case 2: return FORMATION.TRAIL;
			default : return FORMATION.ECHELON;
		}
	}
	
	private ACTION generateAction() {
		switch (DemoHelper.getNextRandomInt(3)){
			case 0: return ACTION.YOYO;
			case 1: return ACTION.BARREL;
			case 2: return ACTION.CISORS;
			case 3: return ACTION.IMMELMANN;
			default : return ACTION.IMMELMANN;
		}
	}

	public FORMATION getFormation() {
		return formation;
	}

	public MODEL getModel() {
		return model;
	}

	public ACTION getAction() {
		return action;
	}
	
	public void updateAction() {
		action = generateAction();
	}
}
