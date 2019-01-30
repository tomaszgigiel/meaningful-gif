(ns pl.tomaszgigiel.agif.agif-test
  (:import java.awt.Color)
  (:import java.awt.image.BufferedImage)
  (:import java.io.ByteArrayOutputStream)
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:require [pl.tomaszgigiel.agif.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(defn get-images-for-test []
  (let [a (BufferedImage. 100 100 BufferedImage/TYPE_INT_RGB)
        b (BufferedImage. 100 100 BufferedImage/TYPE_INT_RGB)]
    (doto (.getGraphics a) (.setColor Color/RED) (.fillRect 10 10 80 80))
    (doto (.getGraphics b) (.setColor Color/GREEN) (.fillRect 10 10 80 80))
    [a b]))

(defn get-bytes-for-test []
  (map agif/image-to-bytes (get-images-for-test)))

(let [images-for-test (get-images-for-test)
      bytes-for-test (get-bytes-for-test)
      delay-time 300
      repeat-count 10
      agif-from-images (with-open [baos (ByteArrayOutputStream.)] (agif/create-agif-from-images baos images-for-test delay-time repeat-count) (.toByteArray baos))
      agif-from-images-by-step (with-open [baos (ByteArrayOutputStream.)]
                                 (let [image-writer (agif/begin baos)]
                                   (doseq [f images-for-test] (agif/write-image (agif/bytes-to-image (agif/image-to-bytes f)) image-writer delay-time repeat-count))
                                   (agif/end image-writer))
                                 (.toByteArray baos))
      agif-from-bytes-by-step (with-open [baos (ByteArrayOutputStream.)]
                                (let [image-writer (agif/begin baos)]
                                  (doseq [f bytes-for-test] (agif/write-bytes (agif/image-to-bytes (agif/bytes-to-image f)) 0 (count f) image-writer delay-time repeat-count))
                                  (agif/end image-writer))
                                (.toByteArray baos))]

  (tst/deftest agif-from-images-test (tst/is (pos? (count agif-from-images))))
  (tst/deftest agif-from-images-by-step-test (tst/is (pos? (count agif-from-images-by-step))))
  (tst/deftest agif-from-bytes-by-step-test (tst/is (pos? (count agif-from-bytes-by-step))))
  (tst/deftest agif-test (tst/is (= (count agif-from-images) (count agif-from-images-by-step) (count agif-from-bytes-by-step))))
  (tst/deftest gif-test (tst/is (pos? (count (first bytes-for-test)))))
  (tst/deftest image-to-bytes-test(tst/is (= (count (first bytes-for-test)) (count (agif/image-to-bytes (first images-for-test))))))
  (tst/deftest image-to-bytes-test(tst/is (= (seq (first bytes-for-test)) (seq (agif/image-to-bytes (first images-for-test))))))
  (tst/deftest bytes-to-image-test(tst/is (= (seq (first bytes-for-test))
                                             (seq (agif/image-to-bytes (agif/bytes-to-image (first bytes-for-test))))
                                             (seq (agif/image-to-bytes (agif/bytes-to-image (first bytes-for-test) 0 (count (first bytes-for-test)))))
                                             (seq (agif/image-to-bytes (agif/bytes-to-image (agif/image-to-bytes (agif/bytes-to-image (first bytes-for-test)))))))))
)