import {host} from "../services/apiConfig";

const WS_TOPIC_PREFIX = '/topic';
export const WS_ENDPOINT = `${host}/websockets`;

export const WS_TOPIC_MY_EVENTS = `${WS_TOPIC_PREFIX}/my_events`;
