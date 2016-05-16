(defproject s3-dir-uploader "0.1.0-SNAPSHOT"
  :description "Minimal tool for uploading a directory to/as an Amazon S3 bucket."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [amazonica "0.3.57"]]
  :main s3-dir-uploader.core
  :aot [s3-dir-uploader.core])
