export const ROUTE_SIGNUP = '/signup';
export const ROUTE_SIGNIN = '/signin';
export const ROUTE_HOME = '/';
export const ROUTE_ADMIN = '/admin';
export const ROUTE_LOGIN = '/login';
export const ROUTE_SUCCESS = '/success';
export const ROUTE_UNAUTHORISED = '/unauthorised';


export const isCurrentUrlLoginRoute = (): boolean => {
    return window.location.href.endsWith(ROUTE_SIGNIN) || window.location.href.endsWith(ROUTE_LOGIN);
}


