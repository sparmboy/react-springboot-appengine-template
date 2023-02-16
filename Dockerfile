# cache as most as possible in this multistage dockerfile.
FROM maven:3.6-alpine as DEPS

WORKDIR /opt/app
COPY api/pom.xml api/pom.xml
COPY ui/pom.xml ui/pom.xml
COPY webapp/pom.xml webapp/pom.xml

COPY pom.xml .
RUN mvn -DbuildCommand=build-docker -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline

# if you have modules that depends each other, you may use -DexcludeArtifactIds as follows
# RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=module1

# Copy the dependencies from the DEPS stage with the advantage
# of using docker layer caches. If something goes wrong from this
# line on, all dependencies from DEPS were already downloaded and
# stored in docker's layers.
FROM maven:3.6-alpine as BUILDER
WORKDIR /opt/app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /opt/app/ /opt/app
COPY api /opt/app/api
COPY ui/package.json /opt/app/ui/package.json
COPY ui/src /opt/app/ui/src
COPY ui/public /opt/app/ui/public
COPY ui/tsconfig.json /opt/app/ui/tsconfig.json
COPY webapp/src /opt/app/webapp/src

# use -o (--offline) if you didn't need to exclude artifacts.
# if you have excluded artifacts, then remove -o flag
RUN mvn -DbuildCommand=build-docker -B -e clean install -DskipTests=true

# At this point, BUILDER stage should have your .jar or whatever in some path
FROM openjdk:8-alpine
COPY --from=builder opt/app/webapp/target/*.jar /usr/local/lib/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","/usr/local/lib/app.jar"]
