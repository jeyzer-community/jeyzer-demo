package org.jeyzer.demo.labors.job.system;

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


import org.jeyzer.demo.labors.job.Job;

public class SystemJob extends Job {
	
	private SystemJobDefinition def;
	
	public SystemJob(SystemJobDefinition def) {
		this.def = def;
	}

	@Override
	public void init() {
		System.setProperty("demo.job." + def.getStickerName(), Boolean.TRUE.toString());
	}
	
	@Override
	public void work() {
		// do nothing
	}

	@Override
	public String getEventName() {
		return def.getEventName();
	}
	
	public boolean isFullSessionScope() {
		return this.def.isFullSessionScope();
	}
}
