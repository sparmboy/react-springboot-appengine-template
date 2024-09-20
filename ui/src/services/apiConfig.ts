import {
    Configuration,
    ConfigurationParameters,
    LoginControllerApi,
    OrdersControllerApi
} from "@react-springboot-appengine-template/api/dist";
import {ACCESS_TOKEN} from "../constants/session";
import {environmentConfig} from "../env/environment";

if( !environmentConfig ) {
    throw new Error("No environment config defined! Please define an environment variable ENV_CONFIG_JSON that points to a valid config file");
}

export const host = environmentConfig.host;
console.info('App Host set to ',host);

const API_PATH = '/api/v1';

const configurationParameters: ConfigurationParameters = {
    basePath: host + API_PATH,
    accessToken: () => {
        const accessToken = localStorage.getItem(ACCESS_TOKEN);
        return accessToken ? accessToken : '';
    }
};
const configuration = new Configuration(configurationParameters);

export const ordersApi = new OrdersControllerApi(configuration);
export const loginApi = new LoginControllerApi(configuration);


