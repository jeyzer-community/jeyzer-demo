package org.jeyzer.demo.dup2;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyDemo2 {
	
	public static final Logger logger = LoggerFactory.getLogger(DummyDemo2.class);	
	
	public void load(){
		logger.debug("Dummy demo 2 loaded.");
	}
}
