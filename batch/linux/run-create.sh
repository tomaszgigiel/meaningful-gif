# http://stackoverflow.com/questions/59895/getting-the-source-directory-of-a-bash-script-from-within
DIR_PROJECT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"/../.. #
#
rm -rf $HOME/_delete_content/encoded-agif #
rm -rf $HOME/_delete_content/encoded-mov #
rm -rf $HOME/_delete_content/encoded-series-10 #
rm -rf $HOME/_delete_content/encoded-series-100 #
rm -rf $HOME/_delete_content/encoded-series-1000 #
#
mkdir -p $HOME/_delete_content/encoded-agif #
mkdir -p $HOME/_delete_content/encoded-mov #
mkdir -p $HOME/_delete_content/encoded-series-10 #
mkdir -p $HOME/_delete_content/encoded-series-100 #
mkdir -p $HOME/_delete_content/encoded-series-1000 #
#
(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.create.agif.10.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.create.agif.100.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.create.agif.1000.properties; cd -) #
#(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.create.mov.10.properties; cd -) #
#(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.create.mov.100.properties; cd -) #
#(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.create.mov.1000.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.create.series.10.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.create.series.100.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.create.series.1000.properties; cd -) #
(cd $DIR_PROJECT; lein run ./src/test/resources/properties/meaningful-gif.create.zip.properties; cd -) #
