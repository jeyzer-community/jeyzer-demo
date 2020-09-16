@echo off

rem -------------------------------------------------------------------------------
rem Set JEYZER_AGENT_HOME if not already set by the installer or the environment.
rem If you see the jeyzer.recorder.agent.home-to-be-set variable in the below
rem    section, it means the Jeyzer installation was done manually. 
rem -------------------------------------------------------------------------------

rem The Jeyzer Recorder Agent home directory
SET "JEYZER_AGENT_HOME=${jeyzer.recorder.agent.home-to-be-set}"

set JEYZER_AGENT=
set "DEMO_RECORD_CONFIG_HOME=%JEYZER_DEMO_HOME%\config\record"
if exist %JEYZER_AGENT_HOME%/lib/jeyzer-agent-${hapiware-agent.version}.jar (

	rem Jeyzer agent parameters
	set "JEYZER_AGENT=-javaagent:"%JEYZER_AGENT_HOME%/lib/jeyzer-agent-${hapiware-agent.version}.jar"=%DEMO_RECORD_CONFIG_HOME%/agent/jeyzer-agent.xml"
	
	rem Jeyzer agent parameters
	set "CLASSPATH=%JEYZER_AGENT_HOME%"
	
	rem Jeyzer recorder log file
    set "JEYZER_RECORDER_LOG_FILE=%JEYZER_AGENT_HOME%\log\jeyzer-recorder-agent-%DEMO_AGENT_PROFILE%.log"
	
	echo Jeyzer Recorder Agent is detected and will be loaded from %JEYZER_AGENT_HOME%.
) else (
	echo No Jeyzer Recorder Agent detected. Please use JMX connection mode. JMX port is %JMX_PORT%.
)
