package org.jeyzer.demo.features.tasks;

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


import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.ArrayList;
import java.util.List;

import org.jeyzer.annotations.Function;
import org.jeyzer.annotations.Operation;
import org.jeyzer.demo.event.codes.FeatureEventCode;
import org.jeyzer.demo.features.Feature;
import org.jeyzer.publish.event.JzrStandardEvent;

public class MemoryExhaustingTask extends Feature {
	
	private static final String FEATURE_NAME = "Memory exhausting";
	
	private List<Long> elements = new ArrayList<>();
	
	@Override
	public void show() throws InterruptedException {
		publishThreadApplicativeEvent(
				new JzrStandardEvent(FeatureEventCode.JZR_FEA_09));
		exhaustMemory();
	}
	
	@Override
	public String getName() {
		return FEATURE_NAME;
	}
	
	@Function(name="Memory exhausting section : please look at GC, CPU and Memory panels for more details")
	@Operation(name="Memory creation : fill list with random long numbers")
	protected void exhaustMemory() throws InterruptedException{
		log("MemoryExhaustingTask", "starts");
		
		int mb = 1024*1024;
		List<MemoryPoolMXBean> memPoolBeans = ManagementFactory.getMemoryPoolMXBeans();
		MemoryPoolMXBean memPoolBean = null;
		for (MemoryPoolMXBean bean : memPoolBeans){
			if ("PS Old Gen".equals(bean.getName()) || "G1 Old Gen".equals(bean.getName())){
				memPoolBean = bean;
				break;
			}
		}
		if (memPoolBean == null){
			log("MemoryExhaustingTask", "old gen pool unknown. Cannot proceed with Memory exhausting test.");
			log("MemoryExhaustingTask", "ends");
			return;
		}
		
		// let's avoid the out of memory
		int count = 0;
		while(true){
			
			for (long i=1; i<125000; i++){
				elements.add(i);
			}
			
			float oldPoolUsedPercent = (float)memPoolBean.getCollectionUsage().getUsed()/memPoolBean.getCollectionUsage().getMax();
			log("MemoryExhaustingTask", memPoolBean.getName() + " used : " + memPoolBean.getCollectionUsage().getUsed()/mb + " Mb");
			log("MemoryExhaustingTask", memPoolBean.getName() + " max : " +memPoolBean.getCollectionUsage().getMax()/mb + " Mb");
			log("MemoryExhaustingTask", memPoolBean.getName() + " used % : " + oldPoolUsedPercent);
			
			
			// System.gc() here gives CPU 100%, move young to old
			
			// let's surf on the max memory edge
			if (oldPoolUsedPercent > 0.90){
				log("MemoryExhaustingTask", "Surfing the max memory edge..");
				while(true){
					log("MemoryExhaustingTask", "Iteration : " + count);
					
					for (long i=1; i<1000; i++){ //remove some, very very slow..
						elements.remove(i);
					}
					for (long i=1; i<1000; i++){ //add some
						elements.add(i);
					}

					log("MemoryExhaustingTask", memPoolBean.getName() + " used : " + memPoolBean.getCollectionUsage().getUsed()/mb + " Mb");
					log("MemoryExhaustingTask", memPoolBean.getName() + " max : " +memPoolBean.getCollectionUsage().getMax()/mb + " Mb");
					oldPoolUsedPercent = (float)memPoolBean.getCollectionUsage().getUsed()/memPoolBean.getCollectionUsage().getMax();
					log("MemoryExhaustingTask", memPoolBean.getName() + " used % : " + oldPoolUsedPercent);
					
					hold();
					count++;
					
					if (count == 30){
						log("MemoryExhaustingTask", "Ends exhaustMemory");
						this.elements.clear();
						return;
					}
				}
			}
		}
	}
	
}
