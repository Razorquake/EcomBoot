#!/bin/bash

cd ../..
cd config-server && ./gradlew clean jib && cd ..
cd eureka-server && ./gradlew clean jib && cd ..
cd gateway && ./gradlew clean jib && cd ..
cd notification && ./gradlew clean jib && cd ..
cd order && ./gradlew clean jib && cd ..
cd product && ./gradlew clean jib && cd ..
cd user && ./gradlew clean jib && cd ..