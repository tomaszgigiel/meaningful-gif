(ns pl.tomaszgigiel.meaningful-gif.extract
  (:import java.io.File)
  (:import org.jcodec.api.FrameGrab)
  (:import org.jcodec.common.io.NIOUtils)
  (:import org.jcodec.scale.AWTUtil)
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:require [pl.tomaszgigiel.qrcode.qrcode :as qrcode])
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:gen-class))

(defn- do-something [buffered-image]
  (-> buffered-image agif/black-white qrcode/text println misc/swallow-exceptions))

(defn extract-mov [input-file output-file]
  (let [grab (-> input-file File. NIOUtils/readableChannel FrameGrab/createFrameGrab)]
    (loop [frame (.getNativeFrame grab)]
      (when (some? frame)
        (-> frame AWTUtil/toBufferedImage do-something)
        (recur (.getNativeFrame grab))))))

(defn extract-agif [input-file output-file]
  (doseq [buffered-image (-> input-file File. agif/images-from-agif)]
    (do-something buffered-image)))

(defn extract-series [input-path output-file]
  (doseq [buffered-image (-> input-path File. agif/images-from-directory)]
    (do-something buffered-image)))