package org.jeyzer.demo.virtualthreads;


/*-
 * ---------------------------LICENSE_START---------------------------
 * Jeyzer Demo 21 Virtual Threads
 * --
 * Copyright (C) 2020 - 2023 Jeyzer
 * --
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 * ----------------------------LICENSE_END----------------------------
 */


public class DemoVT21 {
    public static void main(String[] args) throws InterruptedException {

    	ClassicMethodSynchronization cms = new ClassicMethodSynchronization();
    	cms.sleep(5);
    	cms.startSequence();
    	
    	ThousandsVirtualThreads mvt = new ThousandsVirtualThreads();
    	mvt.sleep(5); // wait to start jcmd
    	mvt.startSequence();
    
    	ThousandsVirtualThreadsCPU mvtc = new ThousandsVirtualThreadsCPU();
    	mvtc.startSequence();
    	
    	ThousandsVirtualThreadsCPUMultiExecutors tvtcme = new ThousandsVirtualThreadsCPUMultiExecutors();
    	tvtcme.startSequence();
    	
    	ImageDownloader2 id = new ImageDownloader2();
    	id.startSequence();

//        var imageIndexes = IntStream.range(0, 5000).toArray();
//      downloadWithPlatformThreads(imageIndexes);
//        ImageDownloaderJP.downloadWithPlatformThreads(imageIndexes);
    	
    	ClassicalSynchronization cs = new ClassicalSynchronization();
    	cs.startSequence();

    	ReentrantLockSequence rls = new ReentrantLockSequence();
    	rls.startSequence();

    	ReentrantLockDeadlockSequence rlds = new ReentrantLockDeadlockSequence();
    	rlds.startSequence();
    }
}
