(ns pl.tomaszgigiel.utils.misc-test
  (:import java.io.BufferedOutputStream)
  (:import org.apache.commons.io.IOUtils)
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:require [pl.tomaszgigiel.utils.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(tst/deftest pprint-is-test
  (tst/is (= (with-open [is (IOUtils/toInputStream "abc")] (with-out-str (misc/pprint-is is))) "[97 98 99]\n")))

(tst/deftest print-is-test
  (tst/is (= (with-open [is (IOUtils/toInputStream "abc")] (with-out-str (misc/print-is is))) "abc\n")))

(tst/deftest piped-input-stream-1-test
  (tst/is (= (with-open [is (misc/piped-input-stream (fn [o] (spit o "opens o with writer, writes, closes")))]
              (with-out-str (misc/print-is is)))
            "opens o with writer, writes, closes\n")))

(tst/deftest piped-input-stream-2-test
  (tst/is (= (with-open [is (misc/piped-input-stream (fn [o] (with-open [output (-> o BufferedOutputStream.)] (doseq [a [97 98 99]] (.write output a)))))]
              (with-out-str (misc/print-is is)))
            "abc\n")))