import {
    Grid, Typography
} from "@mui/material";
import {RouteComponentProps} from "@reach/router";
import  "./HomeScreen.css";
import {RiCheckboxCircleFill} from "react-icons/all";


const SuccessScreen: React.FC<RouteComponentProps> = () => {

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

        <Grid item  sx={{borderRadius:150}}>
            <RiCheckboxCircleFill size={144} color={"#727f8f"} />
        </Grid>


        <Grid item >
            <Typography variant={"h3"} color={"white"}>Success!</Typography>
        </Grid>

        <Grid item  justifyContent={"center"} alignItems={"center"}>
            <Typography sx={{px:8,py:2,textAlign:'center'}} variant={"body2"}  color={"white"}>Now check your email for confirmation link.</Typography>
        </Grid>


        <Grid item sx={{marginTop:20}}>
            <Typography variant={"body1"}  color={"white"}>Didnt receive it? <a href={"#"}>Click here</a></Typography>
        </Grid>


    </Grid>

}

export default SuccessScreen;