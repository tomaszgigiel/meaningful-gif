md "%HOMEPATH%\_delete_content\"
pushd %~dp0\..\..
call lein do clean, with-profile main-create run .\src\test\resources\meaningful-gif.create.properties
pause
popd
