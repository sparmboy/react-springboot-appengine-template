import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import App from './App'
import './index.css'
import {BrowserRouter} from "react-router-dom";
import {StompSessionProvider} from "react-stomp-hooks";
import {WS_ENDPOINT} from "./constants/websockets";
import {ThemeProvider} from "@mui/material/styles";
import {defaultTheme} from "./themes/theme1";

createRoot(document.getElementById('root')!).render(
    <BrowserRouter >
        <StrictMode>
            <ThemeProvider theme={defaultTheme}>
            <StompSessionProvider url={WS_ENDPOINT}>
                <App/>
            </StompSessionProvider>
            </ThemeProvider>
        </StrictMode>,
    </BrowserRouter>
)
