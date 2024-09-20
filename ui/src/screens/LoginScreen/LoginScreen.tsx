import { Button, Drawer, Grid2 as Grid, Paper, Typography} from "@mui/material";
import {RouteProps, useNavigate} from "react-router";
import {IoLogoApple, IoLogoGoogle } from "react-icons/io";
import { MdMail} from "react-icons/md";
import { loginApi} from "../../services/apiConfig";
import React, { useEffect, useState} from "react";
import {createStyles, makeStyles} from "@mui/styles";
import {   ROUTE_SIGNUP} from "../../constants/routes";
import {OauthUrl} from "@react-springboot-appengine-template/api/dist";
import {Alert} from "@mui/lab";
import "./LoginScreen.scss";

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

const LoginScreen: React.FC<RouteProps> = (props) => {
    const classes = useStyles();

    const navigate = useNavigate();
    const [drawerOpen, setDrawerOpen] = useState<boolean>(false);
    const [oauthUrls, setOauthUrls] = useState<OauthUrl[]>([]);
    const [errorMessage,setErrorMessage] = useState<string>();

    const loadUrls = () => {
        loginApi.getOauthUrls().then((urls) => {
            setOauthUrls(urls);
            setDrawerOpen(true);
        }).catch( () => {
            setErrorMessage("Sorry! Looks like there is an error with our system at the moment. Please come back later.");
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
        id={"loginScreen"}
        container
        direction="column"
        justifyContent="center"
        alignItems="stretch"
        spacing={2}
    >

        <Grid size={1}  className={"logo-container"}>
            <img alt={"logo"} src={"/logo192.png"}/>
        </Grid>

        {errorMessage && <Alert severity={"error"} variant={"outlined"} >{errorMessage}</Alert>}
        {!errorMessage &&
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

                    <Grid justifyContent="center">
                        <Typography variant={"h4"}>Create an
                        account</Typography>
                    </Grid>
                    {oauthUrls.map((oa, i) => <Grid justifyContent="center" key={i}>
                        <Button key={oa.key} variant={"outlined"}
                                size={"large"}
                                startIcon={getIconForAuth(oa.key)}
                                href={`${oa.href.startsWith('/') ? '' : '/'}${oa.href + props.path?.search}`}>Continue
                            with {oa.key}</Button>
                    </Grid>)}

                    <Grid  justifyContent="center">
                        <Button variant={"outlined"}
                                size={"large"}
                                onClick={() => navigate(ROUTE_SIGNUP)}
                                startIcon={<MdMail/>}
                        >Sign up with email</Button>
                    </Grid>

                    <Grid  justifyContent="center"> <Typography variant={"body1"}>Already have an
                        account? <a href={"/signin"}>Sign In</a></Typography></Grid>
                    <Grid  justifyContent="center"> <Typography variant={"caption"}
                                                                              >By
                        signing up you
                        agree to our <a href={"/privacy-policy"}>Privacy Policy and Terms</a></Typography></Grid>

                </Grid>

            </Paper>
        </Drawer>
        }

    </Grid>

}

export default LoginScreen;
