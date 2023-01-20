import {RouteComponentProps, RouterProps, useNavigate} from "@reach/router";
import {
    Button,
    Grid,
} from "@mui/material";
import './HomeScreen.css';
import {useContext, useEffect} from "react";
import {ROUTE_ADMIN, ROUTE_LOGIN} from "../constants/routes";
import {AuthState, AuthStateContext, doesUserHaveAnyOneOfTheseRoles, signOut} from "../utils/auth/auth";
import {ROLE_ADMIN} from "../constants/roles";
import {GoSignOut, GrUserAdmin} from "react-icons/all";



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
            if (!authState.user ) {
                navigateToLogin();
            }
        }

    },[authState])

    const onAdmin = () => navigate(`${ROUTE_ADMIN}`);


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

        {authState.user && doesUserHaveAnyOneOfTheseRoles(authState.user,[ROLE_ADMIN]) &&
        <Grid item container justifyContent="center" alignItems={"stretch"} >
            <Button variant={"contained"}
                    onClick={onAdmin}
                    startIcon={<GrUserAdmin/>}
                    sx={{
                        width: '100%',
                        marginRight: '32px',
                        borderRadius: 2
                    }}
            >Admin Page</Button>
        </Grid>}

        <Grid item container justifyContent="center" alignItems={"stretch"} >
            <Button variant={"contained"}
                    onClick={signOut}
                    startIcon={<GoSignOut/>}
                    sx={{
                        width: '100%',
                        marginRight: '32px',
                        borderRadius: 2
                    }}
            >Sign Out</Button>
        </Grid>

    </Grid>
}

export default HomeScreen;