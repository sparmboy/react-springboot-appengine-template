import {Button, createStyles, Grid, makeStyles, Paper, Theme, Typography} from "@material-ui/core";
import {RouteComponentProps, RouterProps, useNavigate} from "@reach/router";
import {IoLogoGoogle} from "react-icons/all";
import { loginApi} from "../services/apiConfig";
import {useEffect, useState} from "react";
import {OauthUrl} from "@react-springboot-appengine-template/api/dist";

interface LoginScreenProps extends RouteComponentProps<RouterProps> {
    authenticated: boolean
}

//const DEFAULT_AUTH_URLS = [{href: GOOGLE_AUTH_URL, key: 'Google'}];

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
        root: {
            width: '100%',
            height: '100vh',
            backgroundColor: theme.palette.grey.A100,
        },
        panel: {
            padding: 24,
            margin: 4
        }
    }),
);

const LoginScreen: React.FC<LoginScreenProps & RouteComponentProps> = (props) => {
    const classes = useStyles();
    const navigate = useNavigate();
    const [oauthUrls, setOauthUrls] = useState<OauthUrl[]>([]);
    const loadUrls = () => {
        loginApi.getOauthUrls().then(setOauthUrls);
    }
    useEffect(loadUrls, []);

    if (props.authenticated) {
        console.log('Authenticated props=',props);
        if(props.path){
            navigate(props.path);
        }else {
            navigate('/');
        }
    }
    return <Grid
        container
        direction="column"
        justify="center"
        alignItems="center"
        className={classes.root}
    >
        <Grid item>
            <Paper elevation={2} className={classes.panel}>
                <Grid
                    container
                    direction="column"
                    justifyContent="center"
                    alignItems="center"
                    spacing={4}
                >
                    <Grid item> <Typography variant={"subtitle1"}>To continue, please login with one of the following
                        options:</Typography>
                    </Grid>
                    {oauthUrls.map((oa,i) => <Grid item key={i}>
                        <Button key={oa.key} color={"primary"} variant={"contained"}
                                startIcon={<IoLogoGoogle/>}
                                href={`${oa.href.startsWith('/') ? '' : '/'}${oa.href+props.location?.search}`}>Login with {oa.key}</Button>
                    </Grid>)}

                </Grid>

            </Paper>
        </Grid>

    </Grid>

}

export default LoginScreen;