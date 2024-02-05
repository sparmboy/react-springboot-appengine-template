import React, {useEffect, useReducer} from 'react';
import HomeScreen from "./screens/HomeScreen";
import LoginScreen from "./screens/LoginScreen";
import {OAuth2RedirectHandler} from "./security/OAuth2RedirectHandler";
import {
    AuthDispatchContext,
    authReducer,
    AuthStateContext,
    getCurrentUser,
    getSavedUrlAndClear,
    initialAuthState, saveCurrentUrl
} from "./utils/auth/auth";
import SignUpScreen from "./screens/SignUpScreen";
import SignInScreen from "./screens/SignInScreen";
import {
    isCurrentUrlLoginRoute, ROUTE_ADMIN,
    ROUTE_HOME,
    ROUTE_LOGIN,
    ROUTE_SIGNIN,
    ROUTE_SIGNUP,
    ROUTE_SUCCESS, ROUTE_UNAUTHORISED
} from "./constants/routes";
import SuccessScreen from "./screens/SuccessScreen";
import {ProtectedRoute} from "./components/routes/ProtectedRoute";
import AdminScreen from "./screens/AdminScreen";
import {ROLE_ADMIN} from "./constants/roles";
import UnauthorisedScreen from "./screens/UnauthorisedScreen";
import {Route,Routes, useNavigate} from "react-router";

export type AppProps = {
    hideLoader: () => void,
    showLoader: () => void
}

function App(props: AppProps) {
    const [authState, authDispatch] = useReducer(authReducer, initialAuthState);
    const navigate = useNavigate();

    const authenticate = () => {
        console.log('Attempting authentication...');
        if (!isCurrentUrlLoginRoute()) {
            saveCurrentUrl();
        }
        authDispatch({
            type: 'authenticating'
        });

        getCurrentUser()
            .then((user) => {
                console.log('User is authenticated with current session');
                props.hideLoader();
                authDispatch({
                    type: 'success',
                    authState: {...authState, user: user}
                });

                const url = getSavedUrlAndClear();
                if (url) {
                    navigate(url);
                } else {
                    navigate(ROUTE_HOME);
                }

            })
            .catch((err) => {
                console.log('User not authenticated');

                authDispatch({
                    type: 'failure',
                    error: JSON.stringify(err)
                });

                props.hideLoader();

                async function navigateToLogin() {
                    await navigate(ROUTE_LOGIN);
                }

                if (!isCurrentUrlLoginRoute()) {
                    saveCurrentUrl();
                    navigateToLogin();
                }
            })
        ;
    }
    useEffect(authenticate, [props]);

    return <AuthStateContext.Provider value={authState}>
        <AuthDispatchContext.Provider value={authDispatch}>
            <Routes>
                <Route path={ROUTE_HOME} element={<HomeScreen/>}/>
                <Route element={<LoginScreen/>} path={ROUTE_LOGIN} />
                <Route element={<SignUpScreen/>} path={ROUTE_SIGNUP}/>
                <Route element={<SignInScreen/>} path={ROUTE_SIGNIN}/>
                <Route element={<SuccessScreen/>} path={ROUTE_SUCCESS}/>
                <Route element={<OAuth2RedirectHandler/>} path="/oauth2"/>

                {/*Admin Role*/}
                <Route path={ROUTE_ADMIN} element={<ProtectedRoute permittedRoles={[ROLE_ADMIN]} as={AdminScreen}/>} />

                <Route element={<UnauthorisedScreen/>} path={`${ROUTE_UNAUTHORISED}`}/>

            </Routes>
        </AuthDispatchContext.Provider>
    </AuthStateContext.Provider>
}

export default App;
