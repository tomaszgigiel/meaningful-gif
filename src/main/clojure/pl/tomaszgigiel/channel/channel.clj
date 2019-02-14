(ns pl.tomaszgigiel.channel.channel
  (:import java.io.FileOutputStream)
  (:import java.nio.ByteBuffer)
  (:import java.nio.channels.NonWritableChannelException)
  (:import org.jcodec.common.io.SeekableByteChannel)
  (:gen-class
    :main false))

(deftype FosChannel [^FileOutputStream fos]
  SeekableByteChannel
  (^int read [this ^ByteBuffer dst] (.read (.getChannel fos) dst))
  (^int write [this ^ByteBuffer src] (.write (.getChannel fos) src))
  (^long position [this] (.position (.getChannel fos)))
  (^SeekableByteChannel setPosition [this ^long newPosition] (do (.position (.getChannel fos) newPosition) this))
  (^long size [this] (.size (.getChannel fos)))
  (^SeekableByteChannel truncate [this ^long size] (do (.truncate (.getChannel fos) size)) this)
  (isOpen [this] (.isOpen (.getChannel fos)))
  (close [this] (.close (.getChannel fos))))

(defn new [^FileOutputStream fos] (FosChannel. fos))
