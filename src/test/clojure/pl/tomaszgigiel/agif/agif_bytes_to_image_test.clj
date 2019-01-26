(ns pl.tomaszgigiel.agif.agif-bytes-to-image-test
  (:import java.awt.Color)
  (:import java.awt.image.BufferedImage)
  (:import java.io.ByteArrayInputStream)
  (:import java.io.ByteArrayOutputStream)
  (:import javax.imageio.ImageIO)
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:require [pl.tomaszgigiel.streams.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

; https://stackoverflow.com/questions/12705385/how-to-convert-a-byte-to-a-bufferedimage-in-java

(defn image-to-bytes [^BufferedImage buffered-image]
  (with-open [baos (ByteArrayOutputStream.)]
    (ImageIO/write buffered-image "gif" baos)
    (.flush baos)
    (.toByteArray baos)))

(defn bytes-to-image-bad
  ([^bytes bytes] (with-open [bais (ByteArrayInputStream. bytes)] (ImageIO/read bais)))
  ([^bytes bytes ^long off ^long len] (with-open [bais (ByteArrayInputStream. bytes off len)] (ImageIO/read bais))))

(defn- byte-array-input-stream [bais]
  (let [buffered-image-pre (ImageIO/read bais)
        buffered-image (BufferedImage. (.getWidth buffered-image-pre) (.getHeight buffered-image-pre) (BufferedImage/TYPE_INT_RGB))]
    (doto (.getGraphics buffered-image) (.drawImage buffered-image-pre 0 0 nil) (.dispose))
    buffered-image))

(defn bytes-to-image-good
  ([^bytes bytes] (with-open [bais (ByteArrayInputStream. bytes)] (byte-array-input-stream bais)))
  ([^bytes bytes ^long off ^long len] (with-open [bais (ByteArrayInputStream. bytes off len)] (byte-array-input-stream bais))))

(defn get-image-for-test []
  (let [a (BufferedImage. 100 100 BufferedImage/TYPE_INT_RGB)]
    (doto (.getGraphics a) (.setColor Color/RED) (.fillRect 10 10 80 80))
    a))

(defn get-bytes-for-test [] (image-to-bytes (get-image-for-test)))

(tst/deftest bytes-to-image-bad-test (tst/is (not= (seq (get-bytes-for-test)) (seq (image-to-bytes (bytes-to-image-bad (get-bytes-for-test)))))))
(tst/deftest bytes-to-image-good-test (tst/is (= (seq (get-bytes-for-test)) (seq (image-to-bytes (bytes-to-image-good (get-bytes-for-test)))))))