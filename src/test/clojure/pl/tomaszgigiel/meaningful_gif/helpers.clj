(ns pl.tomaszgigiel.meaningful-gif.helpers
  (:import javax.imageio.ImageIO)
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:require [pl.tomaszgigiel.qrcode.qrcode :as qrcode]))

(comment
  (ImageIO/write (qrcode/qrcode "abc" 100 100) "png" (misc/file-from-resources "single" "quality-good.png")) 
  (-> (misc/file-from-resources "single" "quality-good.png") ImageIO/read qrcode/text)
  (map #(-> % ImageIO/read qrcode/text misc/swallow-exceptions) (-> (misc/file-from-resources "single-low") .listFiles))
  (map #(-> % ImageIO/read qrcode/text misc/swallow-exceptions) (-> (misc/file-from-resources "series") .listFiles))
  (map #(-> % ImageIO/read agif/black-white qrcode/text misc/swallow-exceptions) (-> (misc/file-from-resources "single-low") .listFiles))
  (map #(-> % ImageIO/read agif/black-white qrcode/text misc/swallow-exceptions) (-> (misc/file-from-resources "series") .listFiles))
)