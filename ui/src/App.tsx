import React, {useEffect, useReducer} from 'react';
import {Router} from "@reach/router";
import {ProtectedRoute} from "./components/routes/ProtectedRoute";
import HomeScreen from "./screens/HomeScreen";
import LoginScreen from "./screens/LoginScreen";
import {OAuth2RedirectHandler} from "./security/OAuth2RedirectHandler";
import {MyOrdersScreen} from "./screens/MyOrdersScreen";
import {AuthDispatchContext, authReducer, AuthStateContext, getCurrentUser, initialAuthState} from "./utils/auth/auth";

export type AppProps = {
  hideLoader: () => void,
  showLoader: () => void
}

function App(props: AppProps) {
    const [authState, authDispatch] = useReducer(authReducer, initialAuthState);


  const authenticate = () => {
    console.log('Attempting authentication...');
    getCurrentUser()
        .then((user) => {
          console.log('User is authenticated with current session');
          props.hideLoader();
            authDispatch({
                type: 'success', authState: {...user, authenticated: true}
            });
        })
        .catch(() => {
          console.log('User not authenticated');
          props.hideLoader();
        })
    ;
  }

  useEffect(authenticate, [props]);

  return <AuthStateContext.Provider value={authState}>
      <AuthDispatchContext.Provider value={authDispatch}>
    <Router>
      <HomeScreen path="/" authenticated={authState.authenticated}/>
      <LoginScreen path="/login" authenticated={authState.authenticated}/>
      <OAuth2RedirectHandler path="/oauth2"/>
      <ProtectedRoute path="orders" isLoggedIn={authState.authenticated} as={MyOrdersScreen}/>
    </Router>
      </AuthDispatchContext.Provider>
  </AuthStateContext.Provider>
}

export default App;
