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



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TollWorker implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(TollWorker.class);

	private CarDriver driver;
	
	private List<Long> elements = new ArrayList<>();

	public TollWorker(CarDriver driver){
		this.driver = driver;
	}

	@Override
	public void run() {
		try {
			processTicket();
			
			waitsForMoney();
			
			storesMoney();
			
			waitsForDriverToLeave();
			
			elements.clear();
			
			if (getRandom(0,10) > 6)
				pause();
			
			log("gate closed. process next driver...");
			
		} catch (InterruptedException e) {
			logger.error("Demo interrupted", e);
			Thread.currentThread().interrupt();
		}
	}

	private void processTicket() throws InterruptedException {
		log("processes ticket");
		int count = getRandom(3,5);
		consumeMemory(count);
		Thread.sleep(count*1000L);
	}
	
	
	private void waitsForMoney() throws InterruptedException {
		log("announces the price and waits for the car driver to pay");
		driver.askMoney();
		synchronized(this){
			wait();
		}
	}
	
	private void storesMoney() throws InterruptedException {
		log("stores money");
		int count = getRandom(1,10);
		consumeMemory(count);
		Thread.sleep(count*1000L);
	}
	
	private void waitsForDriverToLeave() throws InterruptedException {
		log("opens the gate and waits for the car driver to go away");
		driver.confirmsPayment();
		synchronized(this){
			wait();
		}
	}
	
	private void pause() throws InterruptedException {
		int pause = getRandom(30,60);
		log("takes a coffee break for " + pause + " seconds");
		Thread.sleep(pause*1000L);
	}

	protected void log(String msg) {
		logger.info(Thread.currentThread().getName() + " worker processing " + driver + " : " + msg);
	}
	
	private int getRandom(int min, int max) {
		ThreadLocalRandom  random = ThreadLocalRandom.current();
		return min + random.nextInt(max);
	}

	public void receivesMoney() {
		synchronized(this){
			notifyAll();
		}
	}

	public void saysGoodbye() {
		synchronized(this){
			notifyAll();
		}
	}
	
	private void consumeMemory(int count){
		int size = 10000*count;
		for (long i=1; i<size; i++){ 
			elements.add(i);
		}
	}
}
