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


