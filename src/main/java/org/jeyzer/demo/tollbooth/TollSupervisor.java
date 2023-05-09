package org.jeyzer.demo.tollbooth;

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



import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TollSupervisor implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(TollSupervisor.class);
	
	private ThreadPoolExecutor toll;
	int adjust = 0;
	boolean increase = false;

	public TollSupervisor(ThreadPoolExecutor toll2){
		this.toll= toll2;
	}
	
	@Override
	public void run() {
		regulate();
	}
	
	private void regulate() {
		int queueSize = toll.getQueue().size();
		int workerSize = toll.getCorePoolSize();
		
		int diff = queueSize - workerSize;
		
		log("Supervision status : ");
		log("     Toll worker size   : " + workerSize);
		log("     Toll queue size    : " + queueSize);
		log("     Worker adjustment  : " + adjust);
		
		if(diff > 10){
			int count = diff / 5;
			if (increase)
				adjust++; // increase the adjustment to incoming flow
			count += adjust;
			log("     Adding  : " + count + " workers");
			for (int j=0; j< count; j++)
				addOneTollWorker();
			increase = true;
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.error("Demo interrupted", e);
				Thread.currentThread().interrupt();
			}
		}
		else if(diff < - 5){ 
			int count = (-diff) / 5;
			for (int j=0; j< count; j++)
				removeOneTollWorker();
			increase = false;
			if (adjust > 0)
				adjust = 0; // reset ajustment
			log("     Removing : " + count + " workers");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.error("Demo interrupted", e);
				Thread.currentThread().interrupt();
			}
		}
		else{
			increase = false;
		}

	}

	private void removeOneTollWorker() {
		log("Remove one toll worker ----------------------------");
		toll.setCorePoolSize(toll.getCorePoolSize() - 1);
	}

	private void addOneTollWorker() {
		log("Add one tollworker +++++++++++++++++++++++++++++");
		toll.setCorePoolSize(toll.getCorePoolSize() + 1);

	}

	protected void log(String msg) {
		logger.info("Toll supervisor : " + msg);
	}

}
