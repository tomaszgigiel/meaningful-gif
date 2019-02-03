(ns pl.tomaszgigiel.streams.QRCodeOutputStream
  (:import java.awt.Color)
  (:import java.awt.image.BufferedImage)
  (:import java.io.ByteArrayOutputStream)
  (:import java.io.OutputStream)
  (:import javax.imageio.ImageIO)
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:require [pl.tomaszgigiel.qrcode.qrcode :as qrcode])
  (:gen-class
    :name pl.tomaszgigiel.streams.QRCodeOutputStream
    :extends java.io.FilterOutputStream
    :state state
    :init init
    :constructors {[java.io.OutputStream] [java.io.OutputStream] [java.io.OutputStream long long] [java.io.OutputStream]}
    :exposes {out {:get getOut :set setOut}}
    :main false))

(defn -init
  ([^OutputStream out] [[out] (ref {:width 200 :height 200})])
  ([^OutputStream out ^long width ^long height] [[out] (ref {:width width :height height})]))

(defn- write-qrcode
  ([^OutputStream this ^bytes b]
    (write-qrcode this b 0 (count b)))
  ([^OutputStream this ^bytes b ^long off ^long len]
    (dosync
      (let [out (.getOut this)
            state (.state this)
            width (:width @state)
            height (:height @state)
            sub (take len (drop off b))
            text (apply str (map char sub))
            image (qrcode/qrcode text width height)
            image-bytes (agif/image-to-bytes image)
            image-bytes-count (count image-bytes)]
        (.write out image-bytes 0 image-bytes-count)))))

(defmulti -write
  (fn [& args]
    (apply vector (map class args))))

(defmethod -write [OutputStream Integer] [this b] (throw (UnsupportedOperationException. "Only bytes for a one QR Code")))
(defmethod -write [OutputStream  (class (byte-array 0))] [this b] (write-qrcode this b))
(defmethod -write [OutputStream  (class (byte-array 0)) Integer Integer] [this b off len] (write-qrcode this b off len))

(defn -flush [^OutputStream this] (.flush (.getOut this)))