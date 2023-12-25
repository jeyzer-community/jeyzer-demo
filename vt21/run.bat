
echo off

echo .
echo ==========================================================================================
echo      Jeyzer Demo Virtual Threads
echo ==========================================================================================
echo .

rem -----------------------------------------------------------
rem JAVA_HOME parameters
rem -----------------------------------------------------------

rem refer to a Java 20+ jdk
set PATH=C:\Dev\programs\Java\Openjdk\jdk-21\bin;%PATH%
set JAVA_HOME=C:\Dev\programs\Java\Openjdk\jdk-21

rem -----------------------------------------------------------
rem Demo parameters
rem -----------------------------------------------------------

set JEYZER_DEMO_VIRTUAL_THREADS_IMAGE_DOWNLOADER_FETCH_LIMIT=1000

rem -----------------------------------------------------------
rem JMX parameters
rem -----------------------------------------------------------

set JMX_PORT=2500

set "JAVA_OPTS=-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=%JMX_PORT% -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Duser.timezone=UTC"

rem -----------------------------------------------------------
rem JFR parameters
rem -----------------------------------------------------------

set "JAVA_OPTS=%JAVA_OPTS% -XX:StartFlightRecording=maxage=6h,maxsize=100M,dumponexit=true,settings=..\..\..\src\main\config\jfr\jeyzer-demo.jfc,filename=..\..\work\demo-vt-all.jfr"



rem yellow font over blue back ground
color 1E
title=Jeyzer Demo Virtual Threads
mode 135,3000

set "JAVA_OPTS=%JAVA_OPTS% -Djdk.trackAllThreads=true -Xmx4g -Xms4g --enable-preview"

cd target\classes
%JAVA_HOME%\bin\java %JAVA_OPTS% org.jeyzer.demo.virtualthreads.DemoVT21

pause