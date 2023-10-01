@echo off

rem --------------------------------------------------------------------------
rem Set JAVA_HOME if not already set by the installer or the environment.
rem Default JAVA_HOME to JRE_HOME if available
rem Jeyzer start scripts only use JAVA_HOME
rem --------------------------------------------------------------------------

rem If Jeyzer installation result, JAVA_HOME is automatically set
set JEYZER_INSTALLER_DEPLOYMENT=${jeyzer.installer.deployment}
if "%JEYZER_INSTALLER_DEPLOYMENT%" == "true" goto gotJavaHomeSetByInstaller

rem Otherwise either JRE or JDK are fine
if not "%JRE_HOME%" == "" goto gotJreHome
if not "%JAVA_HOME%" == "" goto gotJavaHome
echo Neither the JAVA_HOME nor the JRE_HOME environment variable is defined
echo At least one of these environment variables is needed to run this program
goto exit

:gotJavaHome
rem No JRE given, use JAVA_HOME as JRE_HOME
set "JRE_HOME=%JAVA_HOME%"

:gotJreHome
rem Check if we have a usable JRE
if not exist "%JRE_HOME%\bin\java.exe" goto noJreHome
set "JAVA_HOME=%JRE_HOME%"
goto okJava

:noJreHome
rem Needed at least a JRE
echo The JRE_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto exit

:gotJavaHomeSetByInstaller
set JAVA_HOME=${jeyzer.installer.java.home}
goto okJava

:okJava
rem echo Using JAVA_HOME=%JAVA_HOME%

rem Check the Java module support (Java9+)
set JAVA_VERSION=notfound
for /f tokens^=2-5^ delims^=.+-_^" %%j in ('%JAVA_HOME%\bin\java -fullversion 2^>^&1') do set " JAVA_VERSION=%%j"
echo Jeyzer Virtual Threads demo will run with Java %JAVA_VERSION%

if %JAVA_VERSION% NEQ notfound goto gotJavaVersion

echo Java version not detected : will try to run it anyway.
goto end

:gotJavaVersion

set JAVA_MODULE_SUPPORT=true
set JAVA_JFR_SUPPORT=true


if %JAVA_VERSION% EQU 20 goto end
if %JAVA_VERSION% EQU 21 goto end
if %JAVA_VERSION% EQU 22 goto end
if %JAVA_VERSION% EQU 23 goto end
if %JAVA_VERSION% EQU 24 goto end

echo The Jeyzer Virtual Threads demo require Java 20+. Please update the JAVA_HOME in the demo/bin/check-java21.bat.
goto exit

:exit
exit /b 1

:end
exit /b 0
