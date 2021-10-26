#
# Build stage
#
FROM maven:3.3.9-jdk-8-onbuild-alpine AS build
COPY api /home/app/api
COPY ui /home/app/ui
COPY webapp /home/app/webapp
COPY pom.xml /home/app
WORKDIR /home/app
RUN mvn clean package

#
# Package stage
#
FROM openjdk:8-jdk-alpine
COPY --from=build /home/app/webapp/target/*.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]