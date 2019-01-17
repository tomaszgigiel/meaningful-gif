cd %~dp0/../..
md "%HOMEPATH%/_delete_content/"
start lein run-main-meaningful-gif "./src/test/resources/input" -o "%HOMEPATH%/_delete_content/output.gif"
pause
