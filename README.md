# s3-dir-uploader

A minimal tool for uploading a directory to/as an Amazon S3 bucket.

**WARNING:** that this tool will clean out the specified bucket before uploading to it, so be sure you have a backup of whatever is already in the bucket!

## Usage

### Prerequisites
* Java runtime installed

### Config
Configuration is done via a file in [EDN format](https://github.com/edn-format/edn) named *config.clj*, which should be placed in the same directory that you are executing the tool from. The config specifies the following information:

* **AWS Credentials**. Specify the access key and secret key for you aws credentials. The credentials should have read and write access to the S3 bucket(s) you are using - consider creating a new [IAM](https://aws.amazon.com/iam/) user for this.

* **Bucket Name**. The name of the default bucket to wipe and upload to.

* **Directory Path**. Path to the directory you want to upload. The contents of the directory will be uploaded, but the directory itself will not be replicated as a folder in the bucket (i.e. if you upload a directory ```d2``` with a file ```f1``` and a subdirectory ```d2``` and a file ```f2``` in ```d2```, the bucket will show ```f1``` and ```d2/f2```, but no mention of the directory name ```d1```).

### Running
Start the tool...

* **From Command Line**:
You can simply download the *.jar*, set up *config.clj*, and run the *.jar* with ```java -jar s3-dir-uploader.jar```. Alternatively, if you have [Leiningen](http://leiningen.org/) installed, you can clone the repo, set up config.clj and ```lein run```.

* **From Finder on a Mac:**
Download *s3-dir-uploader.jar*, *s3-dir-uploader.command*, and *config.clj*. Fill in the appropriate values in *config.clj*. Now you can start the tool any time by double clicking on *s3-dir-uploader.command* in Finder.

The tool will prompt for an alternative bucket name. This is useful for making regular backups. If you want to use the bucket as set in *config.clj*, just press *enter*.
If the bucket already exists, you will have to confirm that it is okay to wipe it by entering ```y``` or ```Y```. Entering anything else, or nothing at all, will abort.

## License

Copyright © 2016 Johnny Hoffman

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
