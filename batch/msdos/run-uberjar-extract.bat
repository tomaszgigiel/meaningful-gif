md "%HOMEPATH%\_delete_content\"
pushd %~dp0\..\..
call java -cp .\target\uberjar\meaningful-gif-uberjar.jar pl.tomaszgigiel.meaningful_gif.extract.core .\src\test\resources\meaningful-gif.extract.properties
pause
popd
