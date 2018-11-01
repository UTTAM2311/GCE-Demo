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
  
           gradle clean build -x test
    2. Upload the app to Cloud Storage and also create and upload cloud-sql service account file (follow this link for [service-account](https://cloud.google.com/sql/docs/mysql/connect-external-app#proxy) creation). Similarly for Cloud storage Integration with the app create another service account file (follow this [link](https://cloud.google.com/storage/docs/reference/libraries#setting_up_authentication)) :

           gsutil cp build/libs/* gs://demo-01/demo.jar
           gsutil cp <cloud_sql_service-account.json file> gs://demo-01/key.json
           gsutil cp <cloud_storage_service-account.json file> gs://demo-01/storage.json
           
3. Deploying your application to a single instance
    
    1. Configure environmental variables in <i>instance-start.sh</i> file.
    
           INSTANCE_CONNECTION_NAME 
           DB_NAME
           DB_USERNAME
           DB_PASSWORD
           
      These variables need to be filled in the <i>instance-startup.sh</i> file .
                         
    2. Runs  a startup script and create the VM instance.(Note: Created a file called instance-startup.sh in the application's root directory. Created and configured a Compute Engine instance) .
    
           $ sh deploy.sh
    
    3. (Optional) If want to deploy in 2 instance with load balancing .
    
           $ sh deploy.sh 
           
   #### Reference link :
   * https://cloud.google.com/community/tutorials/kotlin-springboot-compute-engine