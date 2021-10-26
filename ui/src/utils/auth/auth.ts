import {ACCESS_TOKEN, host} from "../../services/apiConfig";

const request = (options:any):Promise<any> => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    })

    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', getAuthorizationHeader())
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
        .then(response =>
            response.json().then(json => {
                if(!response.ok) {
                    return Promise.reject(json);
                }
                return json;
            })
        );
};

export const getAuthorizationHeader = ():string => ('Bearer ' + localStorage.getItem(ACCESS_TOKEN));

export const getCurrentUser = (): Promise<any> => {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: host + "/api/user/me",
        method: 'GET'
    });
}
