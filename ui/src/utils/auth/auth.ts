import { host, loginApi} from "../../services/apiConfig";
import {ACCESS_TOKEN, TARGET_URL} from "../../constants/session";
import {UserRole} from "../../constants/roles";
import {ROUTE_LOGIN} from "../../constants/routes";
import {UserDTO} from "@react-springboot-appengine-template/api/dist";
import {NavigateFunction} from "react-router/dist/lib/hooks";


export const getAuthorizationHeader = ():string => ('Bearer ' + localStorage.getItem(ACCESS_TOKEN));

export const getCurrentUser = (): Promise<UserDTO> => {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }
    const headers = new Headers({
        'Content-Type': 'application/json',
    })

    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', getAuthorizationHeader())
    }

    return fetch(host + "/api/user/me", {
        method: 'GET',
        headers:headers
    })
        .then(response =>
            response.json().then(json => {
                if(!response.ok) {
                    return Promise.reject(json);
                }
                return json;
            })
        );
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
