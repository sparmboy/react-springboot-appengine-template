import { FC} from 'react';
import {doesUserHaveAnyOneOfTheseRoles} from "../../utils/auth/auth";
import {UserRole} from "../../constants/roles";
import {NavigateTo} from "../navigation/NavigateTo";
import {ROUTE_UNAUTHORISED} from "../../constants/routes";
import {RouteProps} from "react-router";
import {AuthState, useAuth} from "../../services/Authentication/Authentication.types";

export type ProtectedRouteProps = RouteProps & {
    children:React.ReactNode;
    permittedRoles: UserRole[],
    unauthorisedRoute?: string
};

export const ProtectedRoute: FC<ProtectedRouteProps> = ({children,permittedRoles,unauthorisedRoute}) => {
    const isAuthorised = (authState: AuthState): boolean => {
        return authState.user ? doesUserHaveAnyOneOfTheseRoles(authState.user, permittedRoles) : false;
    }
    const authStore = useAuth();

    return <>
        {isAuthorised(authStore) && children}
        {authStore.user && !isAuthorised(authStore) && <NavigateTo authState={authStore} to={unauthorisedRoute ? unauthorisedRoute : ROUTE_UNAUTHORISED}/>}
    </>

};
