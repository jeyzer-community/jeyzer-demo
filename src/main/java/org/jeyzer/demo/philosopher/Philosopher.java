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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Philosopher implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(Philosopher.class);

	private int id;
	private Semaphore fork;
	private CountDownLatch tableOpen;
	
	private Bowl left;
	private Bowl right;
	
	private Bowl second;
	private Bowl first;
	
	private boolean ordered;
	
	public Philosopher(int id, Semaphore fork, CountDownLatch tableOpen, Bowl left, Bowl right, boolean ordered){
		this.id = id;
		this.fork = fork;
		this.tableOpen = tableOpen;
		
		this.left = left;
		this.right = right;
		
		this.ordered= ordered;
	}

	@Override
	public void run() {
		tableOpen.countDown();
		try {
			tableOpen.await();
		} catch (InterruptedException e) {
			logger.error("Demo interrupted", e);
			Thread.currentThread().interrupt();
			return;
		}
		
		log("Table open, let's seat !");
		
		try {
			accessBowls();
		} catch (InterruptedException ex) {
			logger.error("Demo interrupted", ex);
			Thread.currentThread().interrupt();
		}
	}
	
	private void getFork() throws InterruptedException {
		log("Ask for the fork");
		fork.acquire();
		log("Fork obtained");
	}
	
	private void releaseFork() {
		log("Release the fork");
		fork.release();
	}

	public void accessBowls() throws InterruptedException{
		accessFirstBowl();
		accessSecondBowl();
	}
	
	private void accessFirstBowl() throws InterruptedException{
		Bowl bowl = null;
		
		while (bowl == null){
			getFork();
			log("I have the fork. Let's take the first bowl");
			
			if (ordered)
				bowl = chooseOrderedBowl();
			else
				bowl = chooseRandomBowl();
			
			if (bowl != null)
				break;
			else{
				// 
				log("Could not get 1st bowl. Will wait..");
				releaseFork();
				first.waitForBowl();
			}
		}
		
		log("I took bowl : \t\t" + bowl);
		bowl.getFirstAccess();
		
		log("I have the first bowl. ");
		releaseFork();		
	}


	private void accessSecondBowl() throws InterruptedException {
		Bowl bowl2  = this.second;

		while (true){
			getFork();
			
			log("I have the fork. Let's take the second bowl : \t\t" + this.second);
			
			if (bowl2.isAvailable())
				break;
			
			log("Could not get 2nd bowl. Will wait..");
			releaseFork();
			bowl2.waitForBowl();
		}
		
		bowl2.hold();
		bowl2.getSecondAccess();
		
		log("I have the 2 bowls. I'm eating half of each. Yummi !!");
		eatBowls();
		this.left.release();
		this.right.release();
		releaseFork();
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
		
		if (elected.isAvailable()){
			elected.hold();
			return elected;
		}

		return null;
	}	
	
	private Bowl chooseRandomBowl(){
		double rand = Math.random()*2;
		long elected = Math.round(rand);
		
		if (elected == 0){
			this.first = left;
			if (left.isAvailable()){
				left.hold();
				this.second = right;
				return left;				
			}
			else if (right.isAvailable()){
				right.hold();
				this.second = left;
				this.first = right;
				return right;	
			}
		}
		else{
			this.first = right;
			if (right.isAvailable()){
				right.hold();
				this.second = left;
				return right;	
			}
			else if (left.isAvailable()){
				left.hold();
				this.second = right;
				this.first = left;
				return left;				
			}
		}
		
		return null;
	}
	
	private void eatBowls() throws InterruptedException{
		Thread.sleep(10000);
	}

	private void log(String msg) {
		logger.info("Philosopher " + id + " says : " + msg);
	}
	
	public String getName(){
		return "Philosopher-" + id;
	}

}
