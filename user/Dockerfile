FROM eclipse-temurin:17-jre-ubi9-minimal
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]