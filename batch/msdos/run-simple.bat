md "%HOMEPATH%\_delete_content\"
pushd %~dp0\..\..
call lein do clean, run .\src\test\resources\meaningful-gif.properties
pause
popd
