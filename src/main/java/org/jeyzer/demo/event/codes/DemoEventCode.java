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
 * <p>DemoEventCode defines all the high level demo applicative event codes.</p> 
 */
public enum DemoEventCode implements JzrEventCode {
	
	/**
	 * Features demo started.
	 */
	JZR_DEM_01(
			"Features demo started",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.HIGH
			),

	/**
	 * Features demo stopped.
	 */
	JZR_DEM_02(
			"Features demo stopped",
			"description",
			JzrEventLevel.INFO,
			JzrEventSubLevel.HIGH
			),
	
	/**
	 * Features demo got interrupted.
	 */
	JZR_DEM_03(
			"Features demo got interrupted",
			"description",
			JzrEventLevel.WARNING,
			JzrEventSubLevel.MEDIUM
			),
	
	/**
	 * Event which should not be published because info service got suspended. If you find it, it means that service suspension is broken. Please report the issue.
	 */
	JZR_DEM_04(
			"Event which should not be published because info service got suspended",
			"If you find it, it means that service suspension is broken. Please report the issue.",
			JzrEventLevel.INFO,
			JzrEventSubLevel.MEDIUM
			),
	
	/**
	 * System country locale is not expected one. Process started with the wrong user.country locale. Please report the issue.
	 */
	JZR_DEM_05(
			"System country locale is not expected one",
			"Process started with the wrong user.country locale. Please report the issue.",
			JzrEventLevel.WARNING,
			JzrEventSubLevel.MEDIUM
			),
	
	/**
	 * Event produced massively to test the event limit. This event is cancelled afterwards. It could be normal that it appears in the JZR report in case the Jeyer Recorder collects it before removal.
	 */
	JZR_DEM_06(
			"Event produced massively to test the event limit.",
			"This event is cancelled afterwards. It could be normal that it appears in the JZR report in case the Jeyer Recorder collects it before removal.",
			JzrEventLevel.INFO,
			JzrEventSubLevel.VERY_LOW
			),
	
	/**
	 * Duplicate event produced massively to test the event flooding protection. All events should be ignored except the first one.
	 */
	JZR_DEM_07(
			"Duplicate event produced massively to test the event flooding protection.",
			"All events should be ignored except the first one.",
			JzrEventLevel.WARNING,
			JzrEventSubLevel.VERY_LOW
			),
	;

	public static final String SERVICE = "Demo";
	
	private String type; // optional
	private String abbreviation;
	private String name;
	private String description; // optional
	private JzrEventLevel level;
	private JzrEventSubLevel subLevel;

	private DemoEventCode(String name, String description, JzrEventLevel level, JzrEventSubLevel subLevel) {
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
