import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import {BrowserRouter} from "react-router-dom";
import {StompSessionProvider} from "react-stomp-hooks";
import {WS_ENDPOINT} from "./constants/websockets";
//import {ThemeProvider} from "@mui/material/styles";
// import defaultTheme from "./themes/theme1";

const loader = document.querySelector('.loader');

const showLoader = () => loader?.classList.remove('loader--hide');
// @ts-ignore
const hideLoader = () => loader?.classList.add('loader--hide');

createRoot(document.getElementById('root')!).render(
    <BrowserRouter>

  <StrictMode>
      {/*<ThemeProvider theme={defaultTheme}>*/}
          <StompSessionProvider url={WS_ENDPOINT}>
              <App
                  hideLoader={hideLoader}
                  showLoader={showLoader}
              />
          </StompSessionProvider>
      {/*</ThemeProvider>*/}
  </StrictMode>,
    </BrowserRouter>
)
