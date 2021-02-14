#!/bin/sh

# -----------------------------------------------------------------------------
# Set JEYZER_AGENT_HOME if not already set by the installer or the environment.
# If you see the jeyzer.recorder.agent.home-to-be-set variable in the below
#    section, it means the Jeyzer installation was done manually. 
# -----------------------------------------------------------------------------

# The Jeyzer Recorder Agent home directory
JEYZER_AGENT_HOME=%{jeyzer.recorder.agent.home-to-be-set}
export JEYZER_AGENT_HOME

if [ -f "$JEYZER_AGENT_HOME/lib/jeyzer-agent-${hapiware-agent.version}.jar" ]; then

	# Jeyzer agent configuration
	DEMO_RECORD_CONFIG_HOME="$JEYZER_DEMO_HOME/config/record"
	export DEMO_RECORD_CONFIG_HOME

	# Jeyzer agent parameters
	JEYZER_AGENT="-javaagent:"$JEYZER_AGENT_HOME/lib/jeyzer-agent-${hapiware-agent.version}.jar"=$DEMO_RECORD_CONFIG_HOME/agent/jeyzer-agent.xml"
	export JEYZER_AGENT
	
	# Jeyzer agent parameters
	CLASSPATH="$JEYZER_AGENT_HOME"
	
	# Jeyzer recorder log file
	JEYZER_RECORDER_LOG_FILE="$JEYZER_AGENT_HOME/log/jeyzer-recorder-agent-"$DEMO_AGENT_PROFILE".log"
	export JEYZER_RECORDER_LOG_FILE
	
	# Recorder boot debug traces
	# JAVA_OPTS="$JAVA_OPTS" -Djeyzer.recorder.boot.debug=true
	
	echo "Jeyzer Recorder agent is detected and will be loaded from $JEYZER_AGENT_HOME."
else
	echo "No Jeyzer Recorder agent detected. Please use JMX connection method. JMX port is $JMX_PORT"
fi
