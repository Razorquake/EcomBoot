import './App.css'
import {useContext, useEffect, useState} from "react";
import {AuthContext} from "react-oauth2-code-pkce";

function App() {
    const {token, tokenData, logIn, logOut} = useContext(AuthContext);
    const [message, setMessage] = useState<string | null>(null);
    useEffect(() => {
        if (token) {
            fetchHello();
            console.log(tokenData)
        }
    }, [token]);
    const fetchHello = async () => {
        try {
            const response = await fetch(
                "http://localhost:8082/api/home",
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'text/plain; charset=utf-8'
                    }
                }
            );
            if (response.ok) {
                const data = await response.json();
                setMessage(data.message);
            }
        } catch (e) {
            console.error(e);
        }
    };

  return (
    <div style={{padding: '2rem'}}>
        <h1>OAuth2 PKCE Demo</h1>
        <div>
            {!token ? (
                <button onClick={()=>logIn}>Login</button>
            ) : (
                <>
                    <button onClick={()=>logOut}>Logout</button>
                    <h3>Message from api:</h3>
                    <p>{message}</p>
                    <p>Token: {token}</p>
                    <pre>Token Data: {JSON.stringify(tokenData, null, 2)}</pre>
                </>


            )}
        </div>
    </div>
  )
}

export default App
