import { RouteComponentProps, useNavigate} from '@reach/router';
import React, { FunctionComponent } from 'react';

type Props = RouteComponentProps & { as: FunctionComponent; isLoggedIn: boolean };

const ProtectedRoute: FunctionComponent<Props> = ({ as: Component, ...props }) => {
    const { ...rest } = props;
    const navigate = useNavigate();
    if( !props.isLoggedIn ) {
        console.log('User not authenticated so unable to access this route. Navigating to login...');
        navigate('/login?uri=' + props.uri);
    }

    return <Component {...rest} />;
};

export { ProtectedRoute };