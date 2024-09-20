import {useEffect} from "react";
import {useNavigate} from "react-router";
import {AuthState} from "../../services/Authentication/Authentication.types";

export const NavigateTo = (props: { to:string,authState: AuthState }) => {
    const navigate = useNavigate();
    useEffect(() => {
        navigate(props.to);
    },[navigate,props.to])
    return <></>
}
