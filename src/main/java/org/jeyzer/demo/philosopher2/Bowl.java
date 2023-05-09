package org.jeyzer.demo.philosopher2;

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



import java.util.ArrayList;
import java.util.List;


public abstract class Bowl {

	private List<Long> riceGrains = new ArrayList<>();	

	public abstract void getFirstAccess(Philosopher philosopher) throws InterruptedException;
	
	public abstract void getSecondAccess(Philosopher philosopher) throws InterruptedException;	
	
	public abstract int getId();
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName();
	}
	
	protected void fillBowl(){
		for (long i=1; i<1000000; i++){
			riceGrains.add(i);
		}
	}
}
