package org.jeyzer.demo.philosopher.bowl;

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


import org.jeyzer.demo.philosopher.Bowl;

public class Bowl_3 extends Bowl{

	@Override
	public int getId(){
		return 3;
	}
	
	@Override
	public void getFirstAccess() throws InterruptedException{
		fillBowl();
		Thread.sleep(5000);
	}
	
	@Override
	public void getSecondAccess() throws InterruptedException{
		fillBowl();
		Thread.sleep(5000);
	}
	
	@Override
	public void waitForBowl() throws InterruptedException{
		Thread.sleep(500);
	}
}
