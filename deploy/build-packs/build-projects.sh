#!/bin/bash

cd ../..
cd config-server && ./gradlew clean bootBuildImage -x test && cd ..
cd eureka-server && ./gradlew clean bootBuildImage -x test && cd ..
cd gateway && ./gradlew clean bootBuildImage -x test && cd ..
cd notification && ./gradlew clean bootBuildImage -x test && cd ..
cd order && ./gradlew clean bootBuildImage -x test && cd ..
cd product && ./gradlew clean bootBuildImage -x test && cd ..
cd user && ./gradlew clean bootBuildImage -x test && cd ..