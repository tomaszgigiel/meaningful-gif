(ns pl.tomaszgigiel.mov.mov
  (:import java.io.FileOutputStream)
  (:import org.jcodec.common.io.SeekableByteChannel)
  (:import org.jcodec.api.awt.AWTSequenceEncoder)
  (:import org.jcodec.api.FrameGrab)
  (:import org.jcodec.api.SequenceEncoder)
  (:import org.jcodec.common.io.NIOUtils)
  (:import org.jcodec.common.model.Rational)
  (:import org.jcodec.scale.AWTUtil)
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:require [pl.tomaszgigiel.channel.channel :as channel])
  (:gen-class))
        
(defn images-from-mov [file]
  (let [grab (-> file NIOUtils/readableChannel FrameGrab/createFrameGrab)
        frames (repeatedly #(.getNativeFrame grab))]
    (map #(AWTUtil/toBufferedImage %) (take-while some? frames))))

(defn begin [^FileOutputStream fos]
  (let [out (channel/new fos)
        encoder (AWTSequenceEncoder. out (Rational/R 25 1))]
    {:encoder encoder :channel out}))

(defn write-bytes [b off len encoder delay-time repeat-count]
  (.encodeImage encoder (agif/bytes-to-image b off len)))

(defn end [^AWTSequenceEncoder encoder ^SeekableByteChannel channel]
  (do
    (.finish encoder)
    (NIOUtils/closeQuietly channel)))

(defn create-mov-from-images [fos fs delay-time repeat-count]
  (with-open [out (channel/new fos)]
    (let [encoder (AWTSequenceEncoder. out (Rational/R 25 1))]
      (doseq [f fs] (.encodeImage encoder f))
      (.finish encoder))))
