# Readme

## About

This repository includes a frontend with a Java-MQTT-Client that is able to send Messages via uProtocol. 
It also sets up a Mosquitto MQTT Broker as a transport mechanism. 
You can use it to Send and Receive Messages via MQTT and add other clients (e.g. Rust-based, ...) and let them communciate with each other. 

## Preconditions

- Docker Daemon is installed
- Maven in version 3.x is installed
- Java in version 21 is installed

## Running the application

To run the project from the command line,
type `sh buildAndStart.sh` , then open
http://localhost:8080 in your browser.
