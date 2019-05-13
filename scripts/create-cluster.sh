#!/usr/bin/env bash

set -x #echo on

gcloud compute networks create jenkins
gcloud container clusters create jenkins-cd \
 --network jenkins \
 --machine-type n1-standard-2 \
 --num-nodes 2 \
 --enable-ip-alias \
 --enable-basic-auth \
 --issue-client-certificate \
 --scopes "https://www.googleapis.com/auth/projecthosting,storage-rw,cloud-platform,service-control,service-management,compute-rw,storage-ro"

gcloud container clusters list
gcloud container clusters get-credentials jenkins-cd
kubectl cluster-info


#add current user and tiller to the cluster RBAC as admins
kubectl create clusterrolebinding cluster-admin-binding --clusterrole=cluster-admin --user=$(gcloud config get-value account)
kubectl create serviceaccount tiller --namespace kube-system
kubectl create clusterrolebinding tiller-admin-binding --clusterrole=cluster-admin --serviceaccount=kube-system:tiller

helm init --service-account=tiller
helm repo update
echo 'Waiting until Tiller is up...'
until helm version; do sleep 10;done
echo 'Tiller is up.'

#install jenkins from the chart archive and customize it
#helm install -n cd stable/jenkins -f jenkins/values.yaml --version 0.18.0 --wait
helm install -n cd stable/jenkins -f jenkins/values.yaml --version 0.34.1 --wait

until kubectl get pods -l app=cd-jenkins | grep Running; do sleep 10;done

export POD_NAME=$(kubectl get pods -l "component=cd-jenkins-master" -o jsonpath="{.items[0].metadata.name}")
kubectl port-forward $POD_NAME 8080:8080 >> /dev/null &
printf "Jenkins credentials admin : %s\n" $(kubectl get secret cd-jenkins -o jsonpath="{.data.jenkins-admin-password}" | base64 --decode)
printf "Jenkins url: http://127.0.0.1:8080/"