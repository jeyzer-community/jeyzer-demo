package org.jeyzer.demo.philosopher2;

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



import java.util.concurrent.CountDownLatch;

import org.jeyzer.demo.philosopher2.bowl.Bowl_1;
import org.jeyzer.demo.philosopher2.bowl.Bowl_2;
import org.jeyzer.demo.philosopher2.bowl.Bowl_3;
import org.jeyzer.demo.philosopher2.bowl.Bowl_4;
import org.jeyzer.demo.philosopher2.bowl.Bowl_5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhilosophersDemo {
	
	private static final Logger logger = LoggerFactory.getLogger(PhilosophersDemo.class);
	
	public static final String TYPE_PARAM = "type=";

	public static final String LINE_SEPARATOR = "================================";
	
	public void demo(String choice){
		Thread[] threads = new Thread[5];
		CountDownLatch tableOpen = new CountDownLatch(6);
		Bowl[] bowls = new Bowl[5];
		
		bowls[0] = new Bowl_1();
		bowls[1] = new Bowl_2();
		bowls[2] = new Bowl_3();
		bowls[3] = new Bowl_4();
		bowls[4] = new Bowl_5();
		
		for (int i=0; i<5; i++){
			Philosopher philosopher;
			if (i < 4)
				philosopher = new Philosopher(i+1, tableOpen, bowls[i], bowls[i+1], choice);
			else
				philosopher = new Philosopher(i+1, tableOpen, bowls[i], bowls[0], choice); //last one
			Thread t = new Thread(philosopher, philosopher.getName());
			threads[i] = t;
			t.start();
		}
		
		log("Table is set. Wait before waking up the philosophers.");
		
		setupTable();
		
		log("Notify the philosophers.");
		
		tableOpen.countDown();	
		
		log("Philosophers access the table now !.");
		
		waitForTermination(threads);
		
		epilogue();
	}
	
	private void setupTable(){
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.error("Demo interrupted", e);
			Thread.currentThread().interrupt();
		}
	}
	
	private void waitForTermination(Thread[] threads){
		for (int i=0; i<5; i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				logger.error("Demo interrupted", e);
				Thread.currentThread().interrupt();
			}
		}
	}
	
	private void epilogue(){
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			logger.error("Demo interrupted", e);
			Thread.currentThread().interrupt();
		}	
	}

	public static void main(String[] args) {
		PhilosophersDemo table = new PhilosophersDemo();
		
		String arg = args[0].substring(TYPE_PARAM.length());
		logger.info("");
		logger.info("Jeyzer demo : Philosophers");
		logger.info("");
		if (Philosopher.CHOICE_DEMO.equals(arg)){
			logger.info(LINE_SEPARATOR);
			logger.info("1/4 Solution scenario");
			logger.info(LINE_SEPARATOR);
			table.demo(Philosopher.CHOICE_SOLUTION);
			logger.info(LINE_SEPARATOR);
			logger.info("2/4 Random scenario");
			logger.info(LINE_SEPARATOR);
			table.demo(Philosopher.CHOICE_RANDOM);
			logger.info(LINE_SEPARATOR);
			logger.info("3/4 Random scenario");
			logger.info(LINE_SEPARATOR);
			table.demo(Philosopher.CHOICE_RANDOM);
			logger.info(LINE_SEPARATOR);
			logger.info("4/4 Lock scenario");
			logger.info(LINE_SEPARATOR);
			table.demo(Philosopher.CHOICE_LOCK);
		}
		else{
			logger.info(LINE_SEPARATOR);
			logger.info("Scenario :" + arg);
			logger.info(LINE_SEPARATOR);
			table.demo(arg);
		}
		
	}	
	
	private void log(String msg) {
		logger.info("Table says : " + msg);
	}
	
}
