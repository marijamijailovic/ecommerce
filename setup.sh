#!/bin/bash -e

docker-compose -f docker/docker-compose.yml up -d

./gradlew clean
./gradlew build
./gradlew bootRun