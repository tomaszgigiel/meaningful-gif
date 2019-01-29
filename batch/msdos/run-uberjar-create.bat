md "%HOMEPATH%\_delete_content\"
pushd %~dp0\..\..
call java -cp .\target\uberjar\meaningful-gif-uberjar.jar pl.tomaszgigiel.meaningful_gif.create.core .\src\test\resources\meaningful-gif.create.properties
pause
popd
