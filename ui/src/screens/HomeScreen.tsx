import {RouteComponentProps, useNavigate, RouterProps} from "@reach/router";
import {Button, createStyles, Grid, makeStyles, Theme} from "@material-ui/core";
import Alert from '@material-ui/lab/Alert';
import {BiPlug, MdList, MdSearch,  MdVerifiedUser} from "react-icons/all";
import {host} from "../services/apiConfig";
import {useEffect, useState} from "react";
import {Stomp} from "@stomp/stompjs";
import SockJS from "sockjs-client";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
        button: {
            width: '100%',
            height: 300,
            fontSize: 30,
            backgroundColor: theme.palette.common.white
        },

    }),
);

interface HomeScreenProps extends RouteComponentProps<RouterProps> {
    authenticated: boolean
}

const HomeScreen: React.FC<HomeScreenProps & RouteComponentProps> = ({authenticated}) => {
    const classes = useStyles();
    const navigate = useNavigate();
    const socket = new SockJS(host + '/session');
    const stompClient = Stomp.over(socket);
    const [message,setMessage] = useState<string>();

    const onSocketMessage =  (msg: any) => {
        console.log('received message', msg.body);
        setMessage(msg.body);
    }

    const onStompDisconnect = () => {
        stompClient.disconnect();
    }

    const onSocketButton = () => {
        stompClient.connect({}, (frame: any) => {
            const stateTopic = '/topic/session';
            console.log('Connected, subscribing to ', stateTopic)
            stompClient.subscribe(stateTopic,onSocketMessage);
        }, onStompDisconnect);
    }

    useEffect(()=>{
        return onStompDisconnect;
    },[])

    return <Grid
        container
        direction="column"
        justifyContent="center"
        alignItems="stretch"
        spacing={2}
    >
        <Grid item>
            <Button className={classes.button}
                    color={"primary"}
                    variant={"outlined"}
                    startIcon={<MdSearch size={40}/>}
                    onClick={() => navigate('/orders/new')}>Place Order</Button>
        </Grid>
        {authenticated &&
        <Grid item>
            <Button className={classes.button}
                    color={"secondary"}
                    variant={"outlined"}
                    startIcon={<MdList size={40}/>}
                    onClick={() => navigate('/orders')}>My Orders</Button>
        </Grid>}
        {!authenticated &&
        <Grid item><Button className={classes.button} color={"secondary"} variant={"outlined"}
                           startIcon={<MdVerifiedUser size={40}/>}
                           onClick={() => navigate('/login')}>Login</Button></Grid>}

        <Grid item><Button variant={"contained"} startIcon={<BiPlug size={40}/>}
                           onClick={() => onSocketButton()}>Click here to open a web socket stream</Button></Grid>

        <Grid item>
            {message && <Alert severity="info">{message}</Alert>}
        </Grid>

    </Grid>
}

export default HomeScreen;