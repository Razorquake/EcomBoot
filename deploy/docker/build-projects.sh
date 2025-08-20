#!/bin/bash

cd ../..
cd config-server && ./gradlew clean build -x test && cd ..
cd eureka-server && ./gradlew clean build -x test && cd ..
cd gateway && ./gradlew clean build -x test && cd ..
cd notification && ./gradlew clean build -x test && cd ..
cd order && ./gradlew clean build -x test && cd ..
cd product && ./gradlew clean build -x test && cd ..
cd user && ./gradlew clean build -x test && cd ..