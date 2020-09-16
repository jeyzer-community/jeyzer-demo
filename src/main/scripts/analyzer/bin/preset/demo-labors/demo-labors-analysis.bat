@echo off

rem Jeyzer Analysis startup script
rem see README.txt for instructions 

rem ============================
rem CONFIGURATION - BEGIN
rem ============================

rem The target profile
set JEYZER_TARGET_PROFILE=demo-labors

rem The application node name
set JEYZER_TARGET_NAME=Demo-labors instance

rem The issue description
set JEYZER_TARGET_DESCRIPTION=All the Jeyzer monitoring rules get applied here on a labors demo application

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