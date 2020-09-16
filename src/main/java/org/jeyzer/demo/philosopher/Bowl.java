package org.jeyzer.demo.philosopher;

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


public abstract class Bowl {
	
	private boolean available = true;

	public abstract void getFirstAccess() throws InterruptedException;
	
	public abstract void getSecondAccess() throws InterruptedException;	
	
	public abstract void waitForBowl() throws InterruptedException;
	
	public abstract int getId();
	
	public boolean isAvailable(){
		return available;
	}
	
	public boolean hold(){
		available= false;
		return available;
	}
	
	public boolean release(){
		available= true;
		return available;
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}
	
	private List<Long> riceGrains = new ArrayList<>();
	
	protected void fillBowl(){
		for (long i=1; i<1000000; i++){
			riceGrains.add(i);
		}
	}
}
