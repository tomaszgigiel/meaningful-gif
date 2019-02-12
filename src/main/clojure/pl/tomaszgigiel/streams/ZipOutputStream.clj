(ns pl.tomaszgigiel.streams.ZipOutputStream
  (:import java.io.File)
  (:import java.io.OutputStream)
  (:import java.net.URI)
  (:import java.net.URL)
  (:import java.util.zip.ZipEntry)
  (:require [clojure.java.io :as io])
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:gen-class
    :name pl.tomaszgigiel.streams.ZipOutputStream
    :extends java.util.zip.ZipOutputStream
    :main false
    :methods
    [[zipFolder [String] void]
     [zipFolder [java.net.URI] void]
     [zipFolder [java.net.URL] void]
     [zipFolder [java.io.File] void]]))

(defn- zip-folder [this file]
  (doseq [entry (file-seq file) :when (.isFile entry)]
    (.putNextEntry this (ZipEntry. (misc/from-common-directory (.getPath file) (.getPath entry))))
    (io/copy entry this)
    (.closeEntry this)))

(defmulti -zipFolder
  (fn [& args]
    (apply vector (map class args))))

(defmethod -zipFolder [OutputStream String] [this path] (zip-folder this (io/file path)))
(defmethod -zipFolder [OutputStream URI] [this uri] (zip-folder this (io/file uri)))
(defmethod -zipFolder [OutputStream URL] [this url] (zip-folder this (io/file url)))
(defmethod -zipFolder [OutputStream File] [this file] (zip-folder this file))
