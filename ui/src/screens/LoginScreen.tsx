import {Button, Drawer, Grid, Paper, Typography} from "@mui/material";
import {RouteProps, useNavigate} from "react-router";
import {IoLogoApple, IoLogoGoogle } from "react-icons/io";
import { MdMail} from "react-icons/md";
import { loginApi} from "../services/apiConfig";
import { useEffect, useState} from "react";
import {createStyles, makeStyles} from "@mui/styles";
import {   ROUTE_SIGNUP} from "../constants/routes";
import {OauthUrl} from "@react-springboot-appengine-template/api/dist";

interface LoginScreenProps  {
}

const useStyles = makeStyles(() =>
    createStyles({
        root: {
            width: '100%',
            height: '100vh',
        },
        panel: {
            borderRadius: '36px 36px 0px 0px !important',
            padding: 24,
            height: '100%'
        }
    }),
);

const LoginScreen: React.FC<LoginScreenProps & RouteProps> = (props) => {
    const classes = useStyles();

    const navigate = useNavigate();
    const [drawerOpen, setDrawerOpen] = useState<boolean>(false);
    const [oauthUrls, setOauthUrls] = useState<OauthUrl[]>([]);

    const loadUrls = () => {
        loginApi.getOauthUrls().then((urls) => {
            setOauthUrls(urls);
            setDrawerOpen(true);
        });
    }

    useEffect(loadUrls, []);

    const getIconForAuth = (auth: string): JSX.Element => {
        if (auth === 'Google') {
            return <IoLogoGoogle/>
        } else {
            return <IoLogoApple/>
        }
    }

    return <Grid
        container
        direction="column"
        justifyContent="center"
        alignItems="stretch"
        spacing={2}
    >
        <Grid item xs={1} sx={{marginTop: 16}}>
            <img alt={"logo"} src={"/logo192.png"}/>
        </Grid>

        <Drawer
            anchor={'bottom'}
            open={drawerOpen}
            PaperProps={{elevation: 0, style: {backgroundColor: "transparent"}}}
        >
            <Paper elevation={2} className={classes.panel}>
                <Grid
                    container
                    direction="column"
                    justifyContent="center"
                    alignItems="stretch"
                    spacing={4}
                >

                    <Grid item container justifyContent="center"> <Typography variant={"h4"}>Create an
                        account</Typography>
                    </Grid>
                    {oauthUrls.map((oa, i) => <Grid item container justifyContent="center" key={i}>
                        <Button key={oa.key} variant={"outlined"}
                                size={"large"}
                                startIcon={getIconForAuth(oa.key)}
                                href={`${oa.href.startsWith('/') ? '' : '/'}${oa.href + props.path?.search}`}>Continue
                            with {oa.key}</Button>
                    </Grid>)}

                    <Grid item container justifyContent="center">
                        <Button variant={"outlined"}
                                size={"large"}
                                onClick={() => navigate(ROUTE_SIGNUP)}
                                startIcon={<MdMail/>}
                        >Sign up with email</Button>
                    </Grid>

                    <Grid item container justifyContent="center"> <Typography variant={"body1"}>Already have an
                        account? <a href={"/signin"}>Sign In</a></Typography></Grid>
                    <Grid item container justifyContent="center"> <Typography variant={"caption"}
                                                                              >By
                        signing up you
                        agree to our <a href={"/privacy-policy"}>Privacy Policy and Terms</a></Typography></Grid>

                </Grid>

            </Paper>
        </Drawer>

    </Grid>

}

export default LoginScreen;
