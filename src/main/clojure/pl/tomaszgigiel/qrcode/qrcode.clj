(ns pl.tomaszgigiel.qrcode.qrcode
  (:import com.google.zxing.BarcodeFormat)
  (:import com.google.zxing.client.j2se.MatrixToImageWriter)
  (:import com.google.zxing.qrcode.QRCodeWriter)
  (:require [clojure.tools.logging :as log])
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:gen-class))

(defn qrcode [b off len width height]
  (let [text (apply str (map char b))
        writer (QRCodeWriter.)
        bitmatrix (.encode writer text BarcodeFormat/QR_CODE width height)
        image (MatrixToImageWriter/toBufferedImage bitmatrix)]
    (log/info "qrcode text = " text)
    (agif/image-to-bytes image)))