import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import defaultTheme from "./themes/theme1";
import {ThemeProvider} from "@mui/material/styles";

const loader = document.querySelector('.loader');
// @ts-ignore
const showLoader = () => loader.classList.remove('loader--hide');
// @ts-ignore
const hideLoader = () => loader.classList.add('loader--hide');

ReactDOM.render(
    <React.StrictMode>
        <ThemeProvider theme={defaultTheme}>

            <App
                hideLoader={hideLoader}
                showLoader={showLoader}
            />
        </ThemeProvider>
    </React.StrictMode>,
    document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
