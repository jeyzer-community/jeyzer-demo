package org.jeyzer.demo.tollbooth;

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



import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;

import org.jeyzer.demo.shared.DemoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CarDriver implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(CarDriver.class);
	
	private int carId;

	private ExecutorService toll;
	
	private TollWorker worker;
	
	private double result;
	
	public CarDriver(ExecutorService toll, int carId) {
		this.carId = carId;
		this.toll = toll;
	}
	
	public  double getResult(){
		return result;
	}
	
	@Override
	public String toString(){
		return "Car driver #" + this.carId;
	}

	@Override
	public void run() {
		
		try {
			// arrives at the toll
			arrivesAtTheToll();
			
			// toll accepts the driver, wait for ticket processing
			giveTicketAndWait();
			
			// sleep for 5-10sec
			prepareMoney();
			
			// toll gets the money, wait for money processing
			giveMoneyAndWait();
			
			// sleep for 5-10sec
			startsCar();
			
			log("says goodbye !!");
			
		} catch (InterruptedException e) {
			logger.error("Demo interrupted", e);
			Thread.currentThread().interrupt();
		}
		
	}

	private void arrivesAtTheToll() {
		log("arrives at the toll");
		worker = new TollWorker(this);
		toll.execute(worker);
	}
	
	
	private void giveTicketAndWait() throws InterruptedException {
		log("waits in the car queue");
		synchronized(this){
			wait();
		}
	}
	
	private void prepareMoney() {
		log("prepares his money");
		int seconds = getRandom(5,6); // takes between 5 and 10 sec to pay
		consumeCPU(seconds);
		
		this.worker.receivesMoney();
	}
	
	private void giveMoneyAndWait() throws InterruptedException {
		log("gives his money and waits");
		synchronized(this){
			wait();
		}
	}
	
	private void startsCar() {
		log("starts the car");
		int seconds = getRandom(5,6); // takes between 5 and 10 sec to start the car
		
		consumeCPU(seconds);
		
		this.worker.saysGoodbye();
	}

	private int getRandom(int min, int max) {
		ThreadLocalRandom  random = ThreadLocalRandom.current();
		return min + random.nextInt(max);
	}

	protected void log(String msg) {
		logger.info(this.toString() + " : " + msg);
	}

	public void askMoney() {
		synchronized(this){
			notifyAll();
		}
	}

	public void confirmsPayment() {
		synchronized(this){
			notifyAll();
		}
	}
	
	private void consumeCPU(int seconds) {
		int value1;
		int value2;

		long start = System.currentTimeMillis();
		int count = 0;
		
		while (true){
			value1 = DemoHelper.getNextRandomInt();
			value2 = DemoHelper.getNextRandomInt();
			result  = (double)value1/value2;
			count++;
			if (count == 50000){
				long current = System.currentTimeMillis();
				if ((current - start)> seconds*1000){ // 5 sec
					break;
				}
				count = 0;
			}
		}

	}

}
