# jeyzer-demo

Content
------------------
The Jeyzer demo project contains a set of java applications used to demonstrate the Jeyzer ecosystem capabilities.
Jeyzer demos are detailed in the [Jeyzer Demo documentation](src/main/doc/README.txt).

The Jeyzer demo project is also a good example of the Jeyzer build features, in order later to apply it within your build.
It highlights :

 1) **The Jeyzer profile updater usage**
 
     The profile updater permits to amend the Jeyzer demo-features profile with any new pattern entry defined at annotation source code level.
     It is currently executed for testing purposes (but one could use it to update the original patterns file on the demo repository).
	 
	 Jeyzer profile updater is provided as Maven and Gradle plugins. See the respective projects for more details.
     
 2) **The obfuscation context handling**
 
     Obfuscation is achieved here with Proguard : each obfuscation creates a Proguard mapping file with a unique version and build number.
	 
	 Example : > jeyzer-demo-1.0-28-proguard_map.txt
	 
	 This Proguard mapping file will be loaded at thread dump analysis time by the Jeyzer Analyzer to deobfuscate the applicative stacks. The deobuscation is achieved with the `retrace-alt` library (under MIT license).
	 
	 Mapping file is zipped for external usage and also deployed in 2 locations for internal testing : on the file system as default mapper file and under any Tomcat doc web directory (optional). The deployment directories are defined in the project.properties file.
     
     The generated build number, the project name and version are injected as attributes in the jeyzer-demo.jar Manifest file. This context info will be accessed and stored in the application property card - at runtime - either automatically through the Jeyzer agent or programatically through the Jeyzer Publish API. This context info will finally permit to retrieve - at Jeyzer analysis time - the proper mapping file, either from the mapper repository (Tomcat, Nexus..) or from the default mapper file.

The Jeyzer demo relies on a few demo libraries which permit to demonstrate the shared profile handling and the monitoring rules on dependencies.

Please read the [lib/README.txt](lib/README.txt) for more details and check the [demo shared library project](https://github.com/jeyzer-community/jeyzer-demo-shared).

Note that the Jeyzer Demo is deployed by default by the Jeyzer Ecosystem Installer.


Build instructions
------------------

Jeyzer demo project can be built with Maven or Gradle.

Edit the project.properties file to adapt it to your needs and paths.

**Maven**

Under the current directory, execute :

> mvn clean package


**Gradle**

The Gradle build file is provided as a sample. 
It is not guaranteed to work out of the box and probably requires modifications.
As it stands, it accesses the local Maven repository configured in the settings.gradle file.

     
License
-------

Copyright 2020 Jeyzer.

Licensed under the [Mozilla Public License, Version 2.0](https://www.mozilla.org/media/MPL/2.0/index.815ca599c9df.txt)

