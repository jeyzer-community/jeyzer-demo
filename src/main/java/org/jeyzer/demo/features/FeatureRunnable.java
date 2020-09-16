package org.jeyzer.demo.features;

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



import java.util.ArrayList;
import java.util.List;

import org.jeyzer.annotations.Exclude;
import org.jeyzer.annotations.Executor;
import org.jeyzer.demo.features.mx.generic.feature.FeaturesPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeatureRunnable implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(FeatureRunnable.class);

	private List<Feature> features = new ArrayList<>();
	
	private Object semaphore;
	private int count = 0;
	
	public FeatureRunnable(final List<Feature> features, int offset, Object semaphore){
		this.features = features;
		this.count = this.count + offset;
		this.semaphore = semaphore;
	}
	
	@Override
	@Executor(name="Demo thread")
	public void run() {
		
		if(this.count == 1)
			waitForTermination();

		while (true){
			
			if (count >= features.size()){
				synchronized(this.semaphore){
					this.semaphore.notifyAll();
				}
				break;
			}
			Feature feature = features.get(count);
			
			FeaturesPublisher.instance().registerActiveFeature(feature.getName());
			
			try {
				feature.show();
			} catch (InterruptedException e) {
				logger.error("Demo interrupted", e);
				Thread.currentThread().interrupt();
			}
			this.count += 2;
			
			FeaturesPublisher.instance().unregisterActiveFeature(feature.getName());
			
			waitForTermination();
		}
	}
	
	@Exclude(name="Inactive demo thread")
	private void waitForTermination(){
		
		synchronized(this.semaphore){
			this.semaphore.notifyAll();
			try {
				this.semaphore.wait();
			} catch (InterruptedException e) {
				logger.error("Demo interrupted", e);
				Thread.currentThread().interrupt();
			}
		}	
	}

}
