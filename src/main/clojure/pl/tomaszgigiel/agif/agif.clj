(ns pl.tomaszgigiel.agif.agif
  (:import java.awt.Color)
  (:import java.awt.Graphics)
  (:import java.awt.Image)
  (:import java.awt.image.BufferedImage)
  (:import java.io.ByteArrayInputStream)
  (:import java.io.ByteArrayOutputStream)
  (:import java.io.OutputStream)
  (:import java.nio.ByteBuffer)
  (:import java.nio.ByteOrder)
  (:import javax.imageio.ImageIO)
  (:import javax.imageio.ImageWriter)
  (:import javax.imageio.ImageTypeSpecifier)
  (:import javax.imageio.metadata.IIOMetadataNode)
  (:import javax.imageio.IIOImage)  
  (:gen-class))

(defn image-to-bytes [^BufferedImage buffered-image]
  (with-open [baos (ByteArrayOutputStream.)]
    (ImageIO/write buffered-image "gif" baos)
    (.flush baos)
    (.toByteArray baos)))

; using (ImageIO/read bais) not working,
; see: /meaningful-gif/src/test/clojure/pl/tomaszgigiel/agif/agif_bytes_to_image_test.clj
(defn- byte-array-input-stream [bais]
  (let [buffered-image-pre (ImageIO/read bais)
        buffered-image (BufferedImage. (.getWidth buffered-image-pre) (.getHeight buffered-image-pre) (BufferedImage/TYPE_INT_RGB))]
    (doto (.getGraphics buffered-image) (.drawImage buffered-image-pre 0 0 nil) (.dispose))
    buffered-image))

(defn bytes-to-image
  ([^bytes bytes] (with-open [bais (ByteArrayInputStream. bytes)] (byte-array-input-stream bais)))
  ([^bytes bytes ^long off ^long len] (with-open [bais (ByteArrayInputStream. bytes off len)] (byte-array-input-stream bais))))

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

(defn write-image [^BufferedImage buffered-image ^ImageWriter image-writer ^long delay-time ^long repeat-count]
  (let [param (.getDefaultWriteParam image-writer)
        type (ImageTypeSpecifier/createFromRenderedImage buffered-image)
        metadata (.getDefaultImageMetadata image-writer type param)
        format (.getNativeMetadataFormatName metadata)
        tree (.getAsTree metadata format)]
    (set-repeat-count repeat-count tree)
    (set-delay-time delay-time tree)
    (.setFromTree metadata format tree)
    (.writeToSequence image-writer (IIOImage. buffered-image nil metadata) nil)))

(defn write-bytes [b off len image-writer delay-time repeat-count]
  ;;;(write-image (bytes-to-image b off len) image-writer delay-time repeat-count))
  (write-image (bytes-to-image b) image-writer delay-time repeat-count))

(defn begin [^OutputStream os]
  (let [image-output-stream (ImageIO/createImageOutputStream os)
        image-writer (->> "image/gif" ImageIO/getImageWritersByMIMEType .next)]
    (.setOutput image-writer image-output-stream)
    (.prepareWriteSequence image-writer nil)
    image-writer))

(defn end [^ImageWriter wr]
  (let [image-output-stream (.getOutput wr)]
    (.endWriteSequence wr)
    (.dispose wr)
    (.close image-output-stream)))

(defn create-agif-from-images [os fs delay-time repeat-count]
  (with-open [ios (ImageIO/createImageOutputStream os)]
    (let [wr (->> "image/gif" ImageIO/getImageWritersByMIMEType .next)]
      (.setOutput wr ios)
      (.prepareWriteSequence wr nil)
      (doseq [f fs] (write-image f wr delay-time repeat-count))
      (.endWriteSequence wr)
      (.dispose wr))))