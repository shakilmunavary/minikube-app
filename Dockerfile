FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy Maven build output (any jar)
COPY target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
