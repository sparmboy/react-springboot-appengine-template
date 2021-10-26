import React, {useEffect, useState} from 'react';
import {Router} from "@reach/router";
import {ProtectedRoute} from "./components/routes/ProtectedRoute";
import {getCurrentUser} from "./utils/auth/auth";
import HomeScreen from "./screens/HomeScreen";
import LoginScreen from "./screens/LoginScreen";
import {OAuth2RedirectHandler} from "./security/OAuth2RedirectHandler";
import {MyOrdersScreen} from "./screens/MyOrdersScreen";

export type AppProps = {
  hideLoader: () => void,
  showLoader: () => void
}

function App(props: AppProps) {
  const [authenticated, setAuthenticated] = useState<boolean>(false);

  const authenticate = () => {
    console.log('Attempting authentication...');
    getCurrentUser()
        .then((resp) => {
          console.log('User is authenticated with current session');
          props.hideLoader();
          setAuthenticated(true);
        })
        .catch(() => {
          console.log('User not authenticated');
          props.hideLoader();
        })
    ;
  }

  useEffect(authenticate, [props])
  return <div>
    <Router>
      <HomeScreen path="/" authenticated={authenticated}/>
      <LoginScreen path="/login" authenticated={authenticated}/>
      <OAuth2RedirectHandler path="/oauth2"/>
      <ProtectedRoute path="orders" isLoggedIn={authenticated} as={MyOrdersScreen}/>
    </Router>
  </div>
}

export default App;
