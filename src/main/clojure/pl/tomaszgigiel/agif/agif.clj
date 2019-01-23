(ns pl.tomaszgigiel.agif.agif
  (:import java.awt.Color)
  (:import java.awt.Graphics)
  (:import java.awt.Image)
  (:import java.awt.image.BufferedImage)
  (:import java.io.BufferedOutputStream)
  (:import java.io.ByteArrayInputStream)
  (:import java.io.ByteArrayOutputStream)
  (:import java.io.OutputStream)
  (:import java.io.PipedInputStream)
  (:import java.io.PipedOutputStream)
  (:import java.nio.ByteBuffer)
  (:import java.nio.ByteOrder)
  (:import java.util.zip.ZipEntry)
  (:import java.util.zip.ZipOutputStream)
  (:import javax.imageio.ImageIO)
  (:import javax.imageio.stream.ImageOutputStream)
  (:import javax.imageio.ImageWriteParam)
  (:import javax.imageio.ImageWriter)
  (:import javax.imageio.ImageTypeSpecifier)
  (:import javax.imageio.metadata.IIOMetadata)
  (:import javax.imageio.metadata.IIOMetadataNode)
  (:import javax.imageio.IIOImage)  
  (:import org.apache.commons.io.IOUtils)
  (:require [clojure.java.io :as io])
  (:require [clojure.string])
  (:require [clojure.pprint])
  (:gen-class))

(defn- first-node [root & tags]
  (first (remove nil? (map (fn [tag] (let [nodes (.getElementsByTagName root tag)] (if (pos? (.getLength nodes)) (.item nodes 0)))) tags))))

(defn- get-application-extensions [root]
  (first-node root "ApplicationExtensions"))

(defn- create-application-extensions [root]
  (let [ae (IIOMetadataNode. "ApplicationExtensions")
        reference (first-node root "CommentExtensions")]
    (.insertBefore root ae reference)
    ae))

(defn- set-repeat-count [n root]
  (let [b (->  (ByteBuffer/allocate 3) (.order ByteOrder/LITTLE_ENDIAN) (.put (byte 1)) (.putShort (short n)) (.array)) ; sub-block index (always 1)
        ae (IIOMetadataNode. "ApplicationExtension")
        aes (or (get-application-extensions root)(create-application-extensions root))]
    (.setAttribute ae "applicationID" "NETSCAPE")
    (.setAttribute ae "authenticationCode" "2.0")
    (.setUserObject ae b)
    (.appendChild aes ae)))

(defn- get-graphic-control-extension [root]
  (first-node root "GraphicControlExtension"))

(defn- create-graphic-control-extension [root]
  (let [gce (IIOMetadataNode. "GraphicControlExtension")
        reference (first-node root "PlainTextExtension" "ApplicationExtensions" "CommentExtensions")]
    (.insertBefore root gce reference)
    gce))

(defn- set-delay-time [d node]
  (let [gce (or (get-graphic-control-extension node)(create-graphic-control-extension node))]
      (.setAttribute gce "delayTime" (str (/ d 10)))))

(defn- write-image [^BufferedImage im ^ImageWriter wr ^long d ^long n]
  (let [param (.getDefaultWriteParam wr)
        type (ImageTypeSpecifier/createFromRenderedImage im)
        metadata (.getDefaultImageMetadata wr type param)
        format (.getNativeMetadataFormatName metadata)
        tree (.getAsTree metadata format)]
    (set-repeat-count n tree)
    (set-delay-time d tree)
    (.setFromTree metadata format tree)
    (.writeToSequence wr (IIOImage. im nil metadata) nil)))

(defn begin [^OutputStream os]
  (let [image-output-stream (ImageIO/createImageOutputStream os)
        image-writer (->> "image/gif" ImageIO/getImageWritersByMIMEType .next)]
    (.setOutput image-writer image-output-stream)
    (.prepareWriteSequence image-writer nil)
    image-writer))

(defn write-bytes [b off len image-writer delay-time repeat-count]
  (let [param (.getDefaultWriteParam image-writer)
        im (-> (ByteArrayInputStream. b off len) ImageIO/read)
        type (ImageTypeSpecifier/createFromRenderedImage im)
        metadata (.getDefaultImageMetadata image-writer type param)
        format (.getNativeMetadataFormatName metadata)
        tree (.getAsTree metadata format)]
    (set-delay-time delay-time tree)
    (set-repeat-count repeat-count tree)
    (.setFromTree metadata format tree)
    (.writeToSequence image-writer (IIOImage. im nil metadata) nil)))

(defn end [^ImageWriter wr]
  (let [image-output-stream (.getOutput wr)]
    (.endWriteSequence wr)
    (.dispose wr)
    (.close image-output-stream)))

(defn create-agif [os fs delay-time repeat-count]
  (with-open [ios (ImageIO/createImageOutputStream os)]
    (let [wr (->> "image/gif" ImageIO/getImageWritersByMIMEType .next)]
      (.setOutput wr ios)
      (.prepareWriteSequence wr nil)
      (doseq [f fs] (write-image f wr delay-time repeat-count))
      (.endWriteSequence wr)
      (.dispose wr))))

(defn test-images []
  (let [a (BufferedImage. 100 100 BufferedImage/TYPE_INT_RGB)
        b (BufferedImage. 100 100 BufferedImage/TYPE_INT_RGB)]
    (doto (.getGraphics a) (.setColor Color/RED) (.fillRect 10 10 80 80))
    (doto (.getGraphics b) (.setColor Color/GREEN) (.fillRect 10 10 80 80))
    [a b]))

(defn image-to-bytes [^BufferedImage buffered-image]
  (with-open [baos (ByteArrayOutputStream.)]
    (ImageIO/write buffered-image "gif" baos)
    (.toByteArray baos)))

(defn test-bytes []
  (map image-to-bytes (test-images)))