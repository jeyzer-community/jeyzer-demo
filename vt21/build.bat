
echo off

echo .
echo ==========================================================================================
echo      Jeyzer demo 21 build
echo ==========================================================================================
echo .

rem refer to a Java 21+ jdk
set PATH=C:\Dev\programs\Java\Openjdk\jdk-21\bin;%PATH%

set JAVA_HOME=C:\Dev\programs\Java\Openjdk\jdk-21

rem == MAVEN BUILD ==
rem display Maven and Java versions
call mvn -v

mvn clean package

