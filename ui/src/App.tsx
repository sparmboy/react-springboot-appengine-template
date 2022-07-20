import React, {useEffect, useReducer} from 'react';
import {navigate, Router} from "@reach/router";
import HomeScreen from "./screens/HomeScreen";
import LoginScreen from "./screens/LoginScreen";
import {OAuth2RedirectHandler} from "./security/OAuth2RedirectHandler";
import {AuthDispatchContext, authReducer, AuthStateContext, getCurrentUser, initialAuthState} from "./utils/auth/auth";
import SignUpScreen from "./screens/SignUpScreen";
import SignInScreen from "./screens/SignInScreen";
import {ROUTE_HOME, ROUTE_LOGIN, ROUTE_SIGNIN, ROUTE_SIGNUP, ROUTE_SUCCESS} from "./constants/routes";
import SuccessScreen from "./screens/SuccessScreen";

export type AppProps = {
    hideLoader: () => void,
    showLoader: () => void
}

function App(props: AppProps) {
    const [authState, authDispatch] = useReducer(authReducer, initialAuthState);

    const authenticate = () => {
        console.log('Attempting authentication...');
        authDispatch({
            type: 'authenticating'
        });

        getCurrentUser()
            .then((user) => {
                console.log('User is authenticated with current session');
                props.hideLoader();
                authDispatch({
                    type: 'success',
                    authState: {...user}
                });
            })
            .catch((err) => {
                console.log('User not authenticated');

                authDispatch({
                    type: 'failure',
                    error: JSON.stringify(err)
                });

                props.hideLoader();

                async function navigateToLogin() {
                    await navigate('login');
                }

                if (!window.location.href.endsWith(ROUTE_SIGNIN)) {
                    navigateToLogin();
                }
            })
        ;
    }

    useEffect(authenticate, [props]);

    return <AuthStateContext.Provider value={authState}>
        <AuthDispatchContext.Provider value={authDispatch}>
            <Router>
                <HomeScreen path={ROUTE_HOME}/>
                <LoginScreen path={ROUTE_LOGIN} />
                <SignUpScreen path={ROUTE_SIGNUP}/>
                <SignInScreen path={ROUTE_SIGNIN}/>
                <SuccessScreen path={ROUTE_SUCCESS}/>
                <OAuth2RedirectHandler path="/oauth2"/>
            </Router>
        </AuthDispatchContext.Provider>
    </AuthStateContext.Provider>
}

export default App;
