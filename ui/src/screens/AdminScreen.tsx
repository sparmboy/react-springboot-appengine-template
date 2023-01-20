import {RouteComponentProps, RouterProps} from "@reach/router";
import {
    Grid, Paper, Typography,
} from "@mui/material";
import './HomeScreen.css';
import {BackButton} from "../components/navigation/BackButton";



interface AdminScreenProps extends RouteComponentProps<RouterProps> {
}

const AdminScreen: React.FC<AdminScreenProps & RouteComponentProps> = () => {

    return <Grid
        container
        direction="column"
        justifyContent="center"
        alignItems="center"
        spacing={2}
    >

        <BackButton/>

        <Grid item sx={{marginTop: 16}}>
            <img alt="logo" src={"/logo192.png"}/>
        </Grid>

        <div className="circle-right"/>

        <Grid item container justifyContent="center" alignItems={"stretch"} >
            <Paper elevation={2}>
                <Typography> This is the admin page</Typography>
            </Paper>
        </Grid>

    </Grid>
}

export default AdminScreen;