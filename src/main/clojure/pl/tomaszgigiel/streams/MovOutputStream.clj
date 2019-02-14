(ns pl.tomaszgigiel.streams.MovOutputStream
  (:import java.io.FileOutputStream)
  (:require [pl.tomaszgigiel.mov.mov :as mov])
  (:gen-class
    :name pl.tomaszgigiel.streams.MovOutputStream
    :extends java.io.FilterOutputStream
    :state state
    :init init
    :constructors {[java.io.OutputStream] [java.io.OutputStream] [java.io.OutputStream long long] [java.io.OutputStream]}
    :exposes-methods {flush flushSuper}
    :main false))

(defn -init
  ([^FileOutputStream out] [[out] (ref {:image-writer (mov/begin out) :delay-time 300 :repeat-count -1})])
  ([^FileOutputStream out ^long delay-time ^long repeat-count] [[out] (ref {:encoder-channel (mov/begin out) :delay-time delay-time :repeat-count repeat-count})]))

(defn -write [^FileOutputStream this ^long b]
  (throw (UnsupportedOperationException. "Only bytes with a one gif picture")))

(defn -write [^FileOutputStream this ^bytes b ^long off ^long len]
  (dosync
    (let [state (.state this)
          encoder-channel (:encoder-channel @state)
          encoder (:encoder encoder-channel)
          delay-time (:delay-time @state)
          repeat-count (:repeat-count @state)]
      (mov/write-bytes b off len encoder delay-time repeat-count))))

(defn -flush [^FileOutputStream this] (.flushSuper this))

(defn -close [^FileOutputStream this]
  (dosync
    (let [state (.state this)
          encoder-channel (:encoder-channel @state)
          encoder (:encoder encoder-channel)
          channel (:channel encoder-channel)]
      (mov/end encoder channel))))
