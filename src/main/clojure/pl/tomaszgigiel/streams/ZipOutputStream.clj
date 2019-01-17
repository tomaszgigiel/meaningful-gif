(ns pl.tomaszgigiel.streams.ZipOutputStream
  (:gen-class
    :name pl.tomaszgigiel.streams.ZipOutputStream
    :extends java.util.zip.ZipOutputStream
    :main false
    :methods [[zipFolder [String] void]]))

(defn -zipFolder [this path-input]
  (doseq [entry (file-seq (clojure.java.io/file path-input)) :when (.isFile entry)]
    (.putNextEntry this (java.util.zip.ZipEntry. (.getPath entry)))
    (clojure.java.io/copy entry this)
    (flush)
    (.closeEntry this)))