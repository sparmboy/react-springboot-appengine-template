import {RouteComponentProps, useNavigate, RouterProps} from "@reach/router";
import {Button, createStyles, Grid, makeStyles, Theme} from "@material-ui/core";
import {MdList, MdSearch, MdSettings, MdVerifiedUser} from "react-icons/all";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
        button: {
            width: '100%',
            height: 300,
            fontSize: 30,
            backgroundColor: theme.palette.common.white
        },

    }),
);

interface HomeScreenProps extends RouteComponentProps<RouterProps> {
    authenticated: boolean
}

const HomeScreen: React.FC<HomeScreenProps & RouteComponentProps> = ({authenticated}) => {
    const classes = useStyles();
    const navigate = useNavigate();
    return <Grid
        container
        direction="column"
        justifyContent="center"
        alignItems="stretch"
        spacing={2}
    >
        <Grid item>
            <Button className={classes.button}
                    color={"primary"}
                    variant={"outlined"}
                    startIcon={<MdSearch size={40}/>}
                    onClick={() => navigate('/orders/new')}>Place Order</Button>
        </Grid>
        {authenticated &&
        <Grid item>
            <Button className={classes.button}
                    color={"secondary"}
                    variant={"outlined"}
                    startIcon={<MdList size={40}/>}
                    onClick={() => navigate('/orders')}>My Orders</Button>
        </Grid>}
        {!authenticated &&
        <Grid item><Button className={classes.button} color={"secondary"} variant={"outlined"}
                                   startIcon={<MdVerifiedUser size={40}/>}
                                   onClick={() => navigate('/login')}>Login</Button></Grid>}

        <Grid item><Button variant={"contained"} startIcon={<MdSettings size={40}/>}
                                   onClick={() => navigate('/settings')}>Settings</Button></Grid>


    </Grid>
}

export default HomeScreen;