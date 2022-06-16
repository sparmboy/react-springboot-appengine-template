# react-springboot-appengine-template

This is an auto-generated project template.

## Build
To build this project run the following from the command line
```shell
mvn clean install
```

## Configuration
* To utilise Google OAuth2.0 you will need to create a google account then go [here](https://console.cloud.google.com/apis/credentials) to generate a Client ID and Client Secret for your google project and then set them in the [application.yml](./webapp/src/main/resources/application.yml)
* Create an environment variable called *ENC_PASSWORD* which stores your encryption password (keep this safe - obvs)
* Create an encrypted password and set it as the *tokenSecret* in the [application.yml](./webapp/src/main/resources/application.yml)

## Running the application
Once built run the following:
```shell
java -jar webapp/target/react-springboot-appengine-template-*.jar
```
Once started you should be able to view the following:
* swagger api documentation: [http://localhost:8080/app/swagger-ui/html](http://localhost:8080/app/swagger-ui.html)
* swagger api definition json: [http://localhost:8080/app/v2/api-docs](http://localhost:8080/app/v2/api-docs)

## Running the UI
```shell
cd ui 
npm run storybook
```
After a short while you should see a browser window open displaying a storybook app that allows you to test the UI components

![img.png](https://preview.redd.it/nate1bd6wzxz.jpg?auto=webp&s=c155ebd29641ac27a48b8a5a0b38c670467e9a02)

_Good luck. We're all counting on you_