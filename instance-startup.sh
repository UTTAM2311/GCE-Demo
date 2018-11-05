#!/bin/sh

# Set the metadata server to the get projct id
PROJECTID=$(curl -s "http://metadata.google.internal/computeMetadata/v1/project/project-id" -H "Metadata-Flavor: Google")
BUCKET=""
INSTANCE_CONNECTION_NAME=""
DB_NAME=""
DB_USERNAME=""
DB_PASSWORD=""
PUBSUB_TOPIC=""
PUBSUB_SUBSCRIPTION_NAME=""

echo "Project ID: ${PROJECTID} Bucket: ${BUCKET}"

# Get the files we need
gsutil cp gs://${BUCKET}/demo.jar .

# get the service account file for cloud sql,pub/sub and cloud storage.
gsutil cp gs://${BUCKET}/cred.json .

# download the cloud proxy
wget https://dl.google.com/cloudsql/cloud_sql_proxy.linux.amd64 -O cloud_sql_proxy

# make it executable
chmod +x cloud_sql_proxy

./cloud_sql_proxy -instances=${INSTANCE_CONNECTION_NAME}=tcp:5432 -credential_file=cred.json &

# add variables for application properties
export db_name=${DB_NAME}
export db_username=${DB_USERNAME}
export db_password=${DB_PASSWORD}
export bucket_name=${BUCKET}
export pub_sub_topic=${PUBSUB_TOPIC}
export pub_sub_subscription_name=${PUBSUB_SUBSCRIPTION_NAME}

export GOOGLE_APPLICATION_CREDENTIALS=cred.json


# Install dependencies
apt-get update
apt-get -y --force-yes install openjdk-8-jdk

# Make Java 8 default
update-alternatives --set java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java

# Start server
java -jar demo.jar
