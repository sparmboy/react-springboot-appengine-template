import {FC} from "react";
import { RouteComponentProps, RouterProps, useNavigate} from "@reach/router";
import {ROUTE_HOME, ROUTE_LOGIN} from "../constants/routes";
import {ACCESS_TOKEN} from "../constants/session";

export const OAuth2RedirectHandler: FC<RouteComponentProps<RouterProps>> = (props) => {
    const navigate = useNavigate();
    const getUrlParameter = (name:string):string =>  {
        name = name.replace(/[[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');

        var results = props.location?.search ? regex.exec(props.location?.search) : '';
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    };

    const token = getUrlParameter('token');
    //const error = getUrlParameter('error');
    const uri = getUrlParameter('uri');

    console.log('Token=',token);
    console.log('uri=',uri);

    if(token) {
        localStorage.setItem(ACCESS_TOKEN, token);
        if( uri ) {
            console.log('Navigating back to original uri ',uri);
            navigate(uri);
        }else {
            navigate(ROUTE_HOME);
        }
    } else {
        navigate(ROUTE_LOGIN);
    }

    return <div/>

}