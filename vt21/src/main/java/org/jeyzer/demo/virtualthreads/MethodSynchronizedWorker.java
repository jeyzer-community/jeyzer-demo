package org.jeyzer.demo.virtualthreads;

/*-
 * ---------------------------LICENSE_START---------------------------
 * Jeyzer Demo Virtual Threads
 * --
 * Copyright (C) 2020 - 2023 Jeyzer
 * --
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 * ----------------------------LICENSE_END----------------------------
 */
import java.time.Duration;

public class MethodSynchronizedWorker {
	
	public synchronized void uniqueAccess() {
        try {
            Thread.sleep(Duration.ofSeconds(10L));
        } catch (InterruptedException e) {
        	System.err.print("Interrupted");
        }
	}
	
}
