
                          Jeyzer Demo package 

  What is it?
  -----------

  This demo package is a set of java applications used to demonstrate the Jeyzer Analyzer tool capabilities.
  It contains 4 demo applications :
  
  	- Labors (in reference to the Labours of Heracles)
  	  The Labors demo executes incidents cases like deadlock, thread leaking, contentions, high CPU consumption at task/process/system levels, Xmx checks...
  	  Each incident signature will have to be detected by the Jeyzer analyzer, thanks to the related monitoring rules defined in the the labors-demo profile.
  	  When started with the "scope=<number of jobs>", the Labors demo will pick up randomly this number of jobs and execute it. 3 is a good value.
  	  When started with the "scope=all", the Labors demo will execute all the possible incidents/check cases (around 80).
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
  Important : all demo classes are obfuscated with Proguard, to demonstrate the Jeyzer de-obfuscation capabilities.
  
  
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
  
  
  Logging
  -------

  Each demo application generates functional traces in console and log file.
  Logback configuration file is located in the $JEYZER_DEMO_HOME/config/log directory.
  Log file is generated in the $JEYZER_DEMO_HOME/log directory.
  
  
  Release Notes
  -------------

  2.0 :	- Jeyzer demo package
			- 4 demo applications : Labors, Features, Philosophers, Tollbooth. 
  
  System Requirements
  -------------------

  JDK:
    1.7 or above
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
  
  1) Start the Jeyzer Recorder connecting to localhost:2500  
     JMX port is 2500 for features, 2501 for philosophers and 2502 for toll.
     If the Jeyzer Recorder agent is enabled, ignore this step.

  2) Execute one of the demo application startup scripts available in the $JEYZER_DEMO_HOME/bin directory :
		jeyzer-demo-labors.bat|sh
		jeyzer-demo-features.bat|sh
		jeyzer-demo-philosophers.bat|sh
		jeyzer-demo-toll.bat|sh

  3.a) If you use the Jeyzer Monitor, run it while the demo is up.

  3.b) Once complete, stop the demo application, collect the recording 
	   and analyze it with the Jeyzer Analyzer

  
  
  Contact Info
  ------------
  If you have any questions, comments or suggestions, we would like to hear from
  you.  For reporting bugs, you can get a dump of program settings by clicking
  Support on the Help menu.  Copy and paste the settings into an email to help
  us track down problems.

    Email:  contact@jeyzer.org 
    Web:    https://www.jeyzer.org/

  