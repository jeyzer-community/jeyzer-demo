@echo off

rem Jeyzer Monitor execution script for the below profile
rem see README.txt for instructions 

rem ============================
rem CONFIGURATION
rem ============================

rem The target profile
set JEYZER_TARGET_PROFILE=demo-virtual-threads

rem The application node name
set JEYZER_TARGET_NAME=Demo-Virtual-Threads instance

rem The Jeyzer record directory
set JEYZER_RECORD_DIRECTORY=${jeyzer.recorder.work.dir}\%JEYZER_TARGET_PROFILE%

rem The monitor scanning period
set JEYZER_MONITOR_SCAN_PERIOD=5m

rem ============================
rem INTERNALS - DO NOT EDIT
rem ============================

rem The recording period - optional
set JEYZER_RECORD_PERIOD=5s

set "PRESET_DIR=%cd%"
cd ..\..
set "JEYZER_ANALYZER_BIN_HOME=%cd%"
call %JEYZER_ANALYZER_BIN_HOME%\jeyzer-monitor.bat
cd "%PRESET_DIR%"