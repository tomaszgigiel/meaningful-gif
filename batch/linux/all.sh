# http://stackoverflow.com/questions/59895/getting-the-source-directory-of-a-bash-script-from-within
DIR_PROJECT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"/../.. #
(cd $DIR_PROJECT; rm -rf ./target; cd -) #
(cd $DIR_PROJECT; ./batch/linux/compile.sh; cd -) #
(cd $DIR_PROJECT; ./batch/linux/test.sh; cd -) #
(cd $DIR_PROJECT; ./batch/linux/uberjar.sh; cd -) #
#
(cd $DIR_PROJECT; ./batch/linux/run-uberjar-create-resources.sh; cd -) #
(cd $DIR_PROJECT; ./batch/linux/test.sh; cd -) #
(cd $DIR_PROJECT; ./batch/linux/run-uberjar-extract-resources.sh; cd -) #
#
(cd $DIR_PROJECT; ./batch/linux/run-uberjar-create.sh; cd -) #
(cd $DIR_PROJECT; ./batch/linux/test.sh; cd -) #
(cd $DIR_PROJECT; ./batch/linux/run-uberjar-extract.sh; cd -) #
#
(cd $DIR_PROJECT; ./batch/linux/run-create-resources.sh; cd -) #
(cd $DIR_PROJECT; ./batch/linux/test.sh; cd -) #
(cd $DIR_PROJECT; ./batch/linux/run-extract-resources.sh; cd -) #
#
(cd $DIR_PROJECT; ./batch/linux/run-create.sh; cd -) #
(cd $DIR_PROJECT; ./batch/linux/test.sh; cd -) #
(cd $DIR_PROJECT; ./batch/linux/run-extract.sh; cd -) #
