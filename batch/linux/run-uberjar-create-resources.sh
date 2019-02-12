# http://stackoverflow.com/questions/59895/getting-the-source-directory-of-a-bash-script-from-within
DIR_PROJECT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"/../.. #
(cd $DIR_PROJECT; rm ./src/test/resources/encoded-agif/*; cd -) #
(cd $DIR_PROJECT; rm ./src/test/resources/encoded-mov/*; cd -) #
(cd $DIR_PROJECT; rm ./src/test/resources/encoded-series-10/*; cd -) #
(cd $DIR_PROJECT; rm ./src/test/resources/encoded-series-100/*; cd -) #
(cd $DIR_PROJECT; rm ./src/test/resources/encoded-series-1000/*; cd -) #
(cd $DIR_PROJECT; rm ./src/test/resources/sample-data.zip; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties-resources/meaningful-gif.create.agif.10.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties-resources/meaningful-gif.create.agif.100.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties-resources/meaningful-gif.create.agif.1000.properties; cd -) #
#(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties-resources/meaningful-gif.create.mov.10.properties; cd -) #
#(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties-resources/meaningful-gif.create.mov.100.properties; cd -) #
#(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties-resources/meaningful-gif.create.mov.1000.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties-resources/meaningful-gif.create.series.10.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties-resources/meaningful-gif.create.series.100.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties-resources/meaningful-gif.create.series.1000.properties; cd -) #
(cd $DIR_PROJECT; java -jar ./target/uberjar/meaningful-gif-uberjar.jar ./src/test/resources/properties-resources/meaningful-gif.create.zip.properties; cd -) #
