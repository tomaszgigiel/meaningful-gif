(ns pl.tomaszgigiel.qrcode.qrcode-test
  (:import java.awt.Color)
  (:import java.awt.image.BufferedImage)
  (:import java.io.ByteArrayOutputStream)
  (:import java.nio.file.Files)
  (:import javax.imageio.ImageIO)
  (:require [clojure.java.io :as io])
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:require [pl.tomaszgigiel.qrcode.qrcode :as qrcode])
  (:require [pl.tomaszgigiel.qrcode.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(let [text "abc"]
  (tst/deftest simple-test (tst/is (= text (qrcode/text (qrcode/qrcode text 100 100))))))