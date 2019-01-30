(ns pl.tomaszgigiel.meaningful-gif.helpers
  (:import java.io.File)
  (:import javax.imageio.ImageIO)
  (:require [pl.tomaszgigiel.qrcode.qrcode :as qrcode]))

(comment
  (ImageIO/write (qrcode/qrcode "abc" 100 100) "png" (File. "/home/tomasz/_delete_content/single.png"))
)