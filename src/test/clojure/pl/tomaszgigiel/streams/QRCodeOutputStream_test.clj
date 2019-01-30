(ns pl.tomaszgigiel.streams.QRCodeOutputStream-test
  (:import java.io.ByteArrayOutputStream)
  (:import pl.tomaszgigiel.streams.QRCodeOutputStream)
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:require [pl.tomaszgigiel.qrcode.qrcode :as qrcode])
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.streams.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(defn- to-qrcode-bytes [x]
  (with-open [baos (ByteArrayOutputStream.)
              qrcos (QRCodeOutputStream. baos 200 200)]
    (.write qrcos x 0 (count x))
    (.flush qrcos)
    (seq (.toByteArray baos))))

(let [abc "abc"
      abc-qrcode-expected (agif/image-to-bytes (qrcode/qrcode abc 200 200))
      abc-qrcode-actual (to-qrcode-bytes (.getBytes abc))]

  (tst/deftest abc-test (tst/is (= (seq abc-qrcode-actual) (seq abc-qrcode-expected))))
)