md "%HOMEPATH%\_delete_content\"
pushd %~dp0\..\..
call lein do clean, with-profile main-extract run .\src\test\resources\meaningful-gif.extract.properties
pause
popd
