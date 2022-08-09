#
# Build stage
#
FROM maven:alpine as build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME

ADD pom.xml $HOME
ADD api/pom.xml $HOME/api/pom.xml
ADD ui/pom.xml $HOME/ui/pom.xml
ADD webapp/pom.xml $HOME/webapp/pom.xml

RUN mvn -pl api verify --fail-never
ADD api $HOME/api
RUN mvn -pl api install

RUN mvn -pl ui verify --fail-never
ADD ui $HOME/ui
RUN mvn -pl ui install

RUN mvn -pl webapp verify --fail-never
ADD webapp $HOME/webapp
RUN mvn -pl api,ui,webapp package

#
# Package stage
#
FROM openjdk:8-jdk-alpine
COPY --from=build /home/app/webapp/target/*.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]