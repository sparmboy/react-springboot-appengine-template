import  {FC, FunctionComponent} from 'react';
import {AuthState, AuthStateContext, doesUserHaveAnyOneOfTheseRoles} from "../../utils/auth/auth";
import {UserRole} from "../../constants/roles";
import {NavigateTo} from "../navigation/NavigateTo";
import {ROUTE_UNAUTHORISED} from "../../constants/routes";
import {RouteProps} from "react-router";

export type ProtectedRouteProps = RouteProps & { as: FunctionComponent;
    permittedRoles: UserRole[],
    unauthorisedRoute?: string
};

// @ts-ignore
export const ProtectedRoute: FC<ProtectedRouteProps> = ({ as: Component, ...props }) => {
    const { ...rest } = props;
    const isAuthorised = (authState: AuthState): boolean => {
        return authState.user ? doesUserHaveAnyOneOfTheseRoles(authState.user, props.permittedRoles) : false;
    }

    return <AuthStateContext.Consumer>
        {(authState:AuthState) => <>
            {/*@ts-ignore*/}
            {isAuthorised(authState) && <Component {...rest} />}
            {authState.user && !isAuthorised(authState) && <NavigateTo authState={authState} to={props.unauthorisedRoute ? props.unauthorisedRoute : ROUTE_UNAUTHORISED}/>}
        </>
        }
    </AuthStateContext.Consumer>
};
