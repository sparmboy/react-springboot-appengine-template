#
# Build stage
#
FROM maven:3.3.9-jdk-8 AS build
RUN mvn -X clean package

#
# Package stage
#
FROM openjdk:8-jdk-alpine
COPY --from=build target/*.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]