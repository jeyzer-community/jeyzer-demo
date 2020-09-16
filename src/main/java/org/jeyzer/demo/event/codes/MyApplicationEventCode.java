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

// -----------------------------------------------------------------------------------
// This code list is documented with Javadoc 
//  Code documentation should be in theory shared with any customer support service
//------------------------------------------------------------------------------------

/**
 * <p>MyApplicationEventCode defines all the high level applicative event codes</p> 
 */
public enum MyApplicationEventCode implements JzrEventCode{
	
	/**
	 * Service event code.
	 */
	MCY_MYA_01(
			"Service available",
			"Service is available for requests",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Service shutdown event code.
	 */
	MCY_MYA_02(
			"Service manual shutdown",
			"Service shutdown requested",
			JzrEventLevel.WARNING,
			JzrEventSubLevel.MEDIUM
			),

	/**
	 * Activity event code.
	 */
	MCY_MYA_10(
			"Activity started",
			"Daily update activity started",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Activity cancel event code.
	 */
	MCY_MYA_11(
			"Activity cancelled",
			"Daily update activity cancelled by the end user",
			JzrEventLevel.WARNING,
			JzrEventSubLevel.MEDIUM
			),
	
	/**
	 * Activity critical error event code.
	 */
	MCY_MYA_12(
			"Activity critical error",
			"Daily update activity failed unexpectedly",
			JzrEventLevel.CRITICAL,
			JzrEventSubLevel.HIGH
			),
	
	/**
	 * Connectivity lost event code.
	 */
	MCY_MYA_20(
			"Remote service connection lost",
			"Remote service connection is lost",
			JzrEventLevel.CRITICAL,
			JzrEventSubLevel.MEDIUM
			),
	
	/**
	 * Connectivity recovery event code.
	 */
	MCY_MYA_21(
			"Remote service connection recovered",
			"Remote service connection is recovered",
			JzrEventLevel.WARNING,
			JzrEventSubLevel.MEDIUM
			);
	
	public static final String SERVICE = "My Application";
	
	private String type; // optional
	private String abbreviation;
	private String name;
	private String description; // optional
	private JzrEventLevel level;
	private JzrEventSubLevel subLevel;

	private MyApplicationEventCode(String name, String description, JzrEventLevel level, JzrEventSubLevel subLevel) {
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
