#!/bin/bash -e

if [[ "$(docker ps -q -f name=ecommerce)" != "" ]];
then
	echo "Stoping container..."
	docker stop ecommerce
fi

if [[ "$(docker ps -q -f status=exited -f name=ecommerce)" != "" ]];
then
	echo "Removing container..."
	docker rm ecommerce
fi

if [[ "$(docker images -q ecommerce)" != "" ]];
then
	docker rmi $(docker images -q ecommerce)
fi

docker build -t ecommerce .
docker-compose -f docker-compose.yml up -d
