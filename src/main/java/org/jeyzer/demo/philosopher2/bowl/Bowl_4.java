package org.jeyzer.demo.philosopher2.bowl;

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


import org.jeyzer.demo.philosopher2.Bowl;
import org.jeyzer.demo.philosopher2.Philosopher;

public class Bowl_4 extends Bowl {

	@Override
	public int getId(){
		return 4;
	}
	
	@Override
	public synchronized void getFirstAccess(Philosopher philosopher) throws InterruptedException{
		ownsFirstBowl(philosopher);
	}
	
	private void ownsFirstBowl(Philosopher philosopher) throws InterruptedException{
		fillBowl();
		Thread.sleep(5000);		
		philosopher.accessSecondBowl();
	}
	
	@Override
	public synchronized void getSecondAccess(Philosopher philosopher) throws InterruptedException{
		ownsSecondBowl(philosopher);
	}
	
	private void ownsSecondBowl(Philosopher philosopher) throws InterruptedException{
		fillBowl();
		Thread.sleep(5000);
		philosopher.eatBowls();
	}
}
