import {FC} from "react";
import { RouteComponentProps, RouterProps, useNavigate} from "@reach/router";
import {ACCESS_TOKEN} from "../services/apiConfig";

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

    if(token) {
        localStorage.setItem(ACCESS_TOKEN, token);
        if( uri ) {
            console.log('Navigating back to original uri ',uri);
            navigate(uri);
        }else {
            navigate('/');
        }
    } else {
        navigate('/login');
    }

    return <div/>

}