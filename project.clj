(defproject meaningful-gif "1.0.0.0-SNAPSHOT"
  :description "meaningful-gif: From files to animated GIF and back in Clojure"
  :url "http://tomaszgigiel.pl"
  :license {:name "Apache License"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/tools.cli "0.3.7"]
                 [org.clojure/tools.logging "0.4.1"]
                 ;; otherwise log4j.properties has no effect
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jmdk/jmxtools
                                                    com.sun.jmx/jmxri]]
                 [commons-codec/commons-codec "1.11"]
                 [commons-io/commons-io "2.6"]
                 [com.google.zxing/core "3.3.3"]
                 [com.google.zxing/javase "3.3.3"]
                 [org.jcodec/jcodec "0.2.3"]
                 [org.jcodec/jcodec-javase "0.2.3"]]

  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :resource-paths ["src/main/resources"]
  :target-path "target/%s"
  :jar-name "meaningful-gif.jar"
  :uberjar-name "meaningful-gif-uberjar.jar"
  :main nil
  :aot [#"pl.tomaszgigiel.streams.*" #".*"]
  :profiles {:main-create {:main pl.tomaszgigiel.meaningful-gif.create.core}
             :main-extract {:main pl.tomaszgigiel.meaningful-gif.extract.core}
             :test {:resource-paths ["src/test/resources"]}
             :dev {:resource-paths ["src/test/resources"]}}
)