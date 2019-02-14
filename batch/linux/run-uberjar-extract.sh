# http://stackoverflow.com/questions/59895/getting-the-source-directory-of-a-bash-script-from-within
DIR_PROJECT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"/../.. #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties/meaningful-gif.extract.agif.10.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties/meaningful-gif.extract.agif.100.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties/meaningful-gif.extract.agif.1000.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties/meaningful-gif.extract.mov.10.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties/meaningful-gif.extract.mov.100.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties/meaningful-gif.extract.mov.1000.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties/meaningful-gif.extract.series.10.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties/meaningful-gif.extract.series.100.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties/meaningful-gif.extract.series.1000.properties; cd -) #
