(ns pl.tomaszgigiel.meaningful-gif.extract.core
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
  (:require [pl.tomaszgigiel.meaningful-gif.extract.cmd :as cmd])
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:gen-class))

(defn- meaningful-gif [file-name-props]
  (let [props (misc/load-props file-name-props)
        input-file (-> props :extract-input-file misc/replace-variable-environment)
        output-file (-> props :extract-output-file misc/replace-variable-environment)]
    (log/info "input-file = " input-file)
    (log/info "output-file = " output-file)))

(defn -main [& args]
  "meaningful-gif: From files to animated GIF and back in Clojure: extract agif"
  (let [{:keys [uri options exit-message ok?]} (cmd/validate-args args)]
    (if exit-message
      (cmd/exit (if ok? 0 1) exit-message)
      (meaningful-gif (first args))))
  (log/info "ok")
  (shutdown-agents))