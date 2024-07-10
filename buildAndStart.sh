#!/usr/bin/env bash

echo "Starting maven build process"
mvn clean package -Pproduction

echo "Building image for client"
docker build . -t up-mqtt-java-demo:latest

echo "Starting demo"
docker compose up