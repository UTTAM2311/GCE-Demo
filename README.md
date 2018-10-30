# GCE-Demo

Demo for deploying the application in Google Compute Engine.

## Prerequisites

* gradle 4.10 or above
* java 8
* Google cloud SDK (gcloud,gsutil)

## Application deployment 

### Building a release for deployment
Create a Google Cloud Storage bucket

You use Cloud Storage to store your application's dependencies.

1. Choose a bucket name (such as demo-01), then create the bucket by running the following command:

       gsutil mb gs://demo-01
2. Creating a build to upload to Cloud Storage
  
     you can build the release locally and upload it to Cloud Storage:
  
    1. Build the Spring Boot application by running the following command from the root of your application folder:
  
           gradle build
    2. Upload the app to Cloud Storage:

           gsutil cp build/libs/* gs://demo-01/demo.jar

3. Deploying your application to a single instance
    
    1. Run  a startup script. Created a file called instance-startup.sh in the application's root directory. Created and configured a Compute Engine instance .
    
           $ sh deploy.sh
           
   #### Reference link :
   * https://cloud.google.com/community/tutorials/kotlin-springboot-compute-engine