package org.jeyzer.demo.event.codes;

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


import org.jeyzer.mx.event.JzrEventCode;
import org.jeyzer.mx.event.JzrEventLevel;
import org.jeyzer.mx.event.JzrEventSubLevel;

// -----------------------------------------------------------------------------------
// This code list is documented with Javadoc 
//  Code documentation should be in theory shared with any customer support service
//------------------------------------------------------------------------------------

/**
 * <p>LaborEventCode defines all the high level labor applicative event codes.</p> 
 */
public enum LaborEventCode implements JzrEventCode{
	
	/**
	 * Labor system event code published.
	 */
	JZR_LAB_01(
			"Labor system event code published",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),

	/**
	 * Labor executable event code published.
	 */
	JZR_LAB_02(
			"Labor executable event published",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.LOW
			),
	
	/**
	 * Labor applicative task one shot event code published.
	 */
	JZR_LAB_03(
			"Applicative task one shot event",
			"description",
			JzrEventLevel.WARNING,
			JzrEventSubLevel.MEDIUM
			),
	
	/**
	 * Labor applicative task long event code published.
	 */
	JZR_LAB_04(
			"Applicative task long event",
			"description",
			JzrEventLevel.WARNING,
			JzrEventSubLevel.MEDIUM
			),
	
	/**
	 * Labor applicative session one shot event code published.
	 */
	JZR_LAB_05(
			"Applicative session one shot event",
			"description",
			JzrEventLevel.WARNING,
			JzrEventSubLevel.MEDIUM
			),
	
	/**
	 * Labor applicative session long event code published.
	 */
	JZR_LAB_06(
			"Applicative session long event",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.MEDIUM
			),
	
	/**
	 * Labor applicative session long event code published.
	 */
	JZR_LAB_07(
			"Applicative system event",
			"description",
			JzrEventLevel.WARNING,
			JzrEventSubLevel.MEDIUM
			);
	
	public static final String SERVICE = "Demo";
	
	private String type; // optional
	private String abbreviation;
	private String name;
	private String description; // optional
	private String ticket; // optional
	private JzrEventLevel level;
	private JzrEventSubLevel subLevel;

	private LaborEventCode(String name, String description, JzrEventLevel level, JzrEventSubLevel subLevel) {
		this.name = name;
		this.description = description;
		this.level = level;
		this.subLevel = subLevel;
		this.abbreviation = this.name();
		this.ticket = null;
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
	public String getTicket() {
		return ticket;
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
