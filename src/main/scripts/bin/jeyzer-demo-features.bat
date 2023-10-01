@echo off

rem -------------------------------------
rem 
rem Jeyzer Demo Features
rem 
rem -------------------------------------

rem -----------------------------------------------------------
rem Jeyzer Recorder mode 
rem set the JMX parameters
rem -----------------------------------------------------------

set JMX_PORT=2500

set "JAVA_OPTS=-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=%JMX_PORT% -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Duser.timezone=US/Pacific"

rem -----------------------------------------------------------
rem Jeyzer Recorder Agent mode
rem Configure the bin\set-jeyzer-recorder-agent.bat file
rem -----------------------------------------------------------

set DEMO_AGENT_PROFILE=demo-features-mx
rem set DEMO_AGENT_PROFILE=demo-features

rem -----------------------------------------------------------
rem Jeyzer Publish parameters
rem -----------------------------------------------------------
rem True by default, set it to false to disable the Jeyzer Publisher
SET "JEYZER_PUBLISH_PARAMS=-Djeyzer.publisher.active=true"

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
title=Jeyzer Demo Features
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
if exist "%JEYZER_DEMO_HOME%\bin\check-java.bat" goto okCheckJava
echo Cannot find "%JEYZER_DEMO_HOME%\bin\check-java.bat"
echo This file is needed to run this program
goto exit
:okCheckJava
call "%JEYZER_DEMO_HOME%\bin\check-java.bat"
if errorlevel 1 goto exit

rem Set the JFR parameters
call "%JEYZER_DEMO_HOME%\bin\set-java-flight-recorder.bat"

rem Java debug options
rem set "JAVA_OPTS=%JAVA_OPTS% -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5000"

rem JVM options
rem -XX:+UseBiasedLocking on Java 21
rem set "JAVA_OPTS=%JAVA_OPTS% -XX:+UseBiasedLocking -server -Xmx256m -Xms128m"
set "JAVA_OPTS=%JAVA_OPTS% -server -Xmx256m -Xms128m"

rem - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
rem Classpath and Module path setup
if "%JAVA_MODULE_SUPPORT%" == "true" goto gotJavaModuleSupport

rem Standard classpath (JDK 8)
rem Jeyzer demo duplicate libraries (for the duplicate lib detection)
set "CLASSPATH=%CLASSPATH%;%JEYZER_DEMO_HOME%\lib\jeyzer-demo-dup-1.1.1.alpha.jar;%JEYZER_DEMO_HOME%\lib\jeyzer-demo-dup-2.2.2-SNAPSHOT.jar;%JEYZER_DEMO_HOME%\lib\jeyzer-demo-dup-no-version.jar"

rem logging libraries
set "CLASSPATH=%CLASSPATH%;%JEYZER_DEMO_HOME%\lib\slf4j-api-${jeyzer.demo-slf4j-api.version}.jar;%JEYZER_DEMO_HOME%\lib\logback-core-${jeyzer.demo-logback-core.version}.jar;%JEYZER_DEMO_HOME%\lib\logback-classic-${jeyzer.demo-ch.qos.logback.logback-classic.version}.jar"

rem Jeyzer publish library
set "CLASSPATH=%CLASSPATH%;%JEYZER_DEMO_HOME%\lib\jeyzer-publish-${jeyzer-publish.version}.jar"

rem Jeyzer-demo, Jeyzer-demo-shared libraries
set "CLASSPATH=%CLASSPATH%;%JEYZER_DEMO_HOME%\lib\jeyzer-demo.jar;%JEYZER_DEMO_HOME%\lib\jeyzer-demo-shared-${jeyzer-demo-shared.version}.jar"
goto okDependencyPaths

rem Modules (JDK 9+)
:gotJavaModuleSupport
set "MODULE_PATH=--module-path %JEYZER_DEMO_HOME%\mods --add-modules org.jeyzer.publish,org.jeyzer.demo.shared,org.slf4j,org.jeyzer.demo"

if not "%JAVA_MODULE_SUPPORT%" == "true" goto okDependencyPaths

:okDependencyPaths
rem - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

rem logback
set "CLASSPATH=%CLASSPATH%;%JEYZER_DEMO_HOME%\config\log"

echo Starting Demo Features v${project.version}...
call "%JAVA_HOME%\bin\java.exe" %JEYZER_AGENT% %JFR_OPTS% %JEYZER_PUBLISH_PARAMS% %JAVA_OPTS% %MODULE_PATH% -cp %CLASSPATH% org.jeyzer.demo.features.FeatureDemo
goto end

:exit
exit /b 1

:end
exit /b 0