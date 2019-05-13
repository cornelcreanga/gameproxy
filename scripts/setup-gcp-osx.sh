#!/usr/bin/env bash

wget https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-217.0.0-darwin-x86_64.tar.gz
tar -xvf google-cloud-sdk-217.0.0-darwin-x86_64.tar.gz
./google-cloud-sdk/install.sh
brew install kubernetes-cli kubernetes-helm