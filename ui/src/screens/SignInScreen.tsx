import {
    Button,
    FormControl,
    Grid, IconButton, InputAdornment,
    InputLabel,
    OutlinedInput,
    TextField,
    Typography,
    useTheme
} from "@mui/material";
import {RouteProps, useNavigate} from "react-router";
import { MdVisibility, MdVisibilityOff} from "react-icons/md";
import {IoArrowBack} from "react-icons/io5"
import {Dispatch, useContext, useEffect, useState} from "react";
import {loginApi} from "../services/apiConfig";
import {ROUTE_HOME, ROUTE_LOGIN} from "../constants/routes";
import {Alert} from "@mui/lab";
import {AuthAction, AuthDispatchContext, getSavedUrlAndClear} from "../utils/auth/auth";
import {LoginRequest, SignupRequest} from "@react-springboot-appengine-template/api/dist";

const SignInScreen: React.FC<RouteProps> = () => {
    const theme = useTheme();
    const navigate = useNavigate();
    const dispatch = useContext<Dispatch<AuthAction> | null>(AuthDispatchContext);
    const [errorMessage, setErrorMessage] = useState<string>();
    const [formValid, setFormValid] = useState<boolean>(false);
    const [showPassword, setShowPassword] = useState<boolean>(false);
    const [loginRequest, setLoginRequest] = useState<LoginRequest>({
        email: '',
        password: ''
    });


    const handleChange =
        (prop: keyof SignupRequest) => (event: React.ChangeEvent<HTMLInputElement>) => {
            setLoginRequest({...loginRequest, [prop]: event.target.value});
        };

    const validateForm = () => {
        setFormValid(
            loginRequest.email.length > 0
            &&
            loginRequest.password.length > 0
        );
    }

    const onSignIn = () => {
        if( dispatch ) {
            dispatch({type: 'authenticating'});
        }
        loginApi.authenticateUser({loginRequest: loginRequest}).then((resp) => {
            if (dispatch) {
                dispatch({type: 'success', authState: {...resp, authenticating: false}});
            }

            const url = getSavedUrlAndClear();
            if( url ) {
                navigate(url);
            }
            else  {
                navigate(ROUTE_HOME);
            }
        },(err) => {
            if( err.status === 401) {
                setErrorMessage('Unable to sign in. Please check your email address and password');
            }
            if( dispatch ) {
                dispatch({type: 'failure',error: JSON.stringify(err)});
            }
        });
    }

    useEffect(validateForm, [loginRequest])

    return <Grid
        container
        sx={{backgroundColor: theme.palette.background.paper, height: '100vh'}}
        direction="column"
        justifyContent="center"
        alignItems="stretch"
        spacing={1}
    >

        <Grid item container justifyContent="start"> <Button startIcon={<IoArrowBack/>}
                                                             color={"secondary"}
                                                             onClick={() => navigate(ROUTE_LOGIN)}/>
        </Grid>

        <Grid item container justifyContent="start"> <Typography variant={"h4"}
                                                                 sx={{color: theme.palette.primary.dark, m: 2}}>Sign in
            to your account</Typography>
        </Grid>

        <Grid item container justifyContent="center" alignItems={"stretch"}>
            <TextField id="email" label="Your Email" variant="outlined"
                       onChange={handleChange('email')}
            />
        </Grid>

        <Grid item container justifyContent="center" alignItems={"stretch"}>
            <FormControl  variant="outlined">
                <InputLabel htmlFor="outlined-adornment-password">Password</InputLabel>
                <OutlinedInput
                    id="outlined-adornment-password"
                    type={showPassword ? 'text' : 'password'}
                    value={loginRequest.password}
                    onChange={handleChange('password')}
                    endAdornment={
                        <InputAdornment position="end">
                            <IconButton
                                aria-label="toggle password visibility"
                                onClick={() => setShowPassword(!showPassword)}
                                onMouseDown={e => e.preventDefault()}
                                edge="end"
                            >
                                {showPassword ? <MdVisibilityOff/> : <MdVisibility/>}
                            </IconButton>
                        </InputAdornment>
                    }
                    label="Password"
                />
            </FormControl>

        </Grid>


        <Grid item container justifyContent="end">
            <Typography variant={"body2"}
                        sx={{color: '#c4c4c4', fontSize: '12px', marginRight:2}}> <a href={"/forgot-password"}>Forgot your password?</a></Typography>
        </Grid>

        <Grid item container justifyContent="center" alignItems={"stretch"} >
            <Button variant={"outlined"}
                    disabled={!formValid}
                    onClick={onSignIn}
                    sx={{width: '100%', marginRight: '32px'}}
            >Sign in</Button>
        </Grid>

        <Grid item container justifyContent="center" alignItems={"stretch"} >
            {errorMessage && <Alert severity={"error"}>{errorMessage}</Alert>}
        </Grid>


    </Grid>

}

export default SignInScreen;
