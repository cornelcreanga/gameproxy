#!/usr/bin/env bash

set -x #echo on

#gcloud container clusters delete jenkins-cd --zone us-east1-d --quiet
#gcloud compute instances list | awk '{if(NR>1) printf "gcloud compute instances delete %s --zone %s --delete-disks=all\n", $1, $2}'

gcloud container clusters delete jenkins-cd --quiet
gcloud compute images delete jenkins-home-image --quiet
gcloud compute disks delete jenkins-home --quiet
for rule in $(gcloud compute firewall-rules list --filter network~jenkins --format='value(name)');do
   gcloud compute firewall-rules delete $rule --quiet
done
gcloud compute networks delete jenkins --quiet
for rule in $(gcloud compute forwarding-rules list  --regexp '.*jenkins-jenkins.*'  --format='value(name)');do
   gcloud compute forwarding-rules delete $rule --global --quiet
done
for address in $(gcloud compute addresses list --regexp '.*-jenkins-jenkins.*'  --format='value(name)');do
   gcloud compute addresses delete $address --global --quiet
done
for proxy in $(gcloud compute target-https-proxies list  --regexp '.*jenkins-jenkins.*'  --format='value(name)');do
   gcloud compute target-https-proxies delete $proxy --quiet
done
for cert in $(gcloud compute ssl-certificates list --regexp 'k8s-ssl-jenkins-jenkins.*'  --format='value(name)');do
   gcloud compute ssl-certificates delete $cert --quiet
done
for target in $(gcloud compute target-pools list --regexp '.*-jenkins-jenkins.*'  --format='value(name)');do
   gcloud compute target-pools delete $target --quiet
done
for target in $(gcloud compute target-http-proxies list --regexp '.*-jenkins-jenkins.*'  --format='value(name)');do
   gcloud compute target-http-proxies delete $target --quiet
done
for url in $(gcloud compute url-maps list --regexp '.*-jenkins-jenkins.*'  --format='value(name)');do
   gcloud compute url-maps delete $url --quiet
done
