#!/bin/sh

# -----------------------------------------------------------------------------
#  Set JAVA_HOME if not already set by the installer or the environment.
#  Default JAVA_HOME to JRE_HOME if available
#  Jeyzer start scripts only use JAVA_HOME
# -----------------------------------------------------------------------------

setJavaModuleSupport(){
  JAVA_VERSION=`"$JAVA_HOME/bin/java" -version 2>&1 | awk -F'"' '/version/ {print $2}'`
  JAVA_MAJOR_VERSION=`echo $JAVA_VERSION | awk -F'.' '{ print $1 }'` 

  if [ "$JAVA_MAJOR_VERSION" = "1" ]; then
    JAVA_MODULE_SUPPORT=false   # 1.7 or 1.8
  else
    JAVA_MODULE_SUPPORT=true    # 9, 10..
  fi
}

JEYZER_INSTALLER_DEPLOYMENT=%{jeyzer.installer.deployment}
if [ "$JEYZER_INSTALLER_DEPLOYMENT" = "true" ]; then
  JAVA_HOME="%{jeyzer.installer.java.home}"
  setJavaModuleSupport
  return
fi

# Make sure prerequisite environment variables are set
if [ -z "$JAVA_HOME" -a -z "$JRE_HOME" ]; then
  JAVA_PATH=`which java 2>/dev/null`
  if [ "x$JAVA_PATH" != "x" ]; then
    JAVA_PATH=`dirname $JAVA_PATH 2>/dev/null`
    JRE_HOME=`dirname $JAVA_PATH 2>/dev/null`
  fi
  if [ "x$JRE_HOME" = "x" ]; then
    # XXX: Should we try other locations?
    if [ -x /usr/bin/java ]; then
      JRE_HOME=/usr
    fi
  fi
  if [ -z "$JAVA_HOME" -a -z "$JRE_HOME" ]; then
    echo "Neither the JAVA_HOME nor the JRE_HOME environment variable is defined"
    echo "At least one of these environment variables is needed to run this program"
    exit 1
  fi
fi
if [ -z "$JRE_HOME" ]; then
  JRE_HOME="$JAVA_HOME"
fi
if [ -z "$JAVA_HOME" ]; then
  JAVA_HOME="$JRE_HOME"
fi

setJavaModuleSupport
