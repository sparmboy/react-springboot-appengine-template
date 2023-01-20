import {Fab, Grid} from "@mui/material";
import {navigate} from "@reach/router";
import {ROUTE_HOME} from "../../constants/routes";
import {IoArrowBack} from "react-icons/all";

export const BackButton = () => <Grid item container justifyContent="start" sx={{m:1}}> <Fab size={"small"} color={"secondary"}
                                                                                  onClick={() => navigate(ROUTE_HOME)}><IoArrowBack/></Fab>
</Grid>
