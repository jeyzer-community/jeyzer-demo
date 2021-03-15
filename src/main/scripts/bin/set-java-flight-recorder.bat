@echo off

rem -------------------------------------------------------------------------------
rem Java Flight Recorder
rem -------------------------------------------------------------------------------

set JFR_OPTS=

if "%JAVA_JFR_ACTIVE%" == "false" goto end

if "%JAVA_JFR_SUPPORT%" == "true" goto gotJavaJFRSupportOk
echo Warning : Java version not detected : Java Flight Recorder (JDK9+) will not be used.
goto end

:gotJavaJFRSupportOk

rem -------------------------------------------------------------------------------
rem Objective : set the JFR_OPTS
rem -------------------------------------------------------------------------------

rem The Jeyzer home (demo parent directory)
set "CURRENT_DIR=%cd%"
cd ..
cd ..
set "DEMO_PARENT_HOME=%cd%"
cd "%CURRENT_DIR%"

SET "JFR_OUTPUT_DIR=%DEMO_PARENT_HOME%\work\recordings\%DEMO_AGENT_PROFILE%\jfr"
SET "JFR_RECORDING=%JFR_OUTPUT_DIR%\%DEMO_AGENT_PROFILE%.jfr"
SET "JFR_CONGIGURATION=%JEYZER_DEMO_HOME%\config\jfr\jeyzer-demo.jfc"


if exist "%JFR_OUTPUT_DIR%" goto gotJFRDirectory

rem The JFR output directory must be created
mkdir %JFR_OUTPUT_DIR%

if exist "%JFR_OUTPUT_DIR%" goto gotJFRDirectory
echo Warning : JFR output directory creation failed : Java Flight Recorder will not be used.
goto end

:gotJFRDirectory
set "JFR_OPTS=-XX:StartFlightRecording=maxage=6h,maxsize=100M,settings=%JFR_CONGIGURATION%,filename=%JFR_RECORDING%"

echo Java Flight Recorder is detected and will be loaded.

:end
exit /b 0