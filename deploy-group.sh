#!/bin/sh

## Create an instance template

gcloud compute instance-templates create demo-template \
    --image-family debian-9 \
    --image-project debian-cloud \
    --machine-type g1-small \
    --scopes "userinfo-email,cloud-platform" \
    --metadata-from-file startup-script=instance-startup.sh \
    --metadata BUCKET=sl-demo-01 \
    --tags http-server

## Create an instance group

gcloud compute instance-groups managed create demo-group \
    --base-instance-name demo-group \
    --size 2 \
    --template demo-template \
    --zone us-east1-b

## create the firewall rule
gcloud compute firewall-rules create default-allow-http-8080 \
    --allow tcp:8080 \
    --source-ranges 0.0.0.0/0 \
    --target-tags http-server \
    --description "Allow port 8080 access to http-server"

## Get the names and external IP addresses of the created instances
gcloud compute instances list


## ------   Creating load balancer steps -------

## Create a health check.
gcloud compute http-health-checks create demo-health-check \
    --request-path /hello \
    --port 8080

## Create a named ports
gcloud compute instance-groups managed set-named-ports demo-group \
    --named-ports http:8080 \
    --zone us-east1-b

## Create a backend service .
gcloud compute backend-services create demo-service \
    --http-health-checks demo-health-check \
    --global

## Add your instance group to the backend service
gcloud compute backend-services add-backend demo-service \
    --instance-group demo-group \
    --global \
    --instance-group-zone us-east1-b

## Create a URL map. This defines which URLs should be directed to which backend services. In this sample, all traffic is served by one backend service.
# If you want to load balance requests between multiple regions or groups, you can create multiple backend services

gcloud compute url-maps create demo-service-map \
    --default-service demo-service

## Create a proxy that receives traffic and forwards it to backend services using the URL map
gcloud compute target-http-proxies create demo-service-proxy \
    --url-map demo-service-map

## Create a global forwarding rule. This ties a public IP address and port to a proxy
gcloud compute forwarding-rules create demo-http-rule \
    --target-http-proxy demo-service-proxy \
    --ports 80 \
    --global

## You are now done configuring the load balancer. It might take a few minutes for it to initialize and get ready to receive traffic. You can check on its progress by running the following command
gcloud compute backend-services get-health demo-service \
    --global
## Get the forwarding IP address for the load balancer:
gcloud compute forwarding-rules list --global