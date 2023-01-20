import React, {FC, FunctionComponent} from 'react';
import {AuthState, AuthStateContext, doesUserHaveAnyOneOfTheseRoles} from "../../utils/auth/auth";
import {UserRole} from "../../constants/roles";
import {NavigateTo} from "../navigation/NavigateTo";
import {RouteComponentProps} from "@reach/router";
import {ROUTE_UNAUTHORISED} from "../../constants/routes";

export type ProtectedRouteProps = RouteComponentProps & { as: FunctionComponent;
    permittedRoles: UserRole[],
    unauthorisedRoute?: string
};

export const ProtectedRoute: FC<ProtectedRouteProps> = ({ as: Component, ...props }) => {
    const { ...rest } = props;
    const isAuthorised = (authState: AuthState): boolean => {
        return authState.user ? doesUserHaveAnyOneOfTheseRoles(authState.user, props.permittedRoles) : false;
    }

    return <AuthStateContext.Consumer>
        {authState => <>
            {isAuthorised(authState) && <Component {...rest} />}
            {authState.user && !isAuthorised(authState) && <NavigateTo authState={authState} to={props.unauthorisedRoute ? props.unauthorisedRoute : ROUTE_UNAUTHORISED}/>}
        </>
        }
    </AuthStateContext.Consumer>
};
