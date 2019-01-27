(ns pl.tomaszgigiel.streams.QRCodeOutputStream
  (:import java.awt.Color)
  (:import java.awt.image.BufferedImage)
  (:import java.io.ByteArrayOutputStream)
  (:import java.io.OutputStream)
  (:import javax.imageio.ImageIO)
  (:require [pl.tomaszgigiel.qrcode.qrcode :as qrcode])
  (:gen-class
    :name pl.tomaszgigiel.streams.QRCodeOutputStream
    :extends java.io.FilterOutputStream
    :exposes {out {:get getOut :set setOut}}
    :exposes-methods {flush flushSuper write writeSuper}
    :main false))

(defn -write-int [^OutputStream this ^long b]
  (throw (UnsupportedOperationException. "Only bytes for a one QR Code")))

(defn -write-bytes [^OutputStream this ^bytes b]
  (let [image (qrcode/qrcode b 0 (count b))]
    (.write (.getOut this) image)))

(defn -write [^OutputStream this ^bytes b ^long off ^long len]
  (let [image (qrcode/qrcode b off len)]
    (.write (.getOut this) image 0 (count image))))

(defn -flush [^OutputStream this] (.flush (.getOut this)))