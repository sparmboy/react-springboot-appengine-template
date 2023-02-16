# react-springboot-appengine-template

This is an auto-generated project template.

## Build
To build this project run the following from the command line
```shell
mvn clean install
```

## Configuration
1. To utilise Google OAuth2.0 you will need to create a google account then go [here](https://console.cloud.google.com/apis/credentials) 
2. Click Create Credentials and select Oauth client ID
![img.png](docs/google-01.png)
3. Select External then click Create
![img_1.png](docs/google-02.png)
4. Fill in the details of your domain / server:
![img_2.png](docs/google-03.png)
5. Add the scope for the users email
![img_3.png](docs/google-04.png)
6. Add test users:
![img_4.png](docs/google-05.png)
7. Go back to the dashboard and create OAuth client ID again (confusing yes I know)
![img_5.png](docs/google-06.png)
8. Select Web application and add the redirect URIs using the following paths but replace your server domain details:
![img_6.png](docs/google-07.png)
![img_8.png](docs/google-08.png)
9. Set the client ID and Client secret  in the [application.yml](./webapp/src/main/resources/application.yml) (use the [PropertyEncryptionUtil](webapp/src/main/java/com/example/utils/PropertyEncryptionUtil.java) to encrypt it)
10. Create an environment variable called *ENC_PASSWORD* which stores your encryption password (keep this safe - obvs)
11. Create an encrypted password and set it as the *tokenSecret* in the [application.yml](./webapp/src/main/resources/application.yml)

# Apple
To utilise Apple OAuth2.0, then strap in buster, it's gonna get rough:
1. Create an account with [Apple Developer](https://developer.apple.com/). This will cost you £70 a year (possibly different where you live) because Apple hate you and want your family to die.
2. When you are all registered and signed up go to the [Identifier Page](https://developer.apple.com/account/resources/identifiers/list)
3. In the top right next your name is your TeamID. Copy this and set it as the **teamId** in the [application.yml](webapp/src/main/resources/application.yml) under the **apple** section
![Team ID](docs/apple-teamid.png)
4. Under the Identifiers section ensure "App Id" is selected on the right and then click the add button.
![apple-add-appid.png](docs/apple-add-appid.png)
5. Select "App Ids" then continue
![img.png](docs/apple-app-ids.png)
6. Select "App" type then continue
![img.png](docs/apple-app-type.png)
7. Enter a description and a BundleID (in reverse domain stylee) but DONT PRESS CONTINUE YET
![img.png](docs/apple-register-app-id.png)
8. Scroll down to "Sign in with Apple", make sure its selected
![img.png](docs/apple-sign-in-edit.png)
9. Now scroll back up and click Continue
10. Then check details and click Register:
![img.png](docs/apple-confirm-app-id.png)
11. Next go back to the identifiers page and ensure "Serivce Ids" is selected from the drop down on the right, then click add
![img.png](docs/apple-add-service.png)
12. Add the details as with the application ID but extend with "Service" (you dont have to do this but makes it easier to distinguish). Click Continue, then Register on the next page
![img.png](docs/apple-add-service-details.png)
13. Back on the identifiers screen, click on your newly created service:
![img.png](docs/apple-click-service.png)
14. Enable sign in with Apple then click "Configure"
![img.png](docs/apple-config-service.png)
15. In the config dialog, make sure your App Id you just created is selected, enter your domain (without protocol prefix), then add the full URL to the call back (https://whateveryourdomainis.com/api/oauth2/callback/apple) - only change the domain, keep the path the same!
![img.png](docs/apple-config-service-dialog.png)
16. Click through Continue, then "Save":
![img.png](docs/apple-save-service.png)
17. Now go to the keys menu then click the add button:
![img.png](docs/apple-add-key.png)
18. Enter a key name, select the "Sign in with Apple" and click "Configure"
![img.png](docs/apple-config-key.png)
19. Make sure your app id is selected then click Save:
![img.png](docs/apple-save-key.png)
20. Then click continue:
![img2.png](docs/apple-continue-key.png)
21. Then Register:
![img_1.png](docs/apple-register-key.png)
22. You will then be prompted to download your key. Download the file and place it here: [webapp/src/main/resources/security/oauth/apple](webapp/src/main/resources/security/oauth/apple) DO NOT RENAME THE FILE!
23. In the [application.yml](webapp/src/main/resources/application.yml) set the **keyId** to be the same as the  Key ID for the key you created (this will be the same code that is in the filename of the downloaded key):
![img.png](docs/apple-key-id.png)
24. And finally set the **clientId** in [application.yml](webapp/src/main/resources/application.yml) to be the serivceId which in this example was com.myapplication.service
![img.png](docs/apple-service-id.png)
25. Build and deploy to your domain and then when you follow the apple oAuth link (generated from [here](webapp/src/main/java/com/example/controller/LoginControllerApiDelegateImpl.java)) for apple, then quicker than you can say "fuck me that was a colossal ball-ache and infinitely more difficult and expensive than setting up Google Oauth why did I even get into computing in the first place I should have been a dancer or something", then it _should_ authenticate you. 


## Running the application
Once built run the following:
```shell
java -jar webapp/target/react-springboot-appengine-template-*.jar
```
Once started you should be able to view the following:
* The running app: [http://localhost:8080](http://localhost:8080)
* swagger api documentation: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* swagger api definition json: [http://localhost:8080/v2/api-docs](http://localhost:8080/v2/api-docs)

## Running the UI
```shell
cd ui 
npm run storybook
```
After a short while you should see a browser window open displaying a storybook app that allows you to test the UI components

## Testing Docker
You can test your docker script locally with the folloing command:
```shell
docker build -t maven-caching .
```



![img.png](https://preview.redd.it/nate1bd6wzxz.jpg?auto=webp&s=c155ebd29641ac27a48b8a5a0b38c670467e9a02)

_Good luck. We're all counting on you_