(ns pl.tomaszgigiel.meaningful-gif.extract
  (:import java.io.File)
  (:import java.util.Base64)
  (:require [clojure.java.io :as io])
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:require [pl.tomaszgigiel.chunk.chunk :as chunk])
  (:require [pl.tomaszgigiel.mov.mov :as mov])
  (:require [pl.tomaszgigiel.qrcode.qrcode :as qrcode])
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:gen-class))

(defn- image-to-data [image] (-> image agif/black-white qrcode/text misc/swallow-exceptions))

(defn- images-to-zip [images]
  (->> images (map image-to-data)
    distinct
    (map chunk/wrap)
    (sort chunk/cmp)
    (map chunk/cargo)
    flatten
    (map char)
    (apply str)
    (#(clojure.string/replace % #"\r\n" ""))
    (.decode (Base64/getDecoder))))

(defn- save [input output-file]
  (with-open [out (io/output-stream output-file)]
    (.write out input)))

(defn extract-agif
  ([file] (-> file agif/images-from-agif images-to-zip))
  ([input-file output-file] (save (-> input-file File. extract-agif) output-file)))

(defn extract-series
  ([directory] (-> directory agif/images-from-directory images-to-zip))
  ([input-directory output-file] (save (-> input-directory File. extract-series) output-file)))

(defn extract-mov
  ([file] (-> file mov/images-from-mov images-to-zip))
  ([input-file output-file] (save (-> input-file File. extract-mov) output-file)))
