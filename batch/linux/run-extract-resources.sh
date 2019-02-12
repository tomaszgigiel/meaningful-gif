# http://stackoverflow.com/questions/59895/getting-the-source-directory-of-a-bash-script-from-within
DIR_PROJECT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"/../.. #
#
rm -f $HOME/_delete_content/sample-data-10.agif.zip #
rm -f $HOME/_delete_content/sample-data-100.agif.zip #
rm -f $HOME/_delete_content/sample-data-1000.agif.zip #
#
rm -f $HOME/_delete_content/sample-data-10.mov.zip #
rm -f $HOME/_delete_content/sample-data-100.mov.zip #
rm -f $HOME/_delete_content/sample-data-1000.mov.zip #
#
rm -f $HOME/_delete_content/sample-data-10.series.zip #
rm -f $HOME/_delete_content/sample-data-100.series.zip #
rm -f $HOME/_delete_content/sample-data-1000.series.zip #
#
(cd $DIR_PROJECT; lein run ./src/test/resources/properties-resources/meaningful-gif.extract.agif.10.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties-resources/meaningful-gif.extract.agif.100.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties-resources/meaningful-gif.extract.agif.1000.properties; cd -) #
#(cd $DIR_PROJECT; lein run ./src/test/resources/properties-resources/meaningful-gif.extract.mov.10.properties; cd -) #
#(cd $DIR_PROJECT; lein run ./src/test/resources/properties-resources/meaningful-gif.extract.mov.100.properties; cd -) #
#(cd $DIR_PROJECT; lein run ./src/test/resources/properties-resources/meaningful-gif.extract.mov.1000.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties-resources/meaningful-gif.extract.series.10.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties-resources/meaningful-gif.extract.series.100.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties-resources/meaningful-gif.extract.series.1000.properties; cd -) #
