#!/usr/bin/env bash

set -x #echo on

kubectl delete ns production  --grace-period=0 && sleep 180 || true
kubectl create ns production
kubectl --namespace=production apply -f k8/production
kubectl --namespace=production apply -f k8/canary
kubectl --namespace=production apply -f k8/services

kubectl --namespace=production get service prime-palindrome
