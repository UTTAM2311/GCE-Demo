#!/bin/sh

# Set the metadata server to the get projct id
PROJECTID=$(curl -s "http://metadata.google.internal/computeMetadata/v1/project/project-id" -H "Metadata-Flavor: Google")
BUCKET=$(curl -s "http://metadata.google.internal/computeMetadata/v1/instance/attributes/BUCKET" -H "Metadata-Flavor: Google")

INSTANCE_CONNECTION_NAME=""
DB_NAME=""
DB_USERNAME=""
DB_PASSWORD=""

echo "Project ID: ${PROJECTID} Bucket: ${BUCKET}"

# Get the files we need
gsutil cp gs://${BUCKET}/demo.jar .

# get the service account file for cloud sql and colud storage.
gsutil cp gs://${BUCKET}/key.json .
gsutil cp gs://${BUCKET}/storage.json .

# download the cloud proxy
wget https://dl.google.com/cloudsql/cloud_sql_proxy.linux.amd64 -O cloud_sql_proxy

# make it executable
chmod +x cloud_sql_proxy

./cloud_sql_proxy -instances=${INSTANCE_CONNECTION_NAME}=tcp:5432 -credential_file=key.json &

# add variables for application properties
export db_name=${DB_NAME}
export db_username=${DB_USERNAME}
export db_password=${DB_PASSWORD}
export GOOGLE_APPLICATION_CREDENTIALS=storage.json
# Start server
java -jar demo.jar
