package org.jeyzer.demo.labors.job.executable;

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
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.jeyzer.demo.labors.job.Job;
import org.jeyzer.publish.JzrMonitorHandler;
import org.jeyzer.publish.event.JzrStandardEvent;

public abstract class ExecutableJob extends Job implements Runnable{

	public static final long HOLD_TIC_DURATION = 5000; // 5 sec
	
	protected JzrMonitorHandler jmonitor;
	protected final ExecutableJobDefinition def;
	
	private List<Thread> threads;
	private int lifeTics;
	private JzrStandardEvent laborEvent; // null if system event
	
	public ExecutableJob(ExecutableJobDefinition def) {
		this.def = def;
		this.lifeTics = def.getStartLifeTics();
	}
	
	public abstract void executeJob();
	
	@Override
	public void init() {
		System.setProperty("demo.job." + def.getStickerName(), "true");
	}
	
	@Override
	public void work() {
		ThreadFactory factory = new JobThreadFactory(this.def.getThreadName(), getThreadCount());
		this.threads= new ArrayList<>();
				
		for (int i=0; i<this.def.getThreadCount();i++)
			threads.add(factory.newThread(this));
			
		for (Thread t : threads)
			t.start();
	}
	
	@Override
	public void run() {
		try {
			this.executeJob();			
		}
		finally {
			if (this.laborEvent != null)
				this.jmonitor.terminateEvent(this.laborEvent);
		}
	}
	
	@Override
	public String getEventName() {
		return def.getEventName();
	}
	
	public ExecutableJobDefinition getJobDefinition() {
		return def;
	}
	
	public boolean lifeTic() {
		lifeTics--;
		if (lifeTics == -1) {
			for (Thread t : threads)
				t.interrupt();
			return false;
		}
		return true;
	}
	
	public int getLifeTics() {
		return this.lifeTics;
	}
	
	public void setLifeTics(int lifeTics) {
		this.lifeTics = lifeTics;
	}
	
	public int getWeight() {
		return this.lifeTics * this.def.getThreadCount();
	}
	
	protected void hold() throws InterruptedException{
		if (this.lifeTics > 0)
			Thread.sleep(HOLD_TIC_DURATION * this.lifeTics);
	}
	
	protected void oneTicHold() throws InterruptedException{
		Thread.sleep(HOLD_TIC_DURATION);
	}
	
	// Dummy job overrides it
	protected AtomicInteger getThreadCount() {
		return new AtomicInteger(1);
	}

	protected void setJzrMonitorHandler(JzrMonitorHandler jmonitor) {
		this.jmonitor = jmonitor;
	}

	public boolean isSystemEventBased() {
		return this.def.isSystemEventBased();
	}

	public void setLaborEvent(JzrStandardEvent event) {
		this.laborEvent = event;
	}
}
