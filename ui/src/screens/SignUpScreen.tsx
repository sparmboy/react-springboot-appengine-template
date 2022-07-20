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
import {RouteComponentProps,  useNavigate} from "@reach/router";
import {IoArrowBack,  MdVisibility, MdVisibilityOff} from "react-icons/all";
import {useEffect, useState} from "react";
import {loginApi} from "../services/apiConfig";
import {ROUTE_LOGIN, ROUTE_SUCCESS} from "../constants/routes";
import {SignupRequest} from "@react-springboot-appengine-template/api/dist";


const inputFieldStyle = {marginLeft: 2, marginRight: 2, marginBottom: 1, width: '100%'};

const SignUpScreen: React.FC<RouteComponentProps> = () => {
    const theme = useTheme();
    const navigate = useNavigate();

    const [formValid, setFormValid] = useState<boolean>(false);
    const [showPassword, setShowPassword] = useState<boolean>(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState<boolean>(false);
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [signupRequest, setSignupRequest] = useState<SignupRequest>({
        name:'',
        email: '',
        password: ''
    });


    const handleChange =
        (prop: keyof SignupRequest) => (event: React.ChangeEvent<HTMLInputElement>) => {
            setSignupRequest({...signupRequest, [prop]: event.target.value});
        };

    const validateForm = () => {
        setFormValid(
            signupRequest.email.length > 0
            &&
            signupRequest.password.length > 0
            &&
            signupRequest.password === confirmPassword
        );
    }

    const onSignUp = () => {
        loginApi.registerUser({signupRequest: signupRequest}).then(() => {
            navigate(ROUTE_SUCCESS);
        });
    }

    useEffect(validateForm, [signupRequest, confirmPassword])

    return <Grid
        container
        sx={{backgroundColor: theme.palette.background.paper, height: '100vh'}}
        direction="column"
        justifyContent="center"
        alignItems="stretch"
        spacing={1}
    >

        <Grid item container justifyContent="start"> <Button startIcon={<IoArrowBack/>} color={"secondary"} onClick={()=>navigate(ROUTE_LOGIN)}/>
        </Grid>

        <Grid item container justifyContent="start"> <Typography variant={"h4"}
                                                                 sx={{color: theme.palette.primary.dark, m: 2}}>Create
            an
            account</Typography>
        </Grid>

        <Grid item container justifyContent="center" alignItems={"stretch"}>
            <TextField id="email" label="Your Email" variant="outlined" sx={inputFieldStyle}
                       onChange={handleChange('email')}
            />
        </Grid>

        <Grid item container justifyContent="center" alignItems={"stretch"}>
            <FormControl sx={inputFieldStyle} variant="outlined">
                <InputLabel htmlFor="outlined-adornment-password">Create a strong password</InputLabel>
                <OutlinedInput
                    id="outlined-adornment-password"
                    type={showPassword ? 'text' : 'password'}
                    value={signupRequest.password}
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
                    label="Create a strong password"
                />
            </FormControl>

        </Grid>

        <Grid item container justifyContent="center" alignItems={"stretch"}>
            <FormControl sx={inputFieldStyle} variant="outlined">
                <InputLabel htmlFor="outlined-adornment-password">Repeat password</InputLabel>
                <OutlinedInput
                    id="outlined-adornment-password"
                    type={showConfirmPassword ? 'text' : 'password'}
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    endAdornment={
                        <InputAdornment position="end">
                            <IconButton
                                aria-label="toggle password visibility"
                                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                                onMouseDown={e => e.preventDefault()}
                                edge="end"
                            >
                                {showConfirmPassword ? <MdVisibilityOff/> : <MdVisibility/>}
                            </IconButton>
                        </InputAdornment>
                    }
                    label="Repeat password"
                />
            </FormControl> </Grid>


        <Grid item container justifyContent="center" alignItems={"stretch"} sx={inputFieldStyle}>
            <Button variant={"outlined"}
                    disabled={!formValid}
                    onClick={onSignUp}
                    sx={{ width: '100%', marginRight: '32px'}}
            >Create an account</Button>
        </Grid>


        <Grid item container justifyContent="center"> <Typography variant={"caption"}>By signing up you
            agree to our <a href={"/privacy-policy"}>Privacy Policy and Terms</a></Typography></Grid>


    </Grid>

}

export default SignUpScreen;