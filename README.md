# react-springboot-appengine-template
Repository recipe for setting up a React typescript UI and Spring Boot backend generated from an openapi definition.

Contains the following goodies out of the box:

* Load screen
* Material UI with Theming and Icons
* React routing secured by authentication
* Google, Facebook and Github Oauth authentication
* Web sockets
* Spring data with user principal and liquibase
* Docker file for deployment 

## Steps
1. Clone this repo
```bash
git clone https://github.com/sparmboy/react-springboot-appengine-template.git mynewapp
```
2. Run the initialisation script to set you group and artifact details
```bash
./init.sh
```
3. Build the app
```bash
mvn clean install
```

4. Start the app
```shell
java -jar webapp/target/your-new-artifact-id.jar
```

5. Access the ui on [http://localhost:8080/index.html](http://localhost:8080/index.html)

6a. Deploy to GCLoud

* Ensure you have a gcloud project setup with billing enabled
* Set the ``spring.cloud.gcp.projectId`` property in the [application.yml](webapp/src/main/resources/application.yml) to your GCloud project ID
* Set the ``projectId`` in the [webapp pom properties](webapp/pom.xml)
* Deploy by running
````shell
mvn clean install -Pdeploy
````

6b. Deploy to Digital Ocean:
* Push your project to your own Github repository
* Signup to [Digital Ocean](https://cloud.digitalocean.com/)
* Create an app and point it to your repo
