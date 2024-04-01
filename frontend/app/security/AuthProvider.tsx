// src/context/auth-context.js
import React from 'react';
import { useRouter } from 'next/router';

const AuthContext = React.createContext({});
const { Provider } = AuthContext;

const AuthProvider = ({ children }: any) => {
    const [authState, setAuthState] = React.useState({
        token: ''
    });

    const setUserAuthInfo = ({ data }: any) => {
        const token: string = data.data;
        localStorage?.setItem('token', data.data);

        setAuthState({
            token
        });
    };

    // checks if the user is authenticated or not
    const isUserAuthenticated = () => {
        if (!authState.token) {
            return false;
        }
    };

    return (
        <Provider
            value={{
                authState,
                setAuthState: (userAuthInfo: any) => setUserAuthInfo(userAuthInfo),
                isUserAuthenticated
            }}
        >
            {children}
        </Provider>
    );
};

export { AuthContext, AuthProvider };
