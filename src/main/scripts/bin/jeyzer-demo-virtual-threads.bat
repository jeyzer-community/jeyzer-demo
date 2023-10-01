@echo off

rem -------------------------------------
rem
rem Jeyzer Demo Virtual Threads
rem 
rem -------------------------------------

rem -----------------------------------------------------------
rem Demo parameters
rem -----------------------------------------------------------

rem Number of images to download (or threads to start)
rem The image downloader will measure the time required to download images
rem through native threads and virtual threads
rem With high values (ex: 5000), your system may fail sometimes to download the images
rem and this in rare cases could end up with a virtual thread leak
set JEYZER_DEMO_VIRTUAL_THREADS_IMAGE_DOWNLOADER_FETCH_LIMIT=1000


rem -----------------------------------------------------------
rem JMX parameters
rem -----------------------------------------------------------

set JMX_PORT=2504

set "JAVA_OPTS=--enable-preview -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=%JMX_PORT% -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"


rem -----------------------------------------------------------
rem Jeyzer Recorder Agent mode
rem Configure the bin\set-jeyzer-recorder-agent.bat file
rem -----------------------------------------------------------

set DEMO_AGENT_PROFILE=demo-virtual-threads

rem -----------------------------------------------------------
rem Java Flight Recorder activation
rem -----------------------------------------------------------
rem Requires Java 9+
SET JAVA_JFR_ACTIVE=true

rem -----------------------------------------------------------
rem Internals - do not edit
rem -----------------------------------------------------------

rem yellow font over blue back ground
color 1E
title=Jeyzer Demo Virtual Threads
mode 135,3000

rem The Jeyzer Demo home (parent directory)
set "CURRENT_DIR=%cd%"
cd ..
set "JEYZER_DEMO_HOME=%cd%"
cd "%CURRENT_DIR%"

set CLASSPATH=
set MODULE_PATH=

rem Set the Jeyzer Recorder Agent paths if enabled
if not exist "%JEYZER_DEMO_HOME%\bin\set-jeyzer-recorder-agent.bat" goto noSetJeyzerRecorderAgent
call "%JEYZER_DEMO_HOME%\bin\set-jeyzer-recorder-agent.bat"

:noSetJeyzerRecorderAgent

rem Ensure JAVA_HOME is set
if exist "%JEYZER_DEMO_HOME%\bin\check-java21.bat" goto okCheckJava
echo Cannot find "%JEYZER_DEMO_HOME%\bin\check-java21.bat"
echo This file is needed to run this program
goto exit
:okCheckJava
call "%JEYZER_DEMO_HOME%\bin\check-java21.bat"
if errorlevel 1 goto exit

rem Set the JFR parameters
call "%JEYZER_DEMO_HOME%\bin\set-java-flight-recorder.bat"

rem Java debug options
rem set "JAVA_OPTS=%JAVA_OPTS% -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5000"
	
rem - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
rem Classpath and Module path setup

rem Jeyzer-demo-virtual-threads library
set "CLASSPATH=%CLASSPATH%;%JEYZER_DEMO_HOME%\lib\jeyzer-demo-virtual-threads.jar"
goto okDependencyPaths

:okDependencyPaths
rem - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

echo Starting Demo Virtual Threads v${project.version}...
call "%JAVA_HOME%\bin\java.exe" %JEYZER_AGENT% %JFR_OPTS% %JAVA_OPTS% %MODULE_PATH% -cp %CLASSPATH% org.jeyzer.demo.virtualthreads.DemoVT21
goto end

:exit
exit /b 1

:end
exit /b 0