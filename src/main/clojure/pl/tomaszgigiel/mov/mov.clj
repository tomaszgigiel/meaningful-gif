(ns pl.tomaszgigiel.mov.mov
  (:import org.jcodec.api.FrameGrab)
  (:import org.jcodec.common.io.NIOUtils)
  (:import org.jcodec.scale.AWTUtil)
  (:gen-class))
        
(defn images-from-mov [file]
  (let [grab (-> file NIOUtils/readableChannel FrameGrab/createFrameGrab)
        frames (cycle (.getNativeFrame grab))]
    (map #(AWTUtil/toBufferedImage %) (take-while some? frames))))
