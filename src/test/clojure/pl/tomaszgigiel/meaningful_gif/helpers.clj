(ns pl.tomaszgigiel.meaningful-gif.helpers
  (:import java.awt.image.BufferedImage)
  (:import javax.imageio.ImageIO)
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:require [pl.tomaszgigiel.qrcode.qrcode :as qrcode]))

(comment
  (defn black-white [original]
    (let [buffered-image (BufferedImage. (.getWidth original) (.getHeight original) (BufferedImage/TYPE_BYTE_BINARY))]
      (doto (.getGraphics buffered-image) (.drawImage original 0 0 nil) (.dispose))
      buffered-image))
  
  (ImageIO/write (qrcode/qrcode "abc" 100 100) "png" (misc/file-from-resources "single" "quality-good.png")) 
  (-> (misc/file-from-resources "single" "quality-good.png") ImageIO/read qrcode/text)
  (map #(-> % ImageIO/read qrcode/text misc/swallow-exceptions) (-> (misc/file-from-resources "single-low") .listFiles))
  (map #(-> % ImageIO/read qrcode/text misc/swallow-exceptions) (-> (misc/file-from-resources "series") .listFiles))
  (map #(-> % ImageIO/read black-white qrcode/text misc/swallow-exceptions) (-> (misc/file-from-resources "single-low") .listFiles))
  (map #(-> % ImageIO/read black-white qrcode/text misc/swallow-exceptions) (-> (misc/file-from-resources "series") .listFiles))
)