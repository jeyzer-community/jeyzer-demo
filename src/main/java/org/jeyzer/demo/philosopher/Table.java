package org.jeyzer.demo.philosopher;

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



import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.jeyzer.demo.philosopher.bowl.Bowl_1;
import org.jeyzer.demo.philosopher.bowl.Bowl_2;
import org.jeyzer.demo.philosopher.bowl.Bowl_3;
import org.jeyzer.demo.philosopher.bowl.Bowl_4;
import org.jeyzer.demo.philosopher.bowl.Bowl_5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Table {
	
	private static final Logger logger = LoggerFactory.getLogger(Table.class);
	
	public static final String ORDERED_PARAM = "ordered=";

	public void demo(boolean ordered) throws InterruptedException{
		Thread[] threads = new Thread[5];
		CountDownLatch tableOpen = new CountDownLatch(6);
		Semaphore fork = new Semaphore(1);
		Bowl[] bowls = new Bowl[5];
		
		bowls[0] = new Bowl_1();
		bowls[1] = new Bowl_2();
		bowls[2] = new Bowl_3();
		bowls[3] = new Bowl_4();
		bowls[4] = new Bowl_5();
		
		displayTitle();
		
		for (int i=0; i<5; i++){
			Philosopher philosopher;
			if (i < 4)
				philosopher = new Philosopher(i+1, fork, tableOpen, bowls[i], bowls[i+1], ordered);
			else
				philosopher = new Philosopher(i+1, fork, tableOpen, bowls[i], bowls[0], ordered); //last one
			Thread t = new Thread(philosopher, philosopher.getName());
			threads[i] = t;
			t.start();
		}
		
		log("Table is set. Wait before waking up the philosophers.");
		
		setupTable();
		
		log("Notify the philosophers.");
		
		tableOpen.countDown();	
		
		log("Philosophers access the table now !.");
		
		waitForTermination();
	}
	
	private void setupTable() throws InterruptedException{
		Thread.sleep(5000);
	}
	
	private void waitForTermination() throws InterruptedException{
		Thread.sleep(60000);
	}

	public static void main(String[] args) {
		Table table = new Table();
		
		String arg = args[0].substring(ORDERED_PARAM.length());
		
		boolean ordered = Boolean.parseBoolean(arg);
		
		try {
			table.demo(ordered);
		} catch (InterruptedException ex) {
			logger.error("Demo interrupted", ex);
			Thread.currentThread().interrupt();
		}
		
	}	
	
	private void log(String msg) {
		logger.info("Table says : " + msg);
	}
	
	private void displayTitle() {
		logger.info("");
		logger.info("============================================================================================================================");
		logger.info("");
		logger.info("        |                                  |    o|                        |                           |                     ");               
		logger.info("        |,---.,   .,---,,---.,---.    ,---.|---..|    ,---.,---.,---.,---.|---.,---.,---.,---.    ,---|,---.,-.-.,---.      ");
		logger.info("        ||---'|   | .-' |---'|        |   ||   |||    |   |`---.|   ||   ||   ||---'|    `---.    |   ||---'| | ||   |      ");
		logger.info("    `---'`---'`---|'---'`---'`        |---'`   '``---'`---'`---'`---'|---'`   '`---'`    `---'    `---'`---'` ' '`---'      ");
		logger.info("              `---'                   |                              |                                                      ");
		logger.info("============================================================================================================================");
	}
}
