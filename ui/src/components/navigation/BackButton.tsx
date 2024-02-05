import {Fab, Grid} from "@mui/material";
import {ROUTE_HOME} from "../../constants/routes";
import {IoArrowBack} from "react-icons/io5";
import {useNavigate} from "react-router";

export const BackButton = () => {
    const navigate = useNavigate();
    return <Grid item container justifyContent="start" sx={{m:1}}> <Fab size={"small"} color={"secondary"}
                                                                 onClick={() => navigate(ROUTE_HOME)}><IoArrowBack/></Fab>
    </Grid>
}
