
                          Jeyzer Demo package 

  What is it?
  -----------

  This demo package is a set of java applications used to demonstrate the Jeyzer Analyzer tool capabilities.
  It contains 5 demo applications :
  
  	- Virtual Threads
	  The Virtual Threads demo executes classical Loom tests. 
	  It is almost derived from Bazlur Rahman's demo code available at https://github.com/rokon12/project-loom-slides-and-demo-code
	  The application is exploring 2 topics with 7 sequences :
	  1. Massive number of virtual threads sleeping, working (with 1 and 2 executors), accessing I/O
	     The I/O sequence is the most advanced and interesting one as it compares platform and virtual threads, using the Executors and StructuredTaskScope frameworks.
	     The I/O sequence load can be controlled with the JEYZER_DEMO_VIRTUAL_THREADS_IMAGE_DOWNLOADER_FETCH_LIMIT environment variable (1000 by default)
	     With a value of 5000, you may experiment some few pending threads still waiting for results : we do assume this is coming from the network layer.
	     This I/O sequence confirms the need for throttling when using virtual threads to prevent from high performance side effects.
	     The I/O sequence code has been courtesy redesigned and enriched by José Paumard (Paris JUG).
	  2. Locking inside the virtual threads : the spinning synchronization case, the reentrant lock case and the deadlock case 
	     The latter case shows that it is difficult to troubleshoot it, as raised initially by Heinz Kabutz.
	  The JZR report will permit to highlight the different cases from functional and technical view : look at the virtual threads sheet.
	  The jcmd.bat tool is provided in the virtual-threads directory, along with the build and source resources.
	  The jcmd.bat and build.bat require a JDK 21.
  
  	- Labors (in reference to the Labours of Heracles)
  	  The Labors demo executes incidents cases like deadlock, thread leaking, contentions, high CPU consumption at task/process/system levels, Xmx checks...
  	  Each incident signature will have to be detected by the Jeyzer analyzer, thanks to the related monitoring rules defined in the the labors-demo profile.
  	  When started with the "scope=<number of jobs>", the Labors demo will pick up randomly this number of jobs and execute it. 3 is a good value.
  	  When started with the "scope=all", the Labors demo will execute all the possible incidents/check cases (around 90).
  	  The "scope=all" mode is also used as part of the Jeyzer end to end testing : it takes about 1 hour to complete. All monitoring rules must match there.
  	  When started with the "jobs=<list of comma separated job names>", the Labors demo will execute all the specified jobs in its declaration order.
  	  Use the "list" parameter to display the list of available jobs.
  	  Note that the $JEYZER_DEMO_HOME/samples/demo-labors directory contains the All labors JZR report (and related recording) which guarantees the testing of the current Jeyzer release.
  	  Monitoring rules get activated through the sticker mechanism : for each executed case, 
  	  the labors demo will activate the related monitoring rule sticker property (exposed as a process card property).
  	  Monitoring rules are divided in system rules (mostly checks at different levels) and runtime rules (the real incidents). 
  	  
	- Features
	  The Features demo executes pattern actions which permit to generate under Jeyzer a report 
	  featuring the different tool capabilities such as long running tasks, locks, etc.
	  The Features demo is delivered with 2 profiles : 
	  - demo-features: standard one containing standard features
	  - demo-features-mx: advanced one offering MX features as well as a discovery mode : 
	    part of the demo - of course not referred into the demo-features-mx profile - relates to commercial aircraft companies.
	    Enter related company names to highlight those in the Discovery sheet. 
	  The Features demo is linked with the Jeyzer publish library to expose process and user action info through the Jeyzer MX interface.
	  The Features demo is also linked with the demo shared library to demonstrate the dynamic shared profile loading within the demo project.
	  The Features demo is also exposing data through the DemoFeatures and MilitaryFlightMission MX beans.
	  The $JEYZER_DEMO_HOME/samples/demo-features directory contains the JZR demo-features-mx report and it related recording.
	  
	- Philosophers
	  The Philosophers demo illustrates the well known academic locking exercise. 
	  The application is running 4 scenarios : the solution, two random cases and the deadlock solution.
	  Philosophers are represented as threads, competing for bowl resources through intrinsic locks (synchronized).
	  The JZR report will permit to highlight the different cases from functional and technical view.
  
	- Tollbooth
	  The Tollbooth demo illustrates the competition between 2 sets of thread pools in a producer / consumer model.
	  Each producer thread represents a car. Cars do consume CPU when active.
	  Each consumer thread represents a toll worker. Toll workers do consume memory when active.
	  Cars are generated periodically, sometimes in numerous way (rush hour).
	  Toll workers are supervised by a toll manager which can decides to increase 
	  or reduce the number of toll workers based on the incoming car rate.
	  The JZR report will permit to highlight the several technical or functional defects.
    
  
  Package content
  ---------------

  The demo package is shipped with libraries available in the $JEYZER_DEMO_HOME/lib directory.
  It contains the Jeyzer demo libraries and logback logging libraries.
  The demo libraries include 3 fake libraries (prefixed with jeyzer-demo-dup) used to demonstrate the versionning and duplicate checks.
  The jeyzer-demo-shared library is provided to test the dynamic shared profile loading : it contains the Jeyzer-Repository=demo in its Manifest attributes.
  Each demo application is delivered with its source code.
  All demo binaries are Java modules, backward compatible with JDK 7, except the virtual threads one which was built with Java 21.
  Important : all demo classes (except the virtual threads ones) are obfuscated with Proguard, to demonstrate the Jeyzer de-obfuscation capabilities.
  The demo package also include a JFR configuration available in the $JEYZER_DEMO_HOME/config/jfr directory.
  
  
  Jeyzer profiles
  ---------------
  
  Each demo application is delivered with its Jeyzer master profile.
  Master profiles are available within the $JEYZER_HOME/profiles/demo/master directory.
  
  One demo shared profile is provided and linked to the jeyzer-shared-demo library/java project.
  It permits to demonstrate the dynamic shared profile loading.
  This shared profile contains one unique rule, sticker and function pattern.
  It is available within the $JEYZER_HOME/profiles/demo/shared directory.
  
  One demo shared profile repository is declared. It contains the above shared profile.
  The demo shared repository is declared in the $JEYZER_HOME/profiles/shared-repositories directory.
  
  
  Java Flight Recorder
  --------------------
  
  Each demo is configured to generate a JFR recording to illustrate the Jeyzer Analyzer tool capabilities to handle such format.
  Important : demo applications must be stopped using a SIGQUIT signal (kill -3 or CTRL+C), 
   otherwise the JVM will not dump its events and the JFR recording will therefore be empty. 
  To disable it, set the JAVA_JFR_ACTIVE environment variable - defined in every demo start script - to false.
  The JFR configuration can be customized through the $JEYZER_DEMO_HOME/config/jfr/jeyzer-demo.jfc file.
  By default the JFR thread dump period is set to 5 seconds.
  Note that JFR support requires Java 11+. On Java 8, the JFR recording on the demos is disabled.
  
  
  Logging
  -------

  Each demo application generates functional traces in console and log file.
  Logback configuration file is located in the $JEYZER_DEMO_HOME/config/log directory.
  Log file is generated in the $JEYZER_DEMO_HOME/log directory.
  
  
  Time zones
  -------

  To demonstrate the time zone management, the Demo features is started in US/Pacific time zone
  and the Demo labors is started in the GMT time zone. 
  Those settings define therefore the recording time zones.
  
  The display time zone is set by default in the profiles : 
  the Demo features profile is using the US/Central time zone 
  and the Demo labors is using the IST time zone (Indian Standard Time).
  
  The JZR report must therefore display the times in the right display time zone, 
  indicating the source ("User selected", "Profile" or "Process") and time zone id.
  
  Those time zone details are available in the Session Details sheet of any JZR report.
  
  
  Release Notes
  -------------

  3.1 :	- Virtual threads : extra demo added
  2.5 :	- Time zones
  2.4 :	- JFR support
  2.0 :	- Jeyzer demo package
			- 4 demo applications : Labors, Features, Philosophers, Tollbooth. 
  
  
  System Requirements
  -------------------

  JDK:
    1.7 or above
    21 for the virtual threads
  Memory:
    No minimum requirement. 
  Disk:
    No minimum requirement. 
  Operating System:
    Unix or Windows. 

	
  Prerequisites
  ----------------------------	

  - Jeyzer Analyzer or Jeyzer Analyzer Web or Jeyzer Monitor
  
  - Jeyzer Recorder (Agent or JMX or JStack).
  
  
	
  Installation
  ----------------------------

  - The Jeyzer demos are automatically deployed and configured by the Jeyzer installer
    By default, the Jeyzer Recorder agent is enabled.
  

 
  Execution
  ----------------------------
  
  1.a) Start the Jeyzer Recorder connecting to localhost:2500  
     JMX port is 2500 for features, 2501 for philosophers, 2502 for toll, 2503 for labors, 2504 for virtual threads.
     If the Jeyzer Recorder agent is enabled, ignore this step.

  1.b) If you intend to run the virtual threads demo, start the $JEYZER_DEMO_HOME\virtual-threads\jcmd.bat on Windows.
     On Unix, execute the watch command (requires the procps package) : 
     watch -n 30 'jcmd Thread.dump_to_file -format=txt > $JEYZER_HOME/work/recordings/jeyzer-demo-virtual-threads/jcmd-dump-${date}.txt'
     Replace $JEYZER_HOME with the right path value.
     Note that the JFR and JZR recordings will also be generated.

  2) Execute one of the demo application startup scripts available in the $JEYZER_DEMO_HOME/bin directory :
		jeyzer-demo-labors.bat|sh
		jeyzer-demo-features.bat|sh
		jeyzer-demo-philosophers.bat|sh
		jeyzer-demo-toll.bat|sh
		jeyzer-demo-virtual-threads.bat|sh

  3.a) If you use the Jeyzer Monitor, run it while the demo is up.

  3.b) Once complete, stop the demo application (preferably with kill -3 or CTRL+C), 
       collect the recording available in the $JEYZER_HOME/work/recordings/<profile name>/archive directory
       or the JFR recording available in the $JEYZER_HOME/work/recordings/<profile name>/jfr directory
	   and analyze it with the Jeyzer Analyzer

  3.c) For the virtual threads demo, zip the jcmd dump files available in the $JEYZER_HOME/work/recordings/<profile name>
       and analyze it with the Jeyzer Analyzer
  
  
  Licensing
  ----------------------------
  This program is free software distributed under the terms of the Mozilla Public License Version 2.0.
  Please see the file called LICENSE.txt in the licenses directory.
  
  
  
  Contact Info
  ------------
  If you have any questions, comments or suggestions, we would like to hear from
  you.  For reporting bugs, you can get a dump of program settings by clicking
  Support on the Help menu.  Copy and paste the settings into an email to help
  us track down problems.

    Email:  contact@jeyzer.org 
    Web:    https://www.jeyzer.org/

  