package org.jeyzer.demo.event.codes;

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


import org.jeyzer.mx.event.JzrEventCode;
import org.jeyzer.mx.event.JzrEventLevel;
import org.jeyzer.mx.event.JzrEventSubLevel;

//-----------------------------------------------------------------------------------
//This code list is documented with Javadoc 
//Code documentation should be in theory shared with any customer support service
//-----------------------------------------------------------------------------------

/**
* <p>FeatureEventCode defines all the feature applicative event codes.</p> 
*/
public enum FeatureEventCode implements JzrEventCode {
	
	/**
	 * CPU consuming task started.
	 */
	JZR_FEA_01(
			"CPU consuming task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),

	/**
	 * Dead lock task started.
	 */
	JZR_FEA_02(
			"Dead lock task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),	
	
	/**
	 * Disfunctional code sequence task started.
	 */
	JZR_FEA_03(
			"Disfunctional code sequence task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Frozen code task started.
	 */
	JZR_FEA_04(
			"Frozen code task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),

	/**
	 * Hiatus start task started.
	 */
	JZR_FEA_05(
			"Hiatus start task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Hiatus end task started.
	 */
	JZR_FEA_06(
			"Hiatus end task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Locked threads task started.
	 */
	JZR_FEA_07(
			"Locked threads task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Memory consuming task started.
	 */
	JZR_FEA_08(
			"Memory consuming task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),

	/**
	 * Memory exhausted task started.
	 */
	JZR_FEA_09(
			"Memory exhausted task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Ping pong task started.
	 */
	JZR_FEA_10(
			"Ping pong task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Volley ball player task started.
	 */
	JZR_FEA_11(
			"Volley ball player task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Tennis player task started.
	 */
	JZR_FEA_12(
			"Tennis player task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Base ball player task started.
	 */
	JZR_FEA_13(
			"Base ball player task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Croquet player task started.
	 */
	JZR_FEA_14(
			"Croquet player task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Golf player task started.
	 */
	JZR_FEA_15(
			"Golf player task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Rugby player task started.
	 */
	JZR_FEA_16(
			"Rugby player task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Soccer player task started.
	 */
	JZR_FEA_17(
			"Soccer player task started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			)
	;
	
	public static final String SERVICE = "Features";
	
	private String type; // optional
	private String abbreviation;
	private String name;
	private String description; // optional
	private JzrEventLevel level;
	private JzrEventSubLevel subLevel;

	private FeatureEventCode(String name, String description, JzrEventLevel level, JzrEventSubLevel subLevel) {
		this.name = name;
		this.description = description;
		this.level = level;
		this.subLevel = subLevel;
		this.abbreviation = this.name();
		this.type = null;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getAbbreviation() {
		return abbreviation;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public JzrEventLevel getLevel() {
		return level;
	}

	@Override
	public JzrEventSubLevel getSubLevel() {
		return subLevel;
	}
}
