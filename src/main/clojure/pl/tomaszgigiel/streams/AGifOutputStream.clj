(ns pl.tomaszgigiel.streams.AGifOutputStream
  (:import java.io.OutputStream)
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:gen-class
    :name pl.tomaszgigiel.streams.AGifOutputStream
    :extends java.io.FilterOutputStream
    :state state
    :init init
    :constructors {[java.io.OutputStream] [java.io.OutputStream] [java.io.OutputStream long long] [java.io.OutputStream]}
    :exposes-methods {flush flushSuper}
    :main false))

(defn -init
  ([^OutputStream out] [[out] (ref {:image-writer (agif/begin out) :delay-time 300 :repeat-count -1})])
  ([^OutputStream out ^long delay-time ^long repeat-count] [[out] (ref {:image-writer (agif/begin out) :delay-time delay-time :repeat-count repeat-count})]))

(defn -write [^OutputStream this ^long b]
  (throw (UnsupportedOperationException. "Only bytes with a one gif picture")))

(defn -write [^OutputStream this ^bytes b ^long off ^long len]
  (dosync
    (let [state (.state this)
          image-writer (:image-writer @state)
          delay-time (:delay-time @state)
          repeat-count (:repeat-count @state)]
      (agif/write-bytes b off len image-writer delay-time repeat-count))))

(defn -flush [^OutputStream this] (.flushSuper this))

(defn -close [^OutputStream this] (dosync (agif/end (:image-writer @(.state this)))))