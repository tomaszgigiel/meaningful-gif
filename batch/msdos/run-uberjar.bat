md "%HOMEPATH%\_delete_content\"
pushd %~dp0\..\..
call java -jar .\target\uberjar\meaningful-gif-uberjar.jar .\src\test\resources\meaningful-gif.properties
pause
popd
