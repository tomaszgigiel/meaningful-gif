(ns pl.tomaszgigiel.streams.ChunkOutputStream
  (:import java.io.OutputStream)
  (:import java.nio.charset.StandardCharsets)
  (:gen-class
    :name pl.tomaszgigiel.streams.ChunkOutputStream
    :extends java.io.FilterOutputStream
    :state state
    :init init
    :constructors {[java.io.OutputStream] [java.io.OutputStream] [java.io.OutputStream long] [java.io.OutputStream]}
    :exposes {out {:get getOut :set setOut}}
    :methods [^:static [headerSize [] long]]
    :main false))

(def header-size 16)
(defn -headerSize [] header-size)

(defn- header [chunk-count]
  (let [h (-> chunk-count Long/toHexString (.getBytes StandardCharsets/UTF_8))]
    (byte-array header-size h)))

(defn- new-chunk [chunk-size]
  (let [chunk-count-new 1
        header (header chunk-count-new)
        buf-size-new (+ chunk-size header-size)
        buf-new (byte-array buf-size-new header)]
    {:buf buf-new :chunk-count chunk-count-new :buf-position header-size :buf-size buf-size-new}))

(defn -init
  ([^OutputStream out] [[out] (ref (new-chunk 100))])
  ([^OutputStream out ^long size] [[out] (ref (new-chunk size))]))

(defn- flush-buffer [out buf buf-position] (when (> buf-position header-size) (.write out buf 0 buf-position)))

(defn- write-byte [out buf chunk-count buf-position buf-size b]
  (let [buf-full? (= buf-position buf-size)
        buf-position-new (if buf-full? header-size buf-position)
        chunk-count-new (if buf-full? (inc chunk-count) chunk-count)]
    (when buf-full?
      (flush-buffer out buf buf-size)
      (System/arraycopy (header chunk-count-new) 0 buf 0 header-size)
      )
    (aset-byte buf buf-position-new b)
    {:buf buf :chunk-count chunk-count-new :buf-position (inc buf-position-new) :buf-size buf-size}))

(defn- write-array [out buf chunk-count buf-position buf-size b off len]
  (loop [buf-full? (= buf-position buf-size)
         buf-position-new (if buf-full? header-size buf-position)
         chunk-size-current (if (<= (+ buf-position len) (- buf-size header-size)) len (- buf-size buf-position))
         off-current off
         len-new (- len chunk-size-current)
         chunk-count-current chunk-count]
    (when buf-full?
      (flush-buffer out buf buf-size)
      (System/arraycopy (header chunk-count-current) 0 buf 0 header-size))
    (System/arraycopy b off-current buf buf-position-new chunk-size-current)
    (if (pos? len-new)
      (recur (boolean true)
             header-size
             (min len-new (- buf-size header-size))
             (+ off-current chunk-size-current)
             (- len-new chunk-size-current)
             (inc chunk-count-current))
      {:buf buf :chunk-count chunk-count-current :buf-position (+ buf-position-new chunk-size-current) :buf-size buf-size})))

(defn -write [^OutputStream this ^long b]
  (dosync
    (let [out (.getOut this)
          state (.state this)
          state-buf (:buf @state)
          state-chunk-count (:chunk-count @state)
          state-buf-position (:buf-position @state)
          state-buf-size (:buf-size @state)
          result (write-byte out state-buf state-chunk-count state-buf-position state-buf-size b)]
      (ref-set state result))))

(defn -write [^OutputStream this ^bytes b ^long off ^long len]
  (dosync 
    (let [out (.getOut this)
          state (.state this)
          state-buf (:buf @state)
          state-chunk-count (:chunk-count @state)
          state-buf-position (:buf-position @state)
          state-buf-size (:buf-size @state)
          result (write-array out state-buf state-chunk-count state-buf-position state-buf-size b off len)]
      (ref-set state result))))

(defn -flush [^OutputStream this]
  (dosync
    (let [out (.getOut this)
          state (.state this)
          state-buf (:buf @state)
          state-chunk-count (:chunk-count @state)
          state-buf-position (:buf-position @state)
          state-buf-size (:buf-size @state)]
      (flush-buffer out state-buf state-buf-position)
      (ref-set state {:buf state-buf :chunk-count (inc state-chunk-count) :buf-position header-size :buf-size state-buf-size}))))