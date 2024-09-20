import HomeScreen from "./screens/HomeScreen/HomeScreen";
import LoginScreen from "./screens/LoginScreen/LoginScreen";
import {OAuth2RedirectHandler} from "./security/OAuth2RedirectHandler";
import SignUpScreen from "./screens/SignUpScreen/SignUpScreen";
import SignInScreen from "./screens/SignInScreen/SignInScreen";
import {
    ROUTE_ADMIN,
    ROUTE_HOME,
    ROUTE_LOGIN,
    ROUTE_SIGNIN,
    ROUTE_SIGNUP,
    ROUTE_SUCCESS, ROUTE_UNAUTHORISED
} from "./constants/routes";
import SuccessScreen from "./screens/SuccessScreen/SuccessScreen";
import {ProtectedRoute} from "./components/routes/ProtectedRoute";
import AdminScreen from "./screens/AdminScreen/AdminScreen";
import {ROLE_ADMIN} from "./constants/roles";
import UnauthorisedScreen from "./screens/UnauthorisedScreen/UnauthorisedScreen";
import {Route, Routes} from "react-router";
import {AuthenticationProvider} from "./services/Authentication/AuthenticationProvider";
import "./App.scss";

function App() {

    return <AuthenticationProvider>
        <Routes>
            <Route path={ROUTE_HOME} element={<HomeScreen/>}/>
            <Route element={<LoginScreen/>} path={ROUTE_LOGIN}/>
            <Route element={<SignUpScreen/>} path={ROUTE_SIGNUP}/>
            <Route element={<SignInScreen/>} path={ROUTE_SIGNIN}/>
            <Route element={<SuccessScreen/>} path={ROUTE_SUCCESS}/>
            <Route element={<OAuth2RedirectHandler/>} path="/oauth2"/>

            {/*Admin Role*/}
            <Route path={ROUTE_ADMIN} element={<ProtectedRoute permittedRoles={[ROLE_ADMIN]}>
                <AdminScreen/>
            </ProtectedRoute>}></Route>

            <Route element={<UnauthorisedScreen/>} path={`${ROUTE_UNAUTHORISED}`}/>

        </Routes>
    </AuthenticationProvider>
}

export default App;
