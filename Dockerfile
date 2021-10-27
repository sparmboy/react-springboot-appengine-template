#
# Build stage
#
FROM maven:3.3.9-jdk-8 AS build
COPY api/ /home/app/api/
RUN ls -ltR /home/app/api/*
COPY ui/ /home/app/ui/
COPY webapp/ /home/app/webapp/
COPY pom.xml /home/app
WORKDIR /home/app/api
RUN mvn compile exec:java -Dexec.mainClass="com.Test" -Dexec.args="/home/app/api/src/main/resources ./schema/requests/signUpRequest.schema.json"
#RUN mvn -X clean package

#
# Package stage
#
FROM openjdk:8-jdk-alpine
COPY --from=build /home/app/webapp/target/*.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]