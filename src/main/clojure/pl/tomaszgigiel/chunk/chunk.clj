(ns pl.tomaszgigiel.chunk.chunk
  (:gen-class
    :main false))

(defrecord Chunk [buf chunk-count header-size chunk-size buf-position buf-size])

(defn- header [chunk-count] (.getBytes (format "%xz" chunk-count)))

(defn- header-size [header] (count header))

(defn- buf-size [header-size chunk-size] (+ header-size chunk-size))

(defn- free-space [chunk] (- (:buf-size chunk) (:buf-position chunk)))

(defn- chunk-count [buf] (Long/parseLong (apply str (take-while #{\1 \2 \3 \4 \5 \6 \7 \8 \9 \0 \a \b \c \d \e \f} buf)) 16))

(defn- updated [chunk b off len]
  (let [buf (:buf chunk)
        buf-position (:buf-position chunk)
        buf-position-new (+ buf-position len)]
    (cond
      (= len 1) (aset-byte buf buf-position (first (drop off b)))
      (> len 1) (System/arraycopy b off buf buf-position len))
    (assoc chunk :buf-position buf-position-new)))

(defn full? [chunk] (= (:buf-position chunk) (:buf-size chunk)))

(defn chunk-some? [chunk] (> (:buf-position chunk) (:header-size chunk)))

(defn cmp [a b] (compare (:chunk-count a) (:chunk-count b)))

(defn chunk-str
  ([chunk] (chunk-str chunk (:buf-position chunk)))
  ([chunk n] (->> chunk :buf (take (min n (:buf-position chunk))) (map char) (apply str))))

(defn cargo [chunk] (->> chunk :buf (drop (:header-size chunk)) (take (:buf-position chunk))))

(defn wrap [buf]
  (let [chunk-count (chunk-count buf)
        header (header chunk-count)
        header-size (header-size header)
        buf-size (count buf)
        chunk-size (- buf-size header-size)
        buf-position buf-size]
    (Chunk. buf chunk-count header-size chunk-size buf-position buf-size)))

(defn new [chunk-size]
  (let [chunk-count 1
        header (header chunk-count)
        header-size (header-size header)
        buf-position header-size
        buf-size (buf-size header-size chunk-size)
        buf (byte-array buf-size header)]
    (Chunk. buf chunk-count header-size chunk-size buf-position buf-size)))

(defn successor [chunk]
  (let [chunk-count (inc (:chunk-count chunk))
        header (header chunk-count)
        header-size (header-size header)
        chunk-size (:chunk-size chunk)
        buf-position header-size
        buf-size (buf-size header-size chunk-size)
        buf (byte-array buf-size header)]
    (Chunk. buf chunk-count header-size chunk-size buf-position buf-size)))

(defn series [chunk b off len]
  (when (pos? len)
    (let [cargo (min (free-space chunk) len)
                 off-new (+ off cargo)
                 len-new (- len cargo)]
      (lazy-seq (cons (updated chunk b off cargo) (series (successor chunk) b off-new len-new))))))

(defmethod clojure.core/print-method pl.tomaszgigiel.chunk.chunk.Chunk [chunk writer]
  (.write writer
    (str "#pl.tomaszgigiel.chunk.chunk.Chunk {"
         " :buf = " (chunk-str chunk 5)
         " :chunk-count = " (:chunk-count chunk)
         " :header-size = " (:header-size chunk)
         " :chunk-size = " (:chunk-size chunk)
         " :buf-position = " (:buf-position chunk)
         " :buf-size = " (:buf-size chunk)
         " }")))
