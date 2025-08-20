import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { AuthProvider } from 'react-oauth2-code-pkce'
import {authConfig} from "./authConfig";

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <AuthProvider
            authConfig = {authConfig}

        >

            <App/>
        </AuthProvider>
    </StrictMode>,
)
