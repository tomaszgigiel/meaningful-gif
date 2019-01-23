(ns pl.tomaszgigiel.agif.agif-test
  (:import java.awt.Color)
  (:import java.awt.image.BufferedImage)
  (:import java.io.ByteArrayOutputStream)
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:require [pl.tomaszgigiel.streams.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(let [a (with-open [baos (ByteArrayOutputStream.)]
          (agif/create-agif baos (agif/test-images) 300 10)
          (.toByteArray baos))
      b (with-open [baos (ByteArrayOutputStream.)]
          (let [image-writer (agif/begin baos)
                delay-time 300
                repeat-count 10]
            (doseq [f (agif/test-bytes)] (agif/write-bytes f 0 (count f) image-writer delay-time repeat-count))
            (agif/end image-writer))
          (.toByteArray baos))]

  (tst/deftest create-agif-test (tst/is (pos? (count a))))
  (tst/deftest write-bytes-test (tst/is (pos? (count b))))
  (tst/deftest agif-test(tst/is (= (count a) (count b)))) ;TODO: why not?
)