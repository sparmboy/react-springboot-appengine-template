import {
    Button,
    FormControl,
    Grid2 as Grid, IconButton, InputAdornment,
    InputLabel,
    OutlinedInput,
    TextField,
    Typography,
    useTheme
} from "@mui/material";
import {RouteProps, useNavigate} from "react-router";
import {MdVisibility, MdVisibilityOff} from "react-icons/md";
import {IoArrowBack} from "react-icons/io5"
import {useEffect, useState} from "react";
import {loginApi} from "../../services/apiConfig";
import {ROUTE_LOGIN} from "../../constants/routes";
import {Alert} from "@mui/lab";
import {LoginRequest, SignupRequest} from "@react-springboot-appengine-template/api/dist";
import {useAuth} from "../../services/Authentication/Authentication.types";

const SignInScreen: React.FC<RouteProps> = () => {
    const theme = useTheme();
    const navigate = useNavigate();
    const authStore = useAuth();
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
        loginApi.authenticateUser({loginRequest: loginRequest})
            .then(authStore.onSuccess)
            .catch(authStore.onFailure);
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

        <Grid container justifyContent="start"> <Button startIcon={<IoArrowBack/>}
                                                             color={"secondary"}
                                                             onClick={() => navigate(ROUTE_LOGIN)}/>
        </Grid>

        <Grid container justifyContent="start"> <Typography variant={"h4"}
                                                                 sx={{color: theme.palette.primary.dark, m: 2}}>Sign in
            to your account</Typography>
        </Grid>

        <Grid container justifyContent="center" alignItems={"stretch"}>
            <TextField id="email" label="Your Email" variant="outlined"
                       onChange={handleChange('email')}
            />
        </Grid>

        <Grid container justifyContent="center" alignItems={"stretch"}>
            <FormControl variant="outlined">
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


        <Grid container justifyContent="end">
            <Typography variant={"body2"}
                        sx={{color: '#c4c4c4', fontSize: '12px', marginRight: 2}}> <a href={"/forgot-password"}>Forgot your password?</a></Typography>
        </Grid>

        <Grid container justifyContent="center" alignItems={"stretch"}>
            <Button variant={"outlined"}
                    disabled={!formValid}
                    onClick={onSignIn}
                    sx={{width: '100%', marginRight: '32px'}}
            >Sign in</Button>
        </Grid>

        <Grid container justifyContent="center" alignItems={"stretch"}>
            {authStore.errorMessage && <Alert severity={"error"}>{authStore.errorMessage}</Alert>}
        </Grid>


    </Grid>

}

export default SignInScreen;
