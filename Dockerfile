FROM openjdk:17

LABEL authors="nju28"

COPY ./target/cloudnative-0.0.1-SNAPSHOT.jar /app/cloudnative.jar

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "cloudnative.jar"]