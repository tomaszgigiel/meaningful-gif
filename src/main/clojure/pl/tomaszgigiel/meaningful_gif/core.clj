(ns pl.tomaszgigiel.meaningful-gif.core
  (:require [clojure.string :as string])
  (:require [clojure.tools.logging :as log])
  (:require [pl.tomaszgigiel.meaningful-gif.cmd :as cmd])
  (:require [pl.tomaszgigiel.meaningful-gif.create :as create])
  (:require [pl.tomaszgigiel.meaningful-gif.extract :as extract])
  (:require [pl.tomaszgigiel.utils.misc :as misc])
  (:gen-class))

(defn- create-agif? [create-output-file]
  (and (some? create-output-file)
       (string/ends-with? create-output-file "gif")))

(defn- create-series? [create-output-path]
  (some? create-output-path))

(defn- create-mov? [create-output-file]
  (and (some? create-output-file)
       (string/ends-with? create-output-file ".mov")))

(defn- create-zip? [create-output-file]
  (and (some? create-output-file)
       (string/ends-with? create-output-file ".zip")))

(defn- extract-agif? [extract-input-file]
  (and (some? extract-input-file)
       (string/ends-with? extract-input-file "gif")))

(defn- extract-mov? [extract-input-file]
  (and (some? extract-input-file)
       (string/ends-with? extract-input-file ".mov")))

(defn- extract-series? [extract-input-path]
  (some? extract-input-path))

(defn- meaningful-gif [file-name-props]
  (let [props (misc/load-props file-name-props)
        create-input-path (-> props :create-input-path misc/replace-variable-environment misc/swallow-exceptions)
        create-output-file (-> props :create-output-file misc/replace-variable-environment misc/swallow-exceptions)
        create-output-path (-> props :create-output-path misc/replace-variable-environment misc/swallow-exceptions)
        create-output-name (-> props :create-output-name misc/replace-variable-environment misc/swallow-exceptions)
        create-chunk-size (-> props :create-chunk-size misc/replace-variable-environment Integer/parseInt misc/swallow-exceptions)
        create-qrcode-width (-> props :create-qrcode.width misc/replace-variable-environment Integer/parseInt misc/swallow-exceptions)
        create-qrcode-height (-> props :create-qrcode.height misc/replace-variable-environment Integer/parseInt misc/swallow-exceptions)
        create-qrcode-delay-time (-> props :create-qrcode.delay-time misc/replace-variable-environment Integer/parseInt misc/swallow-exceptions)
        create-qrcode-repeat-count (-> props :create-qrcode.repeat-count misc/replace-variable-environment Integer/parseInt misc/swallow-exceptions)
        extract-input-file (-> props :extract-input-file misc/replace-variable-environment misc/swallow-exceptions)
        extract-input-path (-> props :extract-input-path misc/replace-variable-environment misc/swallow-exceptions)
        extract-output-file (-> props :extract-output-file misc/replace-variable-environment misc/swallow-exceptions)]
    (when (create-agif? create-output-file) (create/create-agif create-input-path
                                                                create-output-file
                                                                create-chunk-size
                                                                create-qrcode-width
                                                                create-qrcode-height
                                                                create-qrcode-delay-time
                                                                create-qrcode-repeat-count))
    (when (create-series? create-output-path) (create/create-series create-input-path
                                                                    create-output-path
                                                                    create-output-name
                                                                    create-chunk-size
                                                                    create-qrcode-width
                                                                    create-qrcode-height))
    (when (create-mov? create-output-file) (create/create-mov create-input-path
                                                              create-output-file
                                                              create-chunk-size
                                                              create-qrcode-width
                                                              create-qrcode-height
                                                              create-qrcode-delay-time
                                                              create-qrcode-repeat-count))
    (when (create-zip? create-output-file) (create/create-zip create-input-path
                                                              create-output-file))
    (when (extract-agif? extract-input-file) (extract/extract-agif extract-input-file
                                                                   extract-output-file))
    (when (extract-mov? extract-input-file) (extract/extract-mov extract-input-file
                                                                 extract-output-file))
    (when (extract-series? extract-input-path) (extract/extract-series extract-input-path
                                                                       extract-output-file))))

(defn -main [& args]
  "meaningful-gif: From files to animated GIF and back in Clojure"
  (let [{:keys [uri options exit-message ok?]} (cmd/validate-args args)]
    (if exit-message
      (cmd/exit (if ok? 0 1) exit-message)
      (meaningful-gif (first args))))
  (log/info "ok")
  (shutdown-agents))
