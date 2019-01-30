(ns pl.tomaszgigiel.meaningful-gif.extract.core
  (:import java.io.File)
  (:import org.jcodec.api.FrameGrab)
  (:import org.jcodec.common.io.NIOUtils)
  (:import org.jcodec.scale.AWTUtil)
  (:require [clojure.tools.logging :as log])
  (:require [pl.tomaszgigiel.meaningful-gif.extract.cmd :as cmd])
  (:require [pl.tomaszgigiel.qrcode.qrcode :as qrcode])
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:gen-class))

(defn- do-something [frame]
  (let [buffered-image (AWTUtil/toBufferedImage frame)]
    (println (qrcode/text buffered-image))))

(defn- meaningful-gif [file-name-props]
  (let [props (misc/load-props file-name-props)
        input-file (-> props :extract-input-file misc/replace-variable-environment)
        output-file (-> props :extract-output-file misc/replace-variable-environment)
        grab (-> input-file File. NIOUtils/readableChannel FrameGrab/createFrameGrab)]
    (loop [frame (.getNativeFrame grab)]
      (when (some? frame)
        (do-something frame)
        (recur (.getNativeFrame grab))))))

(defn -main [& args]
  "meaningful-gif: From files to animated GIF and back in Clojure: extract agif"
  (let [{:keys [uri options exit-message ok?]} (cmd/validate-args args)]
    (if exit-message
      (cmd/exit (if ok? 0 1) exit-message)
      (meaningful-gif (first args))))
  (log/info "ok")
  (shutdown-agents))