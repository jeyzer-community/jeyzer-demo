package org.jeyzer.demo.labors.job.executable;

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


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class JobThreadFactory  implements ThreadFactory {	
	private String name;
	private AtomicInteger count;
	
	public JobThreadFactory(String name, AtomicInteger count) {
		this.name = name;
		this.count = count;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		t.setName("Job " + name + " #" + count.getAndIncrement());
		return t;
	}
}
