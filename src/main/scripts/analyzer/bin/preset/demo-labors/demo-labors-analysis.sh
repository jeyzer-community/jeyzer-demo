#!/bin/sh

# Jeyzer Analysis startup script
# see README.txt for instructions 

# ============================
# CONFIGURATION - BEGIN
# ============================

# The target profile
JEYZER_TARGET_PROFILE="demo-labors"
export JEYZER_TARGET_PROFILE

# The application node name
JEYZER_TARGET_NAME="Demo-labors instance"
export JEYZER_TARGET_NAME

# The issue description
JEYZER_TARGET_DESCRIPTION="All the Jeyzer monitoring rules get applied here on a labors demo application"
export JEYZER_TARGET_DESCRIPTION

# The Jeyzer record directory
JEYZER_RECORD_DIRECTORY=%{jeyzer.recorder.work.dir}/"$JEYZER_TARGET_PROFILE"
export JEYZER_RECORD_DIRECTORY

# ============================
# INTERNALS - DO NOT EDIT
# ============================

# The recording period - optional
JEYZER_RECORD_PERIOD=5s
export JEYZER_RECORD_PERIOD

# Jeyzer Analyzer bin home (parent-parent directory)
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

# set JEYZER_ANALYZER_BIN_HOME
[ -z "$JEYZER_ANALYZER_BIN_HOME" ] && JEYZER_ANALYZER_BIN_HOME=`cd "$PRGDIR/../.." >/dev/null; pwd`

cd $JEYZER_ANALYZER_BIN_HOME
$JEYZER_ANALYZER_BIN_HOME/jeyzer-analysis.sh
cd $PRGDIR