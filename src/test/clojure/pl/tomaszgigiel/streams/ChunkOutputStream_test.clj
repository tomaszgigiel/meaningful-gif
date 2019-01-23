(ns pl.tomaszgigiel.streams.ChunkOutputStream-test
  (:import java.io.ByteArrayOutputStream)
  (:import pl.tomaszgigiel.streams.ChunkOutputStream)
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.streams.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(let [bytes-abc (.getBytes "abc")]

  (tst/deftest base-test
    (tst/is (= (with-open [baos (ByteArrayOutputStream.) cos (ChunkOutputStream. baos)]
                 (.write cos bytes-abc 0 (count bytes-abc))
                 (.flush cos)
                 (seq (.toByteArray baos)))
               (seq bytes-abc))))
)