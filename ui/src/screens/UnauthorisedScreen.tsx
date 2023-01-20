import {
    Button,
    Grid, Typography
} from "@mui/material";
import {navigate, RouteComponentProps} from "@reach/router";
import "./HomeScreen.css";
import {GiHalt} from "react-icons/all";
import {ROUTE_HOME} from "../constants/routes";


const UnauthorisedScreen: React.FC<RouteComponentProps> = () => {
    const onHome = () => navigate(ROUTE_HOME);

    return <Grid
        container
        direction="column"
        justifyContent="center"
        alignItems="center"
        spacing={2}
    >
        <div className="circle-right"/>

        <Grid item sx={{marginTop: 16}}>
            <img alt="logo" src={"/logo192.png"}/>
        </Grid>

        <Grid item sx={{borderRadius: 150}}>
            <GiHalt size={144} color={"#727f8f"}/>
        </Grid>


        <Grid item>
            <Typography variant={"h3"} color={"white"}>Sorry you don't have permission to access this.</Typography>
        </Grid>


        <Grid item container justifyContent="center" alignItems={"stretch"} >
            <Button variant={"contained"}
                    onClick={onHome}
                    sx={{
                        width: '100%',
                        marginRight: '32px',
                        borderRadius: 2
                    }}
            >Home</Button>
        </Grid>



    </Grid>

}

export default UnauthorisedScreen;