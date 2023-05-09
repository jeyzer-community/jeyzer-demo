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

import org.jeyzer.demo.shared.DemoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrafficGenerator implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(TrafficGenerator.class);

	private static int nextCarId = 0;
	
	private static synchronized int getNextCarId(){
		synchronized(TrafficGenerator.class){
			return nextCarId++;
		}
	}
	
	private ExecutorService toll;
	
	public TrafficGenerator(ExecutorService toll) {
		this.toll = toll;
	}

	@Override
	public void run() {
		generateTraffic();
	}

	private void generateTraffic() {
		int carCount = DemoHelper.getNextRandomInt(8);
		
		// Rush hour ?
		int rushHour = DemoHelper.getNextRandomInt(10);
		if (rushHour > 8){
			// 1 out of 10  (10 is exclusive)
			log(">>>>> RUSH HOUR !!");
			carCount = carCount + 15 + DemoHelper.getNextRandomInt(15);
		}
		
		log(">>>>> Incoming cars : " + carCount);
		
		for (int i=0; i<carCount; i++){
			int id = getNextCarId();
			CarDriver driver = new CarDriver(toll, id);
			Thread t = new Thread(driver);
			t.setName("Car #" + id);	
			t.start();
		}
	}

	protected void log(String msg) {
		logger.info("Traffic generator : " + msg);
	}

}
