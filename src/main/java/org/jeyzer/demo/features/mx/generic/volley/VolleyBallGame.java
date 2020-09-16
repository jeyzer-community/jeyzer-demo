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

import org.jeyzer.demo.shared.DemoHelper;

public class VolleyBallGame {
	
	public enum FORMATION_TYPE { 
		FORMATION_4_2, 
		FORMATION_6_2, 
		FORMATION_5_1;
	
		@Override
		public String toString(){
			switch(this) {
			case FORMATION_4_2: return "4-2";
			case FORMATION_6_2: return "6-2";
			case FORMATION_5_1: return "5-1";
			default: throw new IllegalArgumentException();
			}
		}
	}
	
	public enum VARIANT { 
		NORMAL,
		BEACHBALL,
		BIRIBALl,
		ECUAVOLLEY,
		FootVolley,
		HooverBall,
		NewCombBall,
		Sepak,
		ThrowBall,
		WallyBall;
	}
	
	public enum ACTION { 
		Serve,
		Pass,
		Set,
		Attack
	}
	
	// static
	private FORMATION_TYPE type;
	private VARIANT variant;
	
	// dynamic
	private ACTION action;
	
	public VolleyBallGame(){
		type = generateFormation();
		variant = generateVariant();
		action = ACTION.Serve;
	}

	private VARIANT generateVariant() {
		switch (DemoHelper.getNextRandomInt(9)){
			case 0: return VARIANT.NORMAL;
			case 1: return VARIANT.BEACHBALL;
			case 2: return VARIANT.BIRIBALl;
			case 3: return VARIANT.ECUAVOLLEY;
			case 4: return VARIANT.FootVolley;
			case 5: return VARIANT.HooverBall;
			case 6: return VARIANT.NewCombBall;
			case 7: return VARIANT.Sepak;
			case 8: return VARIANT.ThrowBall;
			case 9: return VARIANT.WallyBall;
			default : return VARIANT.NORMAL;
		}
	}
	
	private FORMATION_TYPE generateFormation() {
		switch (DemoHelper.getNextRandomInt(2)){
			case 0: return FORMATION_TYPE.FORMATION_4_2;
			case 1: return FORMATION_TYPE.FORMATION_6_2;
			case 2: return FORMATION_TYPE.FORMATION_5_1;
			default : return FORMATION_TYPE.FORMATION_4_2;
		}
	}
	
	private ACTION generateAction() {
		switch (DemoHelper.getNextRandomInt(3)){
			case 0: return ACTION.Serve;
			case 1: return ACTION.Set;
			case 2: return ACTION.Attack;
			case 3: return ACTION.Pass;
			default : return ACTION.Pass;
		}
	}

	public FORMATION_TYPE getType() {
		return type;
	}

	public VARIANT getVariant() {
		return variant;
	}

	public ACTION getAction() {
		return action;
	}
	
	public void updateAction() {
		action = generateAction();
	}
}
