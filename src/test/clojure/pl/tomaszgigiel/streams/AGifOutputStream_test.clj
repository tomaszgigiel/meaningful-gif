(ns pl.tomaszgigiel.streams.AGifOutputStream-test
  (:import java.io.ByteArrayOutputStream)
  (:import pl.tomaszgigiel.streams.AGifOutputStream)
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.streams.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(let [bytes-abc (.getBytes "abc")]

  (tst/deftest base-test
    (tst/is (= (with-open [baos (ByteArrayOutputStream.) aos (AGifOutputStream. baos)]
                 (.write aos bytes-abc 0 3)
                 (.flush aos)
                 (seq (.toByteArray baos)))
               (seq bytes-abc))))
)