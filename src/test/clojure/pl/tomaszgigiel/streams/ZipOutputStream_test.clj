(ns pl.tomaszgigiel.streams.ZipOutputStream-test
  (:import java.io.ByteArrayOutputStream)
  (:import pl.tomaszgigiel.streams.ZipOutputStream)
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:require [pl.tomaszgigiel.utils.resources :as resources])
  (:require [pl.tomaszgigiel.streams.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(defn- zipped-bytes [file]
  (with-open [baos (ByteArrayOutputStream.)
              zos (ZipOutputStream. baos)]
    (.zipFolder zos file)
    (.close zos)
    (seq (.toByteArray baos))))

(let [zip (seq (resources/from-resources-bytes "sample-data.zip"))
      by-path (zipped-bytes (resources/from-resources-path "sample-data"))
      by-uri (zipped-bytes (resources/from-resources-uri "sample-data"))
      by-url (zipped-bytes (resources/from-resources-url "sample-data"))
      by-file (zipped-bytes (resources/from-resources-file "sample-data"))]

  (tst/deftest count-test
    (tst/is (pos? (count zip)))
    (tst/is (= (count zip) (count by-path) (count by-uri) (count by-url) (count by-file))))
  (tst/deftest equal-test (tst/is (misc/equals-beside 12 zip by-path by-uri by-url by-file))))
