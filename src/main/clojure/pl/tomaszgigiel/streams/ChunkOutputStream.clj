(ns pl.tomaszgigiel.streams.ChunkOutputStream
  (:import java.io.OutputStream)
  (:gen-class
    :name pl.tomaszgigiel.streams.ChunkOutputStream
    :extends java.io.FilterOutputStream
    :state state
    :init init
    :constructors {[java.io.OutputStream] [java.io.OutputStream] [java.io.OutputStream long] [java.io.OutputStream]}
    :exposes {out {:get getOut :set setOut}}
    :main false))

(defn -init
  ([^OutputStream out] [[out] (ref {:buf (byte-array 100) :count 0 :size 100})])
  ([^OutputStream out ^long size] [[out] (ref {:buf (byte-array size) :count 0 :size size})]))

(defn- flush-buffer [out buf count]
  (if (pos? count) (.write out buf 0 count)))

(defn- write-byte [out buf count size b]
  (let [buf-full? (= count size)
        position (if buf-full? 0 count)]
    (if buf-full? (flush-buffer out buf size))
    (aset-byte buf position b)
    {:buf buf :count (+ position 1) :size size}))

(defn- write-array [out buf count size b off len]
  (loop [buf-full? (= count size)
         position (if buf-full? 0 count)
         chunk (if (<= (+ position len) size) len (- size position))
         off-current off
         len-new (- len chunk)]
    (if buf-full? (flush-buffer out buf size))
    (System/arraycopy b off-current buf position chunk)
    (if (pos? len-new)
      (recur (boolean true) 0 (min len-new size) (+ off-current chunk) (- len-new chunk))
      {:buf buf :count chunk :size size})))

(defn -write [^OutputStream this ^long b]
  (dosync
    (let [out (.getOut this)
          state (.state this)
          state-buf (:buf @state)
          state-count (:count @state)
          state-size (:size @state)
          result (write-byte out state-buf state-count state-size b)]
      (ref-set state result))))

(defn -write [^OutputStream this ^bytes b ^long off ^long len]
  (dosync 
    (let [out (.getOut this)
          state (.state this)
          state-buf (:buf @state)
          state-count (:count @state)
          state-size (:size @state)
          result (write-array out state-buf state-count state-size b off len)]
      (ref-set state result))))

(defn -flush [^OutputStream this]
  (dosync
    (let [state (.state this)
          state-buf (:buf @state)
          state-count (:count @state)
          state-size (:size @state)
          out (.getOut this)]
      (flush-buffer out state-buf state-count)
      (ref-set state {:buf state-buf :count 0 :size state-size}))))