# http://stackoverflow.com/questions/59895/getting-the-source-directory-of-a-bash-script-from-within
DIR_PROJECT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"/../.. #
# http://stackoverflow.com/questions/11420221/sbt-run-from-outside-the-project-directory
(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.extract.agif.10.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.extract.agif.100.properties; cd -) #
#(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.extract.mov.10.properties; cd -) #
#(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.extract.mov.100.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.extract.series.10.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.extract.series.100.properties; cd -) #
