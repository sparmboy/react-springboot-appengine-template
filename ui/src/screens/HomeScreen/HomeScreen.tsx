import {RouteProps, useNavigate} from "react-router";
import {
    Button,
    Grid2 as Grid,
} from "@mui/material";
import './HomeScreen.css';
import { useEffect} from "react";
import {ROUTE_ADMIN, ROUTE_LOGIN} from "../../constants/routes";
import { doesUserHaveAnyOneOfTheseRoles, signOut} from "../../utils/auth/auth";
import {ROLE_ADMIN} from "../../constants/roles";
import {GoSignOut} from "react-icons/go";
import { GrUserAdmin} from "react-icons/gr";
import {useAuth} from "../../services/Authentication/Authentication.types";

const HomeScreen: React.FC<RouteProps> = () => {

    const authStore = useAuth();

    const navigate = useNavigate();
    useEffect(()=> {
        async function navigateToLogin() {
            await navigate(ROUTE_LOGIN);
        }

        if( !authStore.authenticating ) {
            if (!authStore.user ) {
                navigateToLogin();
            }
        }

    },[authStore,navigate])

    const onAdmin = () => navigate(`${ROUTE_ADMIN}`);


    return <Grid
        container
        direction="column"
        justifyContent="center"
        alignItems="center"
        spacing={2}
    >
        <Grid sx={{marginTop: 16}}>
            <img alt="logo" src={"/logo192.png"}/>
        </Grid>

        <div className="circle-right"/>

        {authStore.user && doesUserHaveAnyOneOfTheseRoles(authStore.user,[ROLE_ADMIN]) &&
        <Grid container justifyContent="center" alignItems={"stretch"} >
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

        <Grid container justifyContent="center" alignItems={"stretch"} >
            <Button variant={"contained"}
                    onClick={() => signOut(navigate)}
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
