import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import defaultTheme from "./themes/theme1";
import {ThemeProvider} from "@mui/material/styles";
import {WS_ENDPOINT} from "./constants/websockets";
import {StompSessionProvider} from "react-stomp-hooks";
import {BrowserRouter} from "react-router-dom";

const loader = document.querySelector('.loader');
// @ts-ignore
const showLoader = () => loader.classList.remove('loader--hide');
// @ts-ignore
const hideLoader = () => loader.classList.add('loader--hide');

ReactDOM.render(
    <BrowserRouter>
    <React.StrictMode>
        <ThemeProvider theme={defaultTheme}>
            <StompSessionProvider url={WS_ENDPOINT}>
            <App
                hideLoader={hideLoader}
                showLoader={showLoader}
            />
            </StompSessionProvider>
        </ThemeProvider>
    </React.StrictMode>
    </BrowserRouter>,

    document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
