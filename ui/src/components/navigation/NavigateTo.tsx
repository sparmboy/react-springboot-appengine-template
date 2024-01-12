import {useEffect} from "react";
import {AuthState} from "../../utils/auth/auth";
import {useNavigate} from "react-router";

export const NavigateTo = (props: { to:string,authState: AuthState }) => {
    const navigate = useNavigate();
    useEffect(() => {
        navigate(props.to);
    },[navigate,props.to])
    return <></>
}
