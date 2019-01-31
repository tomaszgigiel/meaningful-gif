(ns pl.tomaszgigiel.utils.misc
  (:import java.net.URI)
  (:import java.io.File)
  (:import java.io.PipedInputStream)
  (:import java.io.PipedOutputStream)
  (:require [clojure.java.io :as io])
  (:require [clojure.pprint :as pp])
  (:require [clojure.string :as string])
  (:gen-class))

;; https://stackoverflow.com/questions/7777882/loading-configuration-file-in-clojure-as-data-structure
(defn load-props
  [file-name]
  (with-open [^java.io.Reader reader (io/reader file-name)] 
    (let [props (java.util.Properties.)]
      (.load props reader)
      (into {} (for [[k v] props] [(keyword k) (read-string v)])))))

(defn replace-variable-environment
  [s]
  (string/replace s #"\$[A-Z]*" #(-> %1 rest string/join System/getenv)))

(defn pprint-is
  [^java.io.InputStream is]
  (let [bufsize 100
        buf (byte-array bufsize)]
    (loop [total-len 0]
      (let [n (.read is buf)]
        (cond
          (pos? n)(do
                    (pp/pprint (subvec (vec buf) 0 n))
                    (recur (+ total-len n)))
          :else total-len)))))

(defn print-is
  [^java.io.InputStream is]
  (let [bufsize 10000
        buf (byte-array bufsize)]
    (loop [total-len 0]
      (let [n (.read is buf)]
        (cond
          (pos? n)(do
                    (println (String. buf 0 n))
                    (recur (+ total-len n)))
          :else total-len)))))

;; https://ring-clojure.github.io/ring/ring.util.io.html
(defn piped-input-stream
  "Create an input stream from a function that takes an output stream as its
  argument. The function will be executed in a separate thread. The stream
  will be automatically closed after the function finishes.
  For example:
    (piped-input-stream
      (fn [ostream]
        (spit ostream \"Hello\")))"
  {:added "1.1"}
  [func]
  (let [input  (PipedInputStream.)
        output (PipedOutputStream.)]
    (.connect input output)
    (future
      (try
        (func output)
        (finally (.close output))))
    input))

;; https://stackoverflow.com/questions/15715546/clojure-how-to-ignore-exceptions-that-may-be-thrown-from-an-expression
(defmacro swallow-exceptions [& body]
  `(try ~@body (catch Exception e#)))

(defn file-from-resources [dir & nexts]
  (let [directory (io/resource dir)
        separator (System/getProperty "file.separator")
        parts (conj nexts directory)
        path (string/join separator parts)]
    (-> path URI. File.)))