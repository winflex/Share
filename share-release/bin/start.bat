@echo off


title Share Server

echo server starting...

setlocal enabledelayedexpansion

set "CURRENT_DIR=%cd%"
set "SERVER_HOME=%CURRENT_DIR%"

rem find server home
if exist "%SERVER_HOME%\bin\start.bat" goto okHome
cd ..
set "SERVER_HOME=%cd%"
cd "%CURRENT_DIR%"
if exist "%SERVER_HOME%\bin\start.bat" goto okHome

echo SERVER_HOME not found.
goto end

:okHome
echo using server home %SERVER_HOME%

rem find classpaths
set "CLASSPATH=.;%SERVER_HOME%\conf;%SERVER_HOME%\bin"
cd %SERVER_HOME%\lib
for /f %%i in ('dir /b /s "*.jar"') do (
  set "CLASSPATH=!CLASSPATH!;%%i"
)
cd "%SERVER_HOME%\lib\ext"
for /f %%i in ('dir /b /s "*.jar"') do (
	set "CLASSPATH=!CLASSPATH!;%%i"
)
cd %CURRENT_DIR%
echo CLASSPATH %CLASSPATH%

rem boot the server
set BOOTSTRAP_CLASS=cc.lixiaohui.share.server.Bootstrap
set JAVA_CMD=java

%JAVA_CMD% -classpath %CLASSPATH% -Dserver.home=%SERVER_HOME% %BOOTSTRAP_CLASS% 

rem boot finished, goto the end
goto end


:end
pause