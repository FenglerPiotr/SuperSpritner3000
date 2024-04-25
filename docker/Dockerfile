FROM eclipse-temurin:17.0.10_7-jre-jammy
WORKDIR /app
COPY ./target/SuperSprinter3000*.jar ./ss3000.jar
COPY ./docker/application.yaml ./
CMD ["java", "-jar", "./ss3000.jar"]
