
Demo libraries
================


1) Demo duplicates

The demo lib/duplicates directory contains fake libraries with constant versions.
Those are required for the testing of the different process jar monitoring rules.
The demo features and demo labors do load it.

The build is fully manual to limit the impact of such small libraries on the global project.
The demo project takes only the jar files in the lib directory.

The jeyzer-demo-dup-no-version.jar is a copy of the jeyzer-demo-dup-3.3.3.jar.
jeyzer-demo-dup-3.3.3.jar is required for the build. It is not shipped (unlike the jeyzer-demo-dup-no-version.jar).


2) Demo shared

See the Jeyzer-community/jeyzer-demo-shared project
https://github.com/jeyzer-community/jeyzer-demo-shared

