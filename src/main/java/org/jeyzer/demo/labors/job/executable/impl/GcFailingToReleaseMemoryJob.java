package org.jeyzer.demo.labors.job.executable.impl;

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


import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.ArrayList;
import java.util.List;

import org.jeyzer.annotations.Executor;
import org.jeyzer.annotations.Function;
import org.jeyzer.demo.labors.job.executable.ExecutableJob;
import org.jeyzer.demo.labors.job.executable.ExecutableJobDefinition;

public class GcFailingToReleaseMemoryJob extends ExecutableJob {
	
	private List<Long> elements = new ArrayList<>();

	public GcFailingToReleaseMemoryJob(ExecutableJobDefinition def) {
		super(def);
	}
	
	@Override
	@Executor(name="Job executor")
	public void executeJob() {
		try {
			exhaustMemory();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Function(name="Memory exhausting")
	protected void exhaustMemory() throws InterruptedException{
		List<MemoryPoolMXBean> memPoolBeans = ManagementFactory.getMemoryPoolMXBeans();
		MemoryPoolMXBean memPoolBean = null;
		for (MemoryPoolMXBean bean : memPoolBeans){
			if ("PS Old Gen".equals(bean.getName()) || "G1 Old Gen".equals(bean.getName())){
				memPoolBean = bean;
				break;
			}
		}
		if (memPoolBean == null)
			return; // not supported
		
		while(true){
			// fill the memory progressively
			// Note that Xmx should be 756Mb and Xmn 100Mb. Without Xmn, we can get random out of memory errors on below add (jdk8). Quite surprising.
			for (long i=1; i<50000; i++)
				elements.add(i);
			
			float oldPoolUsedPercent = (float)memPoolBean.getCollectionUsage().getUsed()/memPoolBean.getCollectionUsage().getMax();
			
			if (oldPoolUsedPercent > 0.90) {
				surfMemoryEdge();
				return;
			}
		}
	}

	private void surfMemoryEdge() throws InterruptedException {
		while(true){
			for (long i=1; i<1000; i++){ //remove some entries, very very slow..
				elements.remove(i);
			}
			for (long i=1; i<1000; i++){ //add some entries
				elements.add(i);
			}
			
			oneTicHold();
			if (Thread.interrupted()) {
				return;
			}
		}
	}
}
