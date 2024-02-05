import { host, loginApi} from "../../services/apiConfig";
import React, {Dispatch} from "react";
import {ACCESS_TOKEN, TARGET_URL} from "../../constants/session";
import {UserRole} from "../../constants/roles";
import {ROUTE_LOGIN} from "../../constants/routes";
import {UserDTO} from "@react-springboot-appengine-template/api/dist";
import {NavigateFunction} from "react-router/dist/lib/hooks";

export type AuthState = {
    authenticating: boolean,
    accessToken?: string,
    user?: UserDTO
}

export const initialAuthState: AuthState = {
    authenticating: false
};

export const authReducer = (state: AuthState, action: AuthAction): AuthState => {
    switch (action.type) {
        case 'success':
            if(action.authState.accessToken) {
                localStorage.setItem(ACCESS_TOKEN, action.authState.accessToken);
            }
            return {...action.authState,authenticating: false};
        case 'failure':
            localStorage.removeItem(ACCESS_TOKEN);
            return {authenticating: false};
        case 'authenticating':
            return {authenticating: true};
        default:
            throw new Error();
    }
}

export type AuthAction =
    | { type: 'authenticating' }
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

export const getCurrentUser = (): Promise<UserDTO> => {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: host + "/api/user/me",
        method: 'GET'
    });
}

/**
 * Takes the current URL and saves it to storage to be
 * retrieved after log in
 */
export const saveCurrentUrl = () => {
    localStorage.setItem(TARGET_URL, window.location.href );
}

/**
 * Returns any saved URL from storage then clears it
 */
export const getSavedUrlAndClear = ():string|null => {
    const url = localStorage.getItem(TARGET_URL);
    localStorage.removeItem(TARGET_URL);
    return url;
}

/**
 * Returns true if the specified user has any one of the specified roles
 * @param user  The user to check
 * @param roles The list of roles to check against
 */
export const doesUserHaveAnyOneOfTheseRoles = (user: UserDTO, roles: UserRole[]):boolean => roles?.find(r=>user.roles?.indexOf(r as string) !== -1) !== undefined;

export const signOut = (navigate:NavigateFunction) => {
    loginApi.logOut().then(()=>{
        localStorage.removeItem(ACCESS_TOKEN);
        navigate(ROUTE_LOGIN);
    });
}
