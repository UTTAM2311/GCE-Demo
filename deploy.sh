#!/bin/sh

## Create an instance

echo "Start creating the instance named : demo-instance"

gcloud compute instances create demo-instance \
--image-family debian-9 \
--image-project debian-cloud \
--machine-type g1-small \
--scopes "userinfo-email,cloud-platform" \
--metadata-from-file startup-script=instance-startup.sh \
--metadata BUCKET=sl-demo-01 \
--zone us-east1-b \
--tags http-server


## Check the progress of instance creation

echo "Verifing the progress of instance creation"

gcloud compute instances get-serial-port-output demo-instance \
    --zone us-east1-b

echo "Done creating the instance named : demo-instance !"

## Create a firewall rule to allow traffic to your instance

gcloud compute firewall-rules create default-allow-http-8080 \
    --allow tcp:8080 \
    --source-ranges 0.0.0.0/0 \
    --target-tags http-server \
    --description "Allow port 8080 access to http-server"


echo "Done creating a firewall rule to allow traffic to : demo-instance !"

## Get the external IP address of your instance:

gcloud compute instances list
