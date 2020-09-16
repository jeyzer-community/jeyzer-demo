#!/bin/sh

# -------------------------------------
#
# Jeyzer demo Philosophers
# 
# -------------------------------------

# -----------------------------------------------------------
# Demo parameters
# -----------------------------------------------------------

# Possible scenario : random, solution, lock, demo (=solution, random x 2, deadlock)
SCENARIO=demo
export SCENARIO 


# -----------------------------------------------------------
# Jeyzer Recorder mode 
# set the JMX parameters
# -----------------------------------------------------------

# JMX options
JMX_PORT=2501
export JMX_PORT

JAVA_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
export JAVA_OPTS


# -----------------------------------------------------------
# Jeyzer Recorder Agent mode
# Configure the bin/set-jeyzer-recorder-agent.sh file
# -----------------------------------------------------------

DEMO_AGENT_PROFILE=demo-philosophers
export DEMO_AGENT_PROFILE


# -----------------------------------------------------------
# Internals - do not edit
# -----------------------------------------------------------

# Jeyzer Demo home (parent directory)
# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get standard environment variables
PRGDIR=`dirname "$PRG"`

# set JEYZER_DEMO_HOME
[ -z "$JEYZER_DEMO_HOME" ] && JEYZER_DEMO_HOME=`cd "$PRGDIR/.." >/dev/null; pwd`

CLASSPATH=

# Set the Jeyzer Recorder Agent paths if enabled
if [ -r "$JEYZER_DEMO_HOME"/bin/set-jeyzer-recorder-agent.sh ]; then
  . "$JEYZER_DEMO_HOME"/bin/set-jeyzer-recorder-agent.sh
fi

# Ensure JAVA_HOME is set
if [ -r "$JEYZER_DEMO_HOME"/bin/check-java.sh ]; then
  . "$JEYZER_DEMO_HOME"/bin/check-java.sh
else
  echo "Cannot find $JEYZER_DEMO_HOME/bin/check-java.sh"
  echo "This file is needed to run this program"
  exit 1
fi

# JVM options
#JAVA_OPTS="$JAVA_OPTS -Xmn15m -Xms20m -Xmx20m"

# logging libraries
CLASSPATH="$CLASSPATH:$JEYZER_DEMO_HOME/lib/slf4j-api-${jeyzer.demo-slf4j-api.version}.jar:$JEYZER_DEMO_HOME/lib/logback-core-${jeyzer.demo-logback-core.version}.jar:$JEYZER_DEMO_HOME/lib/logback-classic-${jeyzer.demo-ch.qos.logback.logback-classic.version}.jar:$JEYZER_DEMO_HOME/lib/janino-${jeyzer.demo-org.codehaus.janino.janino.version}.jar:$JEYZER_DEMO_HOME/lib/commons-compiler-${jeyzer.demo-janino.commons-compiler.version}.jar"

# Jeyzer-demo, Jeyzer-demo-shared libraries and logback
CLASSPATH="$CLASSPATH:$JEYZER_DEMO_HOME/lib/jeyzer-demo.jar:$JEYZER_DEMO_HOME/lib/jeyzer-demo-shared-${project.version}.jar:$JEYZER_DEMO_HOME/config/log"
export CLASSPATH

# Java debug options
# JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5000"

echo Starting Demo Philosophers v${project.version}...
$JAVA_HOME/bin/java $JEYZER_AGENT $JAVA_OPTS org.jeyzer.demo.philosopher2.PhilosophersDemo type=$SCENARIO
