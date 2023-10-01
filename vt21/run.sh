#!/bin/sh

# -------------------------------------
# 
# Jeyzer Demo Virtual Threads
# 
# -------------------------------------


# -----------------------------------------------------------
# JAVA_HOME
# -----------------------------------------------------------

PATH=/opt/Openjdk/jdk-21/bin;$PATH

# refer to a Java 20+ jdk
JAVA_HOME=/opt/Openjdk/jdk-21
export JAVA_HOME

# -----------------------------------------------------------
# Demo parameters
# -----------------------------------------------------------

JEYZER_DEMO_VIRTUAL_THREADS_IMAGE_DOWNLOADER_FETCH_LIMIT=5000
export JEYZER_DEMO_VIRTUAL_THREADS_IMAGE_DOWNLOADER_FETCH_LIMIT


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


# -----------------------------------------------------------
# set the JFR parameters
# -----------------------------------------------------------

JAVA_OPTS="-XX:StartFlightRecording=maxage=6h,maxsize=100M,dumponexit=true,settings=$JEYZER_DEMO_HOME/config/jfr/jeyzer-demo.jfc,filename=../../work/demo-virtual-threads/demo-virtual-threads.jfr"

# -----------------------------------------------------------
# set the JMX parameters
# -----------------------------------------------------------

# JMX options
JMX_PORT=2503
export JMX_PORT 

JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Duser.timezone=GMT"
export JAVA_OPTS 


# -----------------------------------------------------------
# Internals - do not edit
# -----------------------------------------------------------

# JVM options
JAVA_OPTS="$JAVA_OPTS -Djdk.trackAllThreads=true -Xmx4g -Xms4g --enable-preview"

# Java debug options
# JAVA_OPTS="$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5000"

echo Starting Demo Labors v${project.version}...
$JAVA_HOME/bin/java $JEYZER_AGENT $JFR_OPTS $JEYZER_PUBLISH_PARAMS $JAVA_OPTS $MODULE_PATH -cp $CLASSPATH org.jeyzer.demo.virtualthreads.DemoVT21 
