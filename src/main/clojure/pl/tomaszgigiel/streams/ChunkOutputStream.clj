(ns pl.tomaszgigiel.streams.ChunkOutputStream
  (:import java.io.OutputStream)
  (:require [pl.tomaszgigiel.chunk.chunk :as chunk])
  (:gen-class
    :name pl.tomaszgigiel.streams.ChunkOutputStream
    :extends java.io.FilterOutputStream
    :state state
    :init init
    :constructors {[java.io.OutputStream] [java.io.OutputStream] [java.io.OutputStream long] [java.io.OutputStream]}
    :exposes {out {:get getOut :set setOut}}
    :main false))

(defn -init
  ([^OutputStream out] [[out] (ref (chunk/new 100))])
  ([^OutputStream out ^long size] [[out] (ref (chunk/new size))]))

(defn- flush-buffer [out chunk]
  (when (chunk/chunk-some? chunk) (.write out (:buf chunk) 0 (:buf-position chunk))))

(defn- write-chunk [out chunk b off len]
  (reduce
    #(if (some? %2) (do (flush-buffer out %1) %2) %1)
    (chunk/series chunk b off len)))

(defn -write [^OutputStream this ^long b]
  (dosync
    (let [out (.getOut this)
          state @(.state this)
          result (write-chunk out state (byte-array 1 [(byte b)]) 0 1)]
      (ref-set state result))))

(defn -write [^OutputStream this ^bytes b ^long off ^long len]
  (dosync 
    (let [out (.getOut this)
          state (.state this)
          chunk @state
          result (write-chunk out chunk b off len)]
      (ref-set state result))))

(defn -flush [^OutputStream this]
  (dosync
    (let [out (.getOut this)
          state (.state this)
          chunk @state]
      (flush-buffer out chunk)
      (ref-set state (chunk/successor chunk)))))
