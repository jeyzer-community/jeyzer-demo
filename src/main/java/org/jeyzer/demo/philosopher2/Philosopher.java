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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Philosopher implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(Philosopher.class);

	// orders
	public static final String CHOICE_RANDOM = "random";
	public static final String CHOICE_SOLUTION = "solution";
	public static final String CHOICE_LOCK = "lock";
	public static final String CHOICE_DEMO = "demo";
	
	private int id;
	private CountDownLatch tableOpen;
	
	private Bowl left;
	private Bowl right;
	
	private Bowl second;
	private Bowl first;
	
	private String choice;
	
	public Philosopher(int id, CountDownLatch tableOpen, Bowl left, Bowl right, String choice){
		this.id = id;
		this.tableOpen = tableOpen;
		
		this.left = left;
		this.right = right;
		
		this.choice= choice;
	}

	@Override
	public void run() {
		tableOpen.countDown();
		try {
			tableOpen.await();
		} catch (InterruptedException e) {
			logger.error("Demo interrupted", e);
			Thread.currentThread().interrupt();
		}
		
		log("Table open, let's seat !");
		
		try {
			accessFirstBowl();
		} catch (InterruptedException ex) {
			logger.error("Demo interrupted", ex);
			Thread.currentThread().interrupt();
		}
	}
	
	public void accessFirstBowl() throws InterruptedException{
		Bowl bowl = null;

		log("Let's take the first bowl");
			
		if (CHOICE_SOLUTION.equals(choice))
			bowl = chooseOrderedBowl();
		else if (CHOICE_RANDOM.equals(choice))
			bowl = chooseRandomBowl();
		else if (CHOICE_LOCK.equals(choice))
			bowl = chooseLeftBowl();
		else
			bowl = chooseOrderedBowl();
			
		log("I choose bowl : \t\t" + bowl);
		bowl.getFirstAccess(this);	
	}


	public void accessSecondBowl() throws InterruptedException {
		Bowl bowl2  = this.second;
	
		log("Let's take the second bowl : \t\t" + this.second);

		bowl2.getSecondAccess(this);
	}	
	
	private Bowl chooseOrderedBowl(){
		Bowl elected;
		// take first the highest
		if (this.left.getId() > this.right.getId()){
			elected = this.left;	
			this.second = right;
		}
		else {
			elected = this.right;
			this.second = left;
		}
		
		this.first = elected;

		return elected;
	}	
	
	private Bowl chooseLeftBowl(){
		Bowl elected;
		
		elected = this.left;	
		this.second = right;

		return elected;
	}	
	
	private Bowl chooseRandomBowl(){
		double rand = Math.random()*2;
		long elected = Math.round(rand);
		
		if (elected == 0){
			this.first = left;
			this.second = right;
			return left;				
		}
		else{
			this.first = right;
			this.second = left;
			return right;
		}
	}
	
	public void eatBowls() throws InterruptedException{
		log("I have the 2 bowls. I'm eating half of each. Yummi !!");
		Thread.sleep(10000);
	}

	private void log(String msg) {
		logger.info("Philosopher " + id + " says : " + msg);
	}
	
	public String getName(){
		return "Philosopher-" + id;
	}

}
