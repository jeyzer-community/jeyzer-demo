
Demo libraries
================


1) Demo shared

The lib/shared directory contains the demo-shared library.
The goal of this library is to demonstrate the automatic loading of the shared profiles.
The demo-shared is loaded by the demo features and demo labors applications.
The demo-shared library Manifest file contains the Jeyzer-Repository="demo" attribute.

The demo-shared project includes a Jeyzer shared profile with patterns, rules and stickers, 
deployed (at installation time for convenience) within a Jeyzer external repository called demo. 
The path to the Jeyzer external repository is set through the JEYZER_EXTERNAL_REPOSITORY_SETUP_DIRECTORY environment variable.

At Jeyzer recording time, the Jeyzer-Repository attribute (of the shared-demo library) is stored within the JZR recording.

At Jeyzer analysis time, the demo-shared profile is loaded based on the Jeyzer-Repository attribute.
Related patterns, stickers and rules are therefore applied automatically.



2) Demo duplicates

The demo lib/duplicates directory contains fake libraries with constant versions.
Those are required for the testing of the different process jar monitoring rules.
The demo features and demo labors do load it.

The build is fully manual to limit the impact of such small libraries on the global project.
The demo project takes only the jar files in the lib directory.

The jeyzer-demo-dup-no-version.jar is a copy of the jeyzer-demo-dup-3.3.3.jar.
jeyzer-demo-dup-3.3.3.jar is required for the build. It is not shipped (unlike the jeyzer-demo-dup-no-version.jar).

