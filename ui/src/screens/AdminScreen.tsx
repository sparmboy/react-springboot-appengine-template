import {RouteComponentProps, RouterProps} from "@reach/router";
import {
    Grid, Paper, Switch, Typography,
} from "@mui/material";
import './HomeScreen.css';
import {BackButton} from "../components/navigation/BackButton";
import {useState} from "react";
import {WebSocketEventDTO} from "../domain/webSocketEventDTO";
import {WS_TOPIC_MY_EVENTS} from "../constants/websockets";
import {useSubscription} from "react-stomp-hooks";


interface AdminScreenProps extends RouteComponentProps<RouterProps> {
}

const AdminScreen: React.FC<AdminScreenProps & RouteComponentProps> = () => {

    const [status,setStatus] = useState<boolean>(false);

    const onMessage = (msg: any) => {
        const event: WebSocketEventDTO = {...JSON.parse(msg.body) as WebSocketEventDTO};
        setStatus(event.status);
    }

    useSubscription(WS_TOPIC_MY_EVENTS, onMessage);

    return <Grid
        container
        direction="column"
        justifyContent="center"
        alignItems="center"
        spacing={2}
    >

        <BackButton/>

        <Grid item sx={{marginTop: 16}}>
            <img alt="logo" src={"/logo192.png"}/>
        </Grid>

        <div className="circle-right"/>

        <Grid item container justifyContent="center" alignItems={"stretch"} >
            <Paper elevation={2} sx={{p:5}}>
                <Typography> This is the admin page. The switch below is being controlled by web socket events from the server</Typography>
                <Switch checked={status}/>
            </Paper>
        </Grid>

    </Grid>
}

export default AdminScreen;