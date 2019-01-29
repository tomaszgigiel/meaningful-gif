(ns pl.tomaszgigiel.meaningful-gif.create.core
  (:import java.io.BufferedOutputStream)
  (:import java.io.FileOutputStream)
  (:import pl.tomaszgigiel.streams.AGifOutputStream)
  (:import pl.tomaszgigiel.streams.Base64OutputStream)
  (:import pl.tomaszgigiel.streams.ChunkOutputStream)
  (:import pl.tomaszgigiel.streams.FountainCodeOutputStream)
  (:import pl.tomaszgigiel.streams.PipedInputStream)
  (:import pl.tomaszgigiel.streams.QRCodeOutputStream)
  (:import pl.tomaszgigiel.streams.ZipOutputStream)
  (:require [clojure.tools.logging :as log])
  (:require [pl.tomaszgigiel.meaningful-gif.create.cmd :as cmd])
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:gen-class))

(defn- meaningful-gif [file-name-props]
  (let [props (misc/load-props file-name-props)
        input-path (-> props :create-input-path misc/replace-variable-environment)
        output-file (-> props :create-output-file misc/replace-variable-environment)
        chunk-size (Integer/parseInt (-> props :chunk-size misc/replace-variable-environment))
        qrcode-delay-time (Integer/parseInt (-> props :qrcode.delay-time misc/replace-variable-environment))
        qrcode-repeat-count (Integer/parseInt (-> props :qrcode.repeat-count misc/replace-variable-environment))
        qrcode-width (Integer/parseInt (-> props :qrcode.width misc/replace-variable-environment))
        qrcode-height (Integer/parseInt (-> props :qrcode.height misc/replace-variable-environment))]
    (with-open [s (-> output-file
                    FileOutputStream.
                    BufferedOutputStream.
                    (AGifOutputStream. qrcode-delay-time qrcode-repeat-count)
                    (QRCodeOutputStream. qrcode-width qrcode-height)
                    (ChunkOutputStream. chunk-size)
                    FountainCodeOutputStream.
                    Base64OutputStream.
                    ZipOutputStream.)]
      (.zipFolder s input-path))))

(defn -main [& args]
  "meaningful-gif: From files to animated GIF and back in Clojure: create agif"
  (let [{:keys [uri options exit-message ok?]} (cmd/validate-args args)]
    (if exit-message
      (cmd/exit (if ok? 0 1) exit-message)
      (meaningful-gif (first args))))
  (log/info "ok")
  (shutdown-agents))