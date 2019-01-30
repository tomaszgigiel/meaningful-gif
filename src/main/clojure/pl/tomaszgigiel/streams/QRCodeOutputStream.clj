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
    :exposes-methods {flush flushSuper write writeSuper}
    :main false))

(defn -init
  ([^OutputStream out] [[out] (ref {:width 200 :height 200})])
  ([^OutputStream out ^long width ^long height] [[out] (ref {:width width :height height})]))

(defn -write-int [^OutputStream this ^long b]
  (throw (UnsupportedOperationException. "Only bytes for a one QR Code")))

(defn -write [^OutputStream this ^bytes b ^long off ^long len]
  (dosync
    (let [out (.getOut this)
          state (.state this)
          width (:width @state)
          height (:height @state)
          sub (take len (drop off b))
          text (apply str (map char sub))
          image (qrcode/qrcode text width height)
          image-bytes (agif/image-to-bytes image)]
      (.write out image-bytes))))

(defn -write-bytes [^OutputStream this ^bytes b]
  (-write this b 0 (count b)))

(defn -flush [^OutputStream this] (.flush (.getOut this)))