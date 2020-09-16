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


import java.util.Random;

public final class DemoHelper {
	
	private DemoHelper() {}

	// Cached random
	private static final Random random = new Random();
	
	public static int getNextRandomInt(int limit) {
		return random.nextInt(limit);
	}
	
	public static int getNextRandomInt() {
		return random.nextInt();
	}
}
