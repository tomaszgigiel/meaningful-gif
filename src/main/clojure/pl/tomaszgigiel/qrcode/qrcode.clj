(ns pl.tomaszgigiel.qrcode.qrcode
  (:import com.google.zxing.BarcodeFormat)
  (:import com.google.zxing.client.j2se.MatrixToImageWriter)
  (:import com.google.zxing.qrcode.QRCodeWriter)
  (:require [clojure.tools.logging :as log])
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:gen-class))

(defn qrcode [^bytes b ^long off ^long len]
  (let [text (apply str (map char b))
        writer (QRCodeWriter.)
        bitmatrix (.encode writer text BarcodeFormat/QR_CODE 200 200)
        image (MatrixToImageWriter/toBufferedImage bitmatrix)]
    (log/info text)
    (agif/image-to-bytes image)))