# react-springboot-appengine-template
Repository recipe for setting up a React typescript UI and Spring Boot backend generated from a openapi definition.
Contains config to quickly deploy to Gcloud app engine 

## Steps
1. Clone this repo
```bash
git clone git@github.com:sparmboy/react-springboot-appengine-template.git mynewapp
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


