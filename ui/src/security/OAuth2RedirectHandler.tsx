import {FC} from "react";
import {RouteProps, useNavigate} from "react-router";
import {ROUTE_HOME, ROUTE_LOGIN} from "../constants/routes";
import {ACCESS_TOKEN} from "../constants/session";

export const OAuth2RedirectHandler: FC<RouteProps> = (props) => {
    const navigate = useNavigate();

    const getUrlParameter = (name: string): string => {
        name = name.replace(/[[]/, '\\[').replace(/[\]]/, '\\]');
        const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');

        const results = props.path ? regex.exec(props.path) : '';
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    };

    const token = getUrlParameter('token');
    //const error = getUrlParameter('error');
    const uri = getUrlParameter('uri');

    console.log('Token=', token);
    console.log('uri=', uri);

    if (token) {
        localStorage.setItem(ACCESS_TOKEN, token);
        if (uri) {
            console.log('Navigating back to original uri ', uri);
            navigate(uri);
        } else {
            navigate(ROUTE_HOME);
        }
    } else {
        navigate(ROUTE_LOGIN);
    }

    return <div/>

}
