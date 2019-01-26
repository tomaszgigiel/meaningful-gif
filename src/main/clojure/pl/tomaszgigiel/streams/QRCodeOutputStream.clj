(ns pl.tomaszgigiel.streams.QRCodeOutputStream
  (:import java.awt.Color)
  (:import java.awt.image.BufferedImage)
  (:import java.io.ByteArrayOutputStream)
  (:import java.io.OutputStream)
  (:import javax.imageio.ImageIO)
  (:gen-class
    :name pl.tomaszgigiel.streams.QRCodeOutputStream
    :extends java.io.FilterOutputStream
    :exposes {out {:get getOut :set setOut}}
    :exposes-methods {flush flushSuper write writeSuper}
    :main false))

(defn- qr-code [^bytes b ^long off ^long len]
  (let [image (BufferedImage. 100 100 BufferedImage/TYPE_INT_RGB)
        color (if (> len 90) Color/RED Color/GREEN)]
    (doto (.getGraphics image) (.setColor color) (.fillRect 5 5 90 90))
    (with-open [baos (ByteArrayOutputStream.)]
      (ImageIO/write image "gif" baos)
      (.flush baos)
      (.toByteArray baos))))

(defn -write-int [^OutputStream this ^long b]
  (throw (UnsupportedOperationException. "Only bytes for a one QR Code")))

(defn -write-bytes [^OutputStream this ^bytes b]
  (let [image (qr-code b 0 (count b))]
    (.write (.getOut this) image)))

(defn -write [^OutputStream this ^bytes b ^long off ^long len]
  (let [image (qr-code b off len)]
    (.write (.getOut this) image 0 (count image))))

(defn -flush [^OutputStream this] (.flush (.getOut this)))