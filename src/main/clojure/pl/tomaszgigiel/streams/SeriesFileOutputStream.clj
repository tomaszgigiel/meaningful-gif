(ns pl.tomaszgigiel.streams.SeriesFileOutputStream
  (:import java.io.OutputStream)
  (:require [clojure.java.io :as io])
  (:gen-class
    :name pl.tomaszgigiel.streams.SeriesFileOutputStream
    :extends java.io.OutputStream
    :state state
    :init init
    :constructors {[String String] []}
    :main false))

(defn -init
  ([^String output-path ^String output-name] [[] (ref {:output-path output-path :output-name output-name :count 1})]))

(defn- write-file
  ([^OutputStream this ^bytes b]
    (write-file this b 0 (count b)))
  ([^OutputStream this ^bytes b ^long off ^long len]
    (dosync
      (let [state (.state this)
            output-path (:output-path @state)
            output-name (:output-name @state)
            count (:count @state)
            separator (System/getProperty "file.separator")
            path (str output-path separator output-name "." (format "%03d" count) ".gif")]
        (with-open [os (io/output-stream path)]
          (.write os b off len))
        (ref-set state {:output-path output-path :output-name output-name :count (inc count)})))))

(defmulti -write
  (fn [& args] 
    (apply vector (map class args))))

(defmethod -write [OutputStream Integer] [this b] (throw (UnsupportedOperationException. "Only bytes for a one file"))) 
(defmethod -write [OutputStream  (class (byte-array 0))] [this b] (write-file this b)) 
(defmethod -write [OutputStream  (class (byte-array 0)) Integer Integer] [this b off len] (write-file this b off len))