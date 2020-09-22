@echo off

rem -------------------------------------
rem 
rem Jeyzer Demo Tollbooth
rem 
rem -------------------------------------

rem -----------------------------------------------------------
rem JMX parameters
rem -----------------------------------------------------------

set JMX_PORT=2502

set "JAVA_OPTS=-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=%JMX_PORT% -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"


rem -----------------------------------------------------------
rem Jeyzer Recorder Agent mode
rem Configure the bin\set-jeyzer-recorder-agent.bat file
rem -----------------------------------------------------------

set DEMO_AGENT_PROFILE=demo-toll


rem -----------------------------------------------------------
rem Internals - do not edit
rem -----------------------------------------------------------

rem yellow font over blue back ground
color 1E
title=Jeyzer Demo Toll
mode 135,3000

rem The Jeyzer Demo home (parent directory)
set "CURRENT_DIR=%cd%"
cd ..
set "JEYZER_DEMO_HOME=%cd%"
cd "%CURRENT_DIR%"

set CLASSPATH=

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

rem Java debug options
rem set "JAVA_OPTS=%JAVA_OPTS% -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5000"

rem JVM options
rem set "JAVA_OPTS=%JAVA_OPTS% -Xmn15m -Xms20m -Xmx20m"

rem logging libraries
set "CLASSPATH=%CLASSPATH%;%JEYZER_DEMO_HOME%\lib\slf4j-api-${jeyzer.demo-slf4j-api.version}.jar;%JEYZER_DEMO_HOME%\lib\logback-core-${jeyzer.demo-logback-core.version}.jar;%JEYZER_DEMO_HOME%\lib\logback-classic-${jeyzer.demo-ch.qos.logback.logback-classic.version}.jar;%JEYZER_DEMO_HOME%\lib\janino-${jeyzer.demo-org.codehaus.janino.janino.version}.jar;%JEYZER_DEMO_HOME%\lib\commons-compiler-${jeyzer.demo-janino.commons-compiler.version}.jar"

rem Jeyzer-demo, Jeyzer-demo-shared libraries and logback
set "CLASSPATH=%CLASSPATH%;%JEYZER_DEMO_HOME%\lib\jeyzer-demo.jar;%JEYZER_DEMO_HOME%\lib\jeyzer-demo-shared-${jeyzer-demo-shared.version}.jar;%JEYZER_DEMO_HOME%\config\log"

echo Starting Demo Tollbooth v${project.version}...
call "%JAVA_HOME%\bin\java.exe" %JEYZER_AGENT% %JAVA_OPTS% -cp %CLASSPATH% org.jeyzer.demo.tollbooth.TollDemo
goto end

:exit
exit /b 1

:end
exit /b 0