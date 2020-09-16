package org.jeyzer.demo.shared;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jeyzer.annotations.Function;

public class DemoSharedLoader {
	
	public static final Logger logger = LoggerFactory.getLogger(DemoSharedLoader.class);	
	
	// The shared profile must be loaded automatically at analysis time : 
	//   below marker function must be therefore listed in any JZR demo report (Profiles sheet)
	@Function
	public void loadSharedDemoLibrary(){
		logger.info("Shared demo library loaded.");
	}
}
