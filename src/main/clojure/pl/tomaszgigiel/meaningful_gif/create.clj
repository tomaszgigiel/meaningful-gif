(ns pl.tomaszgigiel.meaningful-gif.create
  (:import java.io.BufferedOutputStream)
  (:import java.io.FileOutputStream)
  (:import pl.tomaszgigiel.streams.AGifOutputStream)
  (:import pl.tomaszgigiel.streams.Base64OutputStream)
  (:import pl.tomaszgigiel.streams.ChunkOutputStream)
  (:import pl.tomaszgigiel.streams.FountainCodeOutputStream)
  (:import pl.tomaszgigiel.streams.MovOutputStream)
  (:import pl.tomaszgigiel.streams.PipedInputStream)
  (:import pl.tomaszgigiel.streams.QRCodeOutputStream)
  (:import pl.tomaszgigiel.streams.SeriesFileOutputStream)
  (:import pl.tomaszgigiel.streams.ZipOutputStream)
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:gen-class))

(defn create-agif [input-path output-file chunk-size qrcode-width qrcode-height qrcode-delay-time qrcode-repeat-count]
  (with-open [s (-> output-file
                    FileOutputStream.
                    (AGifOutputStream. qrcode-delay-time qrcode-repeat-count)
                    (QRCodeOutputStream. qrcode-width qrcode-height)
                    (ChunkOutputStream. chunk-size)
                    FountainCodeOutputStream.
                    Base64OutputStream.
                    ZipOutputStream.)]
    (.zipFolder s input-path)))

(defn create-series [input-path output-path output-name chunk-size qrcode-width qrcode-height]
  (with-open [s (-> (SeriesFileOutputStream. output-path output-name)
                    (QRCodeOutputStream. qrcode-width qrcode-height)
                    (ChunkOutputStream. chunk-size)
                    FountainCodeOutputStream.
                    Base64OutputStream.
                    ZipOutputStream.)]
    (.zipFolder s input-path)))

(defn create-mov [input-path output-file chunk-size qrcode-width qrcode-height qrcode-delay-time qrcode-repeat-count]
  (with-open [s (-> output-file
                    FileOutputStream.
                    (MovOutputStream. qrcode-delay-time qrcode-repeat-count)
                    (QRCodeOutputStream. qrcode-width qrcode-height)
                    (ChunkOutputStream. chunk-size)
                    FountainCodeOutputStream.
                    Base64OutputStream.
                    ZipOutputStream.)]
    (.zipFolder s input-path)))

(defn create-zip [input-path output-file]
  (with-open [s (-> output-file
                    FileOutputStream.
                    BufferedOutputStream.
                    ZipOutputStream.)]
    (.zipFolder s input-path)))
