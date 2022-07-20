import {RouteComponentProps, RouterProps, useNavigate} from "@reach/router";
import {
    Grid,
} from "@mui/material";
import './HomeScreen.css';
import {useContext, useEffect} from "react";
import { ROUTE_LOGIN} from "../constants/routes";
import {AuthState, AuthStateContext} from "../utils/auth/auth";



interface HomeScreenProps extends RouteComponentProps<RouterProps> {
}

const HomeScreen: React.FC<HomeScreenProps & RouteComponentProps> = () => {

    const authState = useContext<AuthState>(AuthStateContext);

    const navigate = useNavigate();
    useEffect(()=> {
        async function navigateToLogin() {
            await navigate(ROUTE_LOGIN);
        }

        if( !authState.authenticating ) {
            if (!authState.authenticated ) {
                navigateToLogin();
            }
        }

    },[authState])


    return <Grid
        container
        direction="column"
        justifyContent="center"
        alignItems="center"
        spacing={2}
    >
        <Grid item sx={{marginTop: 16}}>
            <img alt="logo" src={"/logo192.png"}/>
        </Grid>

        <div className="circle-right"/>


    </Grid>
}

export default HomeScreen;