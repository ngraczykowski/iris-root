@echo off

setlocal

set "DIR=%~dp0"
set "PATH=%DIR%\node;%PATH%"

if not exist "%DIR%\node" (
  pushd "%DIR%"
  call mvn frontend:install-node-and-npm
  popd
)

if not exist "%DIR%\node_modules" (
  call "%DIR%\node\npm.cmd" install
)

"%DIR%\node_modules\.bin\ng.cmd" %*
