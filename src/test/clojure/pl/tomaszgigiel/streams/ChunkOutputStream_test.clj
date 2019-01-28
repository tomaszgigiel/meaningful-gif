(ns pl.tomaszgigiel.streams.ChunkOutputStream-test
  (:import java.io.ByteArrayOutputStream)
  (:import pl.tomaszgigiel.streams.ChunkOutputStream)
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.streams.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(let [bytes-abc (.getBytes "abc")
      bytes-abc-chunked-expected (byte-array [(byte \1) ;  0 
                                              (byte \0) ;  1
                                              (byte \0) ;  2
                                              (byte \0) ;  3
                                              (byte \0) ;  4
                                              (byte \0) ;  5
                                              (byte \0) ;  6
                                              (byte \0) ;  7
                                              (byte \0) ;  8
                                              (byte \0) ;  9
                                              (byte \0) ; 10
                                              (byte \0) ; 11
                                              (byte \0) ; 12
                                              (byte \0) ; 13
                                              (byte \0) ; 14
                                              (byte \0) ; 15
                                              (byte \a) ; 16
                                              (byte \b) ; 17
                                              (byte \c)]) ; 18
      bytes-abc-chunked (with-open [baos (ByteArrayOutputStream.) cos (ChunkOutputStream. baos)]
                          (.write cos bytes-abc 0 (count bytes-abc))
                          (.flush cos)
                          (seq (.toByteArray baos)))]

  (tst/deftest base-test
    (tst/is (= (seq bytes-abc-chunked-expected) (seq bytes-abc-chunked-expected))))
)