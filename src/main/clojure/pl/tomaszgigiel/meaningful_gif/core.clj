(ns pl.tomaszgigiel.meaningful-gif.core
  (:import java.io.BufferedOutputStream)
  (:import java.io.FileOutputStream)
  (:import pl.tomaszgigiel.streams.AGifOutputStream)
  (:import pl.tomaszgigiel.streams.Base64OutputStream)
  (:import pl.tomaszgigiel.streams.FountainCodeOutputStream)
  (:import pl.tomaszgigiel.streams.PipedInputStream)
  (:import pl.tomaszgigiel.streams.ZipOutputStream)
  (:require [clojure.tools.logging :as log])
  (:require [pl.tomaszgigiel.meaningful-gif.cmd :as cmd])
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:gen-class))

(defn meaningful-gif [file-name-props]
  (let [props (misc/load-props file-name-props)
        path-input (-> props :input misc/replace-variable-environment)
        path-output (-> props :output misc/replace-variable-environment)]
    (with-open [s (-> path-output
                    FileOutputStream.
                    BufferedOutputStream.
                    AGifOutputStream.
                    FountainCodeOutputStream.
                    Base64OutputStream.
                    ZipOutputStream.)]
      (.zipFolder s path-input))))

(defn -main [& args]
  "meaningful-gif: From files to animated GIF and back in Clojure"
  (let [{:keys [uri options exit-message ok?]} (cmd/validate-args args)]
    (if exit-message
      (cmd/exit (if ok? 0 1) exit-message)
      (meaningful-gif (first args))))
  (log/info "ok" )
  (shutdown-agents))