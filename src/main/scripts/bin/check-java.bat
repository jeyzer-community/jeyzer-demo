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
for /f tokens^=2-5^ delims^=.-_^" %%j in ('%JAVA_HOME%\bin\java -fullversion 2^>^&1') do set " JAVA_VERSION=%%j"
rem echo Java version : %JAVA_VERSION%

if %JAVA_VERSION% NEQ notfound goto gotJavaVersion

rem Version not found : let's test now the JDK9+ directory structure (not valid for JREs) :
if exist "%JAVA_HOME%\jmods" goto gotJava9Plus

echo Java version not detected : demo Java modules (JDK9+) will not be used.
goto gotJava8OrLower

:gotJavaVersion

rem JDK 1.7 or 1.8. Starts with 1.
if %JAVA_VERSION% EQU 1 goto gotJava8OrLower

:gotJava9Plus
set JAVA_MODULE_SUPPORT=true
set JAVA_JFR_SUPPORT=true
goto okModuleSupportCheck

:gotJava8OrLower

for /f tokens^=3-5^ delims^=.-_^" %%k in ('%JAVA_HOME%\bin\java -fullversion 2^>^&1') do set " JAVA_OLD_VERSION=%%k"
rem echo Java version : %JAVA_OLD_VERSION%

if %JAVA_OLD_VERSION% EQU 8 goto gotJava8
echo The Jeyzer demos require Java 8+. Please update the JAVA_HOME in the demo/bin/check-java.bat.
goto exit

:gotJava8
set JAVA_MODULE_SUPPORT=false
set JAVA_JFR_SUPPORT=false
goto okModuleSupportCheck

:okModuleSupportCheck
rem echo Module support : %JAVA_MODULE_SUPPORT%
rem echo JFR support : %JAVA_JFR_SUPPORT%

goto end

:exit
exit /b 1

:end
exit /b 0
