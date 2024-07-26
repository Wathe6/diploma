FROM eclipse-temurin:17-jre-alpine
EXPOSE 9090
WORKDIR /opt/app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
