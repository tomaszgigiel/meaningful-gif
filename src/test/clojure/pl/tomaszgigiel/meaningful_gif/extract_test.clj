(ns pl.tomaszgigiel.meaningful-gif.extract-test
  (:import java.io.ByteArrayOutputStream)
  (:import pl.tomaszgigiel.streams.ZipOutputStream)
  (:require [clojure.java.io :as io])
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.meaningful-gif.extract :as extract])
  (:require [pl.tomaszgigiel.meaningful-gif.test-config :as test-config])
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:require [pl.tomaszgigiel.utils.resources :as resources]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(let [zip (resources/from-resources-bytes "sample-data.zip")
      sample-data-agif-10 (->> ["encoded-agif" "sample-data-10.agif"] (apply resources/from-resources-file) extract/extract-agif)
      sample-data-agif-100 (->> ["encoded-agif" "sample-data-100.agif"] (apply resources/from-resources-file) extract/extract-agif)
      sample-data-agif-1000 (->> ["encoded-agif" "sample-data-1000.agif"] (apply resources/from-resources-file) extract/extract-agif)
      sample-data-mov-10 (->> ["encoded-mov" "sample-data-10.mov"] (apply resources/from-resources-file) extract/extract-mov)
      sample-data-mov-100 (->> ["encoded-mov" "sample-data-100.mov"] (apply resources/from-resources-file) extract/extract-mov)
      sample-data-mov-1000 (->> ["encoded-mov" "sample-data-1000.mov"] (apply resources/from-resources-file) extract/extract-mov)
      sample-data-series-10 (->> "encoded-series-10" resources/from-resources-file extract/extract-series)
      sample-data-series-100 (->> "encoded-series-100" resources/from-resources-file extract/extract-series)
      sample-data-series-1000 (->> "encoded-series-1000" resources/from-resources-file extract/extract-series)]

  (tst/deftest count-test
    (tst/is (pos? (count zip)))
    (tst/is (= (count zip)
               (count sample-data-agif-10)
               (count sample-data-agif-100)
               (count sample-data-agif-1000)
               (count sample-data-mov-10)
               (count sample-data-mov-100)
               (count sample-data-mov-1000)
               (count sample-data-series-10)
               (count sample-data-series-100)
               (count sample-data-series-1000))))

  (tst/deftest equal-test
    (tst/is (misc/equals-beside 12 zip
                                sample-data-agif-10
                                sample-data-agif-100
                                sample-data-agif-1000
                                sample-data-mov-10
                                sample-data-mov-100
                                sample-data-mov-1000
                                sample-data-series-10
                                sample-data-series-100
                                sample-data-series-1000))))
