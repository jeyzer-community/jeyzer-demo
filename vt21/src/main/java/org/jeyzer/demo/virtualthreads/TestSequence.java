package org.jeyzer.demo.virtualthreads;

/*-
 * ---------------------------LICENSE_START---------------------------
 * Jeyzer Demo 21 Virtual Threads
 * --
 * Copyright (C) 2020 - 2023 Jeyzer
 * --
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 * ----------------------------LICENSE_END----------------------------
 */

import java.time.Duration;

public abstract class TestSequence {
	
	public void startSequence() {
		introduction();
		executeSequence();
	}

	public abstract void introduction();
	
	public abstract void executeSequence();
	
    protected void sleep(long time) {
        try {
            Thread.sleep(Duration.ofSeconds(time));
        } catch (InterruptedException e) {
        	System.err.print("Interrupted");
        }
    }
}
