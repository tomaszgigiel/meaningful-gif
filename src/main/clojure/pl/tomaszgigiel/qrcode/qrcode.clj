(ns pl.tomaszgigiel.qrcode.qrcode
  (:import com.google.zxing.BarcodeFormat)
  (:import com.google.zxing.BinaryBitmap)
  (:import com.google.zxing.client.j2se.BufferedImageLuminanceSource)
  (:import com.google.zxing.client.j2se.MatrixToImageWriter)
  (:import com.google.zxing.common.HybridBinarizer)
  (:import com.google.zxing.MultiFormatReader)
  (:import com.google.zxing.qrcode.QRCodeWriter)
  (:require [clojure.tools.logging :as log])
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:gen-class))

(defn qrcode [text width height]
  (let [writer (QRCodeWriter.)
        bitmatrix (.encode writer text BarcodeFormat/QR_CODE width height)
        image (MatrixToImageWriter/toBufferedImage bitmatrix)]
    image))

(defn text [buffered-image]
  (let [source (BufferedImageLuminanceSource. buffered-image)
        binarizer (HybridBinarizer. source)
        bitmap (BinaryBitmap. binarizer)
        reader (MultiFormatReader.)
        result (.decode reader bitmap)
        text (.getText result)]
    text))