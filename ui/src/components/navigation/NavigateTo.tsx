import {useEffect} from "react";
import {navigate} from "@reach/router";
import {AuthState} from "../../utils/auth/auth";

export const NavigateTo = (props: { to:string,authState: AuthState }) => {
    useEffect(() => {
        navigate(props.to);
    },[])
    return <></>
}
