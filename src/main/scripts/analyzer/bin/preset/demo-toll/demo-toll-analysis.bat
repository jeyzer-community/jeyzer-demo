@echo off

rem Jeyzer Analysis execution script for the below profile
rem see README.txt for instructions 

rem ============================
rem CONFIGURATION
rem ============================

rem The target profile
set JEYZER_TARGET_PROFILE=demo-toll

rem The application node name
set JEYZER_TARGET_NAME=Demo-Toll instance

rem The issue description
set JEYZER_TARGET_DESCRIPTION=Jeyzer analysis features get applied on a large multi-threaded application

rem The Jeyzer record directory
set JEYZER_RECORD_DIRECTORY=${jeyzer.recorder.work.dir}\%JEYZER_TARGET_PROFILE%

rem ============================
rem INTERNALS - DO NOT EDIT
rem ============================

rem The recording period - optional
set JEYZER_RECORD_PERIOD=5s

set "PRESET_DIR=%cd%"
cd ..\..
set "JEYZER_ANALYZER_BIN_HOME=%cd%"
call %JEYZER_ANALYZER_BIN_HOME%\jeyzer-analysis.bat
cd "%PRESET_DIR%"