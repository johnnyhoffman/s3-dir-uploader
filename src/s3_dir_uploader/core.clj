(ns s3-dir-uploader.core
  (:require [amazonica.aws.s3 :as s3]
            [clojure.string :as string])
  (:gen-class))

(defn bucket-exists [creds bucket-name]
  (not-empty
    (filter
      (fn [bucket] 
        (= (:name bucket) bucket-name))
      (s3/list-buckets creds))))

(defn empty-bucket [creds bucket-name]
  (println (str "Emptying bucket '" bucket-name "'.")) 
  (let [resp (s3/list-objects creds bucket-name)
        objects (:object-summaries resp)
        truncated? (:truncated? resp)]
    (doseq [object objects]
      (do
        (println (str "Deleting '" (:key object) "' from bucket '" bucket-exists "'."))
        (s3/delete-object creds 
                        :key (:key object)
                        :bucket-name  bucket-name)))
    ;; recur when not all results were returned by first listing
    (if truncated?
      (empty-bucket creds bucket-name))))

(defn confirm [message]
  (print message)
  (flush)
  (let [answer (string/trim (read-line))]
    (if (or (= answer "Y") (= answer "y"))
      true
      false)))

(defn upload-to-bucket [creds bucket-name dir]
  (println (str "Starting upload of directory '" dir "'."))
  (let [directory (clojure.java.io/file dir)
        files (file-seq directory)]
    (doseq 
      [file files]
      (if (.isDirectory file)
        nil
        (let [k (subs (.getPath file) (+ 1 (count dir)))]
          (println (str "Uploading '" k "'."))
          (s3/put-object
            creds
            :bucket-name bucket-name
            :key k
            :file file)))))
  (println "Done uploading."))

(defn get-alternate-bucket-name [original]
  (print (str "To use bucket '" original "' press enter. Or, enter an alternate bucket name: "))
  (flush)
  (let [input (string/trim (read-line))]
    (if (empty? input)
      original
      input)))

(defn -main [& args]
  (try 
    (let [config (read-string (slurp "config.clj"))
          creds (:credentials config)
          bucket-name (:bucket-name config)
          bucket-name (get-alternate-bucket-name bucket-name)
          dir (:directory config)]
      (println (str "Checking if bucket '" bucket-name "' exists."))
      (if (bucket-exists creds bucket-name)
        (if (confirm (str "Bucket exists. Are you sure it is okay to empty the bucket '" bucket-name "? [Y] "))
          (do (empty-bucket creds bucket-name)
              (upload-to-bucket creds bucket-name dir))
          (println "Aborting."))
        (do 
          (println (str "Creating bucket '" bucket-name "'."))
          (s3/create-bucket creds bucket-name) 
          (upload-to-bucket creds bucket-name dir))))
    (catch Exception e (println (str "Error: " (.getMessage e)))))
  (print "Press enter to close")
  (flush)
  (read-line))

