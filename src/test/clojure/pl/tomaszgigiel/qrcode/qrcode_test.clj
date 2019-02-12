(ns pl.tomaszgigiel.qrcode.qrcode-test
  (:import javax.imageio.ImageIO)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.utils.resources :as resources])
  (:require [pl.tomaszgigiel.qrcode.qrcode :as qrcode])
  (:require [pl.tomaszgigiel.qrcode.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(let [text "abc"]
  (tst/deftest simple-test (tst/is (= text (qrcode/text (qrcode/qrcode text 100 100))))))

(tst/deftest file-test
  (tst/is (str/starts-with? (->> "sample-data-series-10.001.gif" (resources/from-resources-file "encoded-series-10") ImageIO/read qrcode/text) "1z"))
  (tst/is (str/starts-with? (->> "sample-data-series-10.002.gif" (resources/from-resources-file "encoded-series-10") ImageIO/read qrcode/text) "2z"))
  (tst/is (str/starts-with? (->> "sample-data-series-100.001.gif" (resources/from-resources-file "encoded-series-100") ImageIO/read qrcode/text) "1z"))
  (tst/is (str/starts-with? (->> "sample-data-series-100.002.gif" (resources/from-resources-file "encoded-series-100") ImageIO/read qrcode/text) "2z")))
