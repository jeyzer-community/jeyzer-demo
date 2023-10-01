#!/bin/sh

# -------------------------------------
# 
# Jeyzer demo Virtual Threads
# 
# -------------------------------------


# Number of images to download (or threads to start)
# The image downloader will measure the time required to download images
# through native threads and virtual threads
# With high values (ex: 5000), your system may fail sometimes to download the images
# and this in rare cases could end up with a virtual thread leak
JEYZER_DEMO_VIRTUAL_THREADS_IMAGE_DOWNLOADER_FETCH_LIMIT=1000
export JEYZER_DEMO_VIRTUAL_THREADS_IMAGE_DOWNLOADER_FETCH_LIMIT


# -----------------------------------------------------------
# Jeyzer Recorder mode 
# set the JMX parameters
# -----------------------------------------------------------

# JMX options
JMX_PORT=2504
export JMX_PORT 

JAVA_OPTS="--enable-preview -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Duser.timezone=UTC"
export JAVA_OPTS 


# -----------------------------------------------------------
# Jeyzer Recorder Agent mode
# Configure the bin/set-jeyzer-recorder-agent.sh file
# -----------------------------------------------------------

DEMO_AGENT_PROFILE=demo-virtual-threads
export DEMO_AGENT_PROFILE


# -----------------------------------------------------------
# Java Flight Recorder activation
# -----------------------------------------------------------
# Requires Java 9+
JAVA_JFR_ACTIVE=true
export JAVA_JFR_ACTIVE


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
export JEYZER_DEMO_HOME

CLASSPATH=
MODULE_PATH=

# Set the Jeyzer Recorder Agent paths if enabled
if [ -r "$JEYZER_DEMO_HOME"/bin/set-jeyzer-recorder-agent.sh ]; then
  . "$JEYZER_DEMO_HOME"/bin/set-jeyzer-recorder-agent.sh
fi
  
# Ensure JAVA_HOME is set
if [ -r "$JEYZER_DEMO_HOME"/bin/check-java21.sh ]; then
  . "$JEYZER_DEMO_HOME"/bin/check-java21.sh
else
  echo "Cannot find $JEYZER_DEMO_HOME/bin/check-java.sh"
  echo "This file is needed to run this program"
  exit 1
fi

if [ -r "$JEYZER_DEMO_HOME"/bin/set-java-flight-recorder.sh ]; then
  . "$JEYZER_DEMO_HOME"/bin/set-java-flight-recorder.sh
fi

# JVM options
JAVA_OPTS="$JAVA_OPTS -Xmx768m -Xmn100m -XX:+UseSerialGC"

# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
# Classpath   
CLASSPATH="$JEYZER_DEMO_HOME/lib/jeyzer-demo-virtual-threads.jar"
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

# Java debug options
# JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5000"

echo Starting Demo Virtual Threads v${project.version}...
$JAVA_HOME/bin/java $JEYZER_AGENT $JFR_OPTS $JAVA_OPTS -cp $CLASSPATH org.jeyzer.demo.virtualthreads.DemoVT21 
