import {Context, createContext, useContext} from "react";
import {UserDTO} from "@react-springboot-appengine-template/api/dist";
import {AuthResponse} from "@react-springboot-appengine-template/api/dist/models";

export type AuthState = {
    authenticating: boolean,
    accessToken?: string,
    user?: UserDTO,
    errorMessage?: string,
    onSuccess: (authResponse:AuthResponse) => void,
    onFailure: (err: AuthError) => void
}

export type AuthError = { status?:number }

const initialAuthState: AuthState = {
    authenticating: false,
    onSuccess: () => {},
    onFailure: () => {},
};

export const AuthContext: Context<AuthState> = createContext(initialAuthState);
export const useAuth = () => useContext(AuthContext);
