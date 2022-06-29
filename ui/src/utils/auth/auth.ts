import {ACCESS_TOKEN, host} from "../../services/apiConfig";
import React, {Dispatch} from "react";

export type AuthState = {
    authenticated: boolean,
    name?:string,
    imageUrl?:string,
    roles?:string[],
    accessToken?: string,
}

export const initialAuthState: AuthState = {
    authenticated: false,
};

export const authReducer = (state: AuthState, action: AuthAction) => {
    switch (action.type) {
        case 'success':
            if(action.authState.accessToken) {
                localStorage.setItem(ACCESS_TOKEN, action.authState.accessToken);
            }
            return {...action.authState,authenticated:true};
        case 'failure':
            return {authenticated: false};
        default:
            throw new Error();
    }
}

export type AuthAction =
    | { type: 'success', authState: AuthState }
    | { type: 'failure', error: string };

export const AuthDispatchContext = React.createContext<Dispatch<AuthAction> | null>(null);
export const AuthStateContext = React.createContext<AuthState>(initialAuthState);


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
