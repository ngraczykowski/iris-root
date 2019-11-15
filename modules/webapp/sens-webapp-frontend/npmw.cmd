@echo off

setlocal

set "DIR=%~dp0"
set "PATH=%DIR%\node;%PATH%"

if not exist "%DIR%\node" (
  pushd "%DIR%"
  call mvn frontend:install-node-and-npm
  popd
)

"%DIR%\node\npm.cmd" %*
