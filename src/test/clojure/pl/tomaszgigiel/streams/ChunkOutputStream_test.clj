(ns pl.tomaszgigiel.streams.ChunkOutputStream-test
  (:import java.io.ByteArrayOutputStream)
  (:import pl.tomaszgigiel.streams.ChunkOutputStream)
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.streams.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(defn- bytes-chunked [x chunk-size]
  (with-open [baos (ByteArrayOutputStream.)
              cos (ChunkOutputStream. baos chunk-size)]
    (.write cos x 0 (count x))
    (.flush cos)
    (seq (.toByteArray baos))))

(let [chunk-size 20
      cargo-size (* chunk-size 1024 1024)
      cargo (byte-array cargo-size (byte \a))
      cargo-actual (bytes-chunked cargo chunk-size)
      abc (.getBytes "abc")
      abc-actual (bytes-chunked abc chunk-size)
      abc-expected (map byte "1zabc")]

  (tst/deftest abc-test (tst/is (= abc-actual abc-expected)))
  (tst/deftest cargo-test
    (tst/is (> (count cargo-actual) cargo-size))
    (tst/is (= (->> cargo-actual (take 3) (map char) (apply str)) "1za"))))
