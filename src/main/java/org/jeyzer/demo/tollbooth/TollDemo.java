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



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TollDemo {
	
	private static final Logger logger = LoggerFactory.getLogger(TollDemo.class);

	private static int nextTollId = 0;
	
	private static synchronized int getNextTollId(){
		synchronized(TollDemo.class){
			return nextTollId++;
		}
	}
	
	 public class TollThreadFactory implements ThreadFactory {
	  @Override
	  public Thread newThread(Runnable r) {
		 Thread t = new Thread(r);
		 t.setName("Toll #" + getNextTollId());
	     return t;
	   }
	  }
	 
	 public class TollSupervisorThreadFactory implements ThreadFactory {
		  @Override
		  public Thread newThread(Runnable r) {
			 Thread t = new Thread(r);
			 t.setName("Toll supervisor");
		     return t;
		   }
		  }
	 
	 public class TrafficThreadFactory implements ThreadFactory {
		  public Thread newThread(Runnable r) {
			 Thread t = new Thread(r);
			 t.setName("Traffic generator");
		     return t;
		   }
		  }
	
	public static void main(String[] args) {		
		TollDemo simulator = new TollDemo();
		simulator.startSimulation();
	}

	private void startSimulation() {

		displayTitle();
		
		// toll queue
		TransferQueue<Runnable> carQueue = new LinkedTransferQueue<>();
		
		// toll
		ExecutorService toll = new ThreadPoolExecutor(6,10,10,TimeUnit.SECONDS, 
				carQueue, new TollThreadFactory());

		// toll supervisor
		ScheduledExecutorService supervision = Executors.newScheduledThreadPool(1, new TollSupervisorThreadFactory());		
		supervision.scheduleWithFixedDelay(new TollSupervisor((ThreadPoolExecutor)toll), 60, 60, TimeUnit.SECONDS);
		
		// traffic generation
		ScheduledExecutorService traffic = Executors.newScheduledThreadPool(1, new TrafficThreadFactory());		
		traffic.scheduleWithFixedDelay(new TrafficGenerator(toll), 10, 10, TimeUnit.SECONDS);

	}
	
	private void displayTitle() {
		logger.info("");
		logger.info("==========================================================================================================");
		logger.info("");
		logger.info("              |                             |         |    |            |                ");
		logger.info("              |,---.,   .,---,,---.,---.    |--- ,---.|    |        ,---|,---.,-.-.,---. ");
		logger.info("              ||---'|   | .-' |---'|        |    |   ||    |        |   ||---'| | ||   | ");
		logger.info("          `---'`---'`---|'---'`---'`        `---'`---'`---'`---'    `---'`---'` ' '`---' ");
		logger.info("                    `---'                                                                ");
		logger.info("==========================================================================================================");
	}
}
