# jeyzer-demo virtual threads

Content
------------------
This project contains a demo application used to demonstrate the Jeyzer ecosystem capabilities over the virtual threads.  
It is mostly derived and adapted from [Bazlur Rahman demo](https://github.com/rokon12/project-loom-slides-and-demo-code) code.  
It requires Java 21+.

This project contains only the source code.
It is isolated from the jeyzer-demo project because that one is compiled for Java 7.

Recording snapshot period must be 5 seconds.


Build instructions
------------------

Jeyzer demo vt21 project can be built with Maven.


**Maven**

Edit the build.bat and update the PATH and JAVA_HOME variables.
Execute it.

To integrate it in the standard jeyzer-demo distribution, copy the target/jeyzer-demo-virtual-threads.jar inside the jeyzer-demo/lib directory.



Run instructions
------------------

Execute the run.sh or run.bat


Monitoring instructions
-----------------------

We want here to create periodic thread dumps with the JDK jcmd tool.
Dumps get generated in the work directory.

On Windows, execute the jcmd.bat

On Unix, execute the watch command (requires the procps package) : 
watch -n 30 'jcmd Thread.dump_to_file -format=txt > work/jcmd-dump-${date}.txt'

     
License
-------

Copyright 2020-2024 Jeyzer.

Licensed under the [Mozilla Public License, Version 2.0](https://www.mozilla.org/media/MPL/2.0/index.815ca599c9df.txt)

