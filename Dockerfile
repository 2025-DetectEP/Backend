FROM openjdk:17-jdk
LABEL authors="yerim"
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]