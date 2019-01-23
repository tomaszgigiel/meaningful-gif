(ns pl.tomaszgigiel.streams.AGifOutputStream
  (:import java.io.OutputStream)
  (:require [pl.tomaszgigiel.agif.agif :as agif])
  (:gen-class
    :name pl.tomaszgigiel.streams.AGifOutputStream
    :extends java.io.FilterOutputStream
    :state state
    :init init
    :constructors {[java.io.OutputStream] [java.io.OutputStream]}
    :exposes-methods {flush flushSuper}
    :main false))

(defn -init
  ([^OutputStream out] [[out] (ref {:image-writer (agif/begin out)})]))

(defn -write [^OutputStream this ^long b]
  (throw (UnsupportedOperationException. "Only bytes with a one gif picture")))

(defn -write [^OutputStream this ^bytes b ^long off ^long len]
  (dosync (agif/write-bytes b off len (:image-writer @(.state this)) 300 10)))

(defn -flush [^OutputStream this] (.flushSuper this))

(defn -close [^OutputStream this] (dosync (agif/end (:image-writer @(.state this)))))