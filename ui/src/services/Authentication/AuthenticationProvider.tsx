import {AuthContext, AuthError, AuthState} from "./Authentication.types";
import {ACCESS_TOKEN} from "../../constants/session";
import {FC, useCallback, useEffect, useState} from "react";
import {isCurrentUrlLoginRoute, ROUTE_HOME, ROUTE_LOGIN} from "../../constants/routes";
import {getCurrentUser, getSavedUrlAndClear, saveCurrentUrl} from "../../utils/auth/auth";
import {UserDTO} from "@react-springboot-appengine-template/api/dist";
import {useNavigate} from "react-router";
import {LoadingScreen} from "../../screens/LoadingScreen/LoadingScreen";
import {AuthResponse} from "@react-springboot-appengine-template/api/dist/models";


export const AuthenticationProvider:FC<React.PropsWithChildren > = ({children}) => {
    const [authenticating,setAuthenticating] = useState<boolean>(false);
    const [user,setUser] = useState<UserDTO>();
    const [errorMessage,setErrorMessage] = useState<string>();
    const navigate = useNavigate();


    const authenticate = () => {
        console.log('Attempting authentication...');
        if (!isCurrentUrlLoginRoute()) {
            saveCurrentUrl();
        }
        setAuthenticating(true);

        getCurrentUser()
            .then(setUserAndNavigate)
            .catch(() => {
                console.log('User not authenticated');
                localStorage.removeItem(ACCESS_TOKEN);

                async function navigateToLogin() {
                    await navigate(ROUTE_LOGIN);
                }

                if (!isCurrentUrlLoginRoute()) {
                    saveCurrentUrl();
                    navigateToLogin();
                }
            }).finally(() =>                 setAuthenticating(false))
        ;
    }

    const setUserAndNavigate = useCallback((user:UserDTO) => {
        setUser(user);
        const url = getSavedUrlAndClear();
        if (url) {
            navigate(url);
        } else {
            navigate(ROUTE_HOME);
        }

    },[navigate])

    const onSuccess = (authResponse:AuthResponse) => {
        if( authResponse.accessToken ) {
            localStorage.setItem(ACCESS_TOKEN, authResponse.accessToken);
        }
        setUserAndNavigate(authResponse.user);
    }

    const onFailure = (err: AuthError) => {
        localStorage.removeItem(ACCESS_TOKEN);
        if( err.status === 401) {
            setErrorMessage('Unable to sign in. Please check your email address and password');
        }
    }

    const authState:AuthState = {
        authenticating,
        user,
        onSuccess,
        onFailure,
        errorMessage
    }

    useEffect(authenticate, []);


    return authenticating ? <LoadingScreen/> : <AuthContext.Provider value={authState}>{children}</AuthContext.Provider>
}
