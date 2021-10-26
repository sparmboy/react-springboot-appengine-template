import {Configuration, ConfigurationParameters, OrdersControllerApi} from "@react-springboot-appengine-template/api/dist";

const host = ''; // Leave this as blank when building to serve within the spring boot app
const API_PATH = '/api/v1';

let configurationParameters:ConfigurationParameters = {basePath:host+API_PATH};
let configuration = new Configuration(configurationParameters);

export const ordersApi = new OrdersControllerApi(configuration);