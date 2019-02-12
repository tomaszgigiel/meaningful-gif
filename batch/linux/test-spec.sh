# http://stackoverflow.com/questions/59895/getting-the-source-directory-of-a-bash-script-from-within
DIR_PROJECT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"/../.. #
#(cd $DIR_PROJECT; lein test :only pl.tomaszgigiel.utils.resources-test; cd -) #
#(cd $DIR_PROJECT; lein test :only pl.tomaszgigiel.chunk.chunk-test; cd -) #
#(cd $DIR_PROJECT; lein test :only pl.tomaszgigiel.streams.ZipOutputStream-test; cd -) #
(cd $DIR_PROJECT; lein test :only pl.tomaszgigiel.meaningful-gif.extract-test; cd -) #
