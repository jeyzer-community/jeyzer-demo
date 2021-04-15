#!/bin/sh

# -----------------------------------------------------------------------------
# Java Flight Recorder
# -----------------------------------------------------------------------------

JFR_OPTS=
export JFR_OPTS

if [ "$JAVA_JFR_ACTIVE" = "false" ]; then
  return
fi

if [ "$JAVA_JFR_SUPPORT" = "false" ]; then
  echo "Warning : Java version not detected : Java Flight Recorder (JDK9+) will not be used."
  return
fi

# -----------------------------------------------------------------------------
# Objective : set the JFR_OPTS 
# -----------------------------------------------------------------------------

# The Jeyzer home (demo parent directory)
[ -z "$DEMO_PARENT_HOME" ] && DEMO_PARENT_HOME=`cd "$JEYZER_DEMO_HOME/.." >/dev/null; pwd`
export DEMO_PARENT_HOME

APP_RECORDING_OUTPUT_DIR="$DEMO_PARENT_HOME/work/recordings/$DEMO_AGENT_PROFILE"
JFR_OUTPUT_DIR="$APP_RECORDING_OUTPUT_DIR/jfr"
JFR_RECORDING="$JFR_OUTPUT_DIR/$DEMO_AGENT_PROFILE.jfr"
JFR_CONGIGURATION="$JEYZER_DEMO_HOME/config/jfr/jeyzer-demo.jfc"

# Mkdir is not recursive : create the parent directory
if [ ! -d "$JFR_OUTPUT_DIR" ]; then
  mkdir "$APP_RECORDING_OUTPUT_DIR"
  mkdir "$JFR_OUTPUT_DIR"
fi

if [ ! -d "$JFR_OUTPUT_DIR" ]; then
  echo "Warning : JFR output directory creation failed : Java Flight Recorder will not be used."
  return
fi

JFR_OPTS="-XX:StartFlightRecording=maxage=6h,maxsize=100M,dumponexit=true,settings=$JFR_CONGIGURATION,filename=$JFR_RECORDING"
export JFR_OPTS
