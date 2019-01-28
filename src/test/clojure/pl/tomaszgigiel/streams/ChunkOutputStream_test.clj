(ns pl.tomaszgigiel.streams.ChunkOutputStream-test
  (:import java.io.ByteArrayOutputStream)
  (:import pl.tomaszgigiel.streams.ChunkOutputStream)
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.streams.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(defn- to-chunked-bytes [x]
  (with-open [baos (ByteArrayOutputStream.)
              cos (ChunkOutputStream. baos)]
    (.write cos x 0 (count x))
    (.flush cos)
    (seq (.toByteArray baos))))

(let [abc (.getBytes "abc")
      abc-chunked-expected (concat (byte-array (ChunkOutputStream/headerSize) [(byte \1)]) abc)
      abc-chunked (to-chunked-bytes abc) 
      long (byte-array 1600 (byte \a))
      long-chunked-expected (concat
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "1")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "2")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "3")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "4")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "5")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "6")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "7")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "8")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "9")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "a")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "b")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "c")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "d")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "e")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "f")) (byte-array 100 (byte \a))
                              (byte-array (ChunkOutputStream/headerSize) (.getBytes "10")) (byte-array 100 (byte \a)))
      long-chunked (to-chunked-bytes long)]

  (tst/deftest abc-test (tst/is (= (seq abc-chunked) (seq abc-chunked-expected))))
  (tst/deftest long-test (tst/is (= (seq long-chunked) (seq long-chunked-expected))))
)