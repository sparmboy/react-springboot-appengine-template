import {
    Configuration,
    ConfigurationParameters,
    LoginControllerApi,
    OrdersControllerApi
} from "@react-springboot-appengine-template/api/dist";

//export const host = ''; // Leave this as blank when building to serve within the spring boot app
export const host = 'http://localhost:8080'; // Leave this as blank when building to serve within the spring boot app

const API_PATH = '/api/v1';
export const ACCESS_TOKEN = 'accessToken';
//export const OAUTH2_REDIRECT_URI = 'http://localhost:8080/oauth2'
//export const GOOGLE_AUTH_URL = host + '/oauth2/authorize/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
//export const FACEBOOK_AUTH_URL = host + '/oauth2/authorize/facebook?redirect_uri=' + OAUTH2_REDIRECT_URI;
//export const GITHUB_AUTH_URL = host + '/oauth2/authorize/github?redirect_uri=' + OAUTH2_REDIRECT_URI;

let configurationParameters: ConfigurationParameters = {
    basePath: host + API_PATH,
    accessToken: (name?: string, scopes?: string[]) => {
        const accessToken = localStorage.getItem(ACCESS_TOKEN);
        return accessToken ? accessToken : '';
    }
};
let configuration = new Configuration(configurationParameters);

export const ordersApi = new OrdersControllerApi(configuration);
export const loginApi = new LoginControllerApi(configuration);


