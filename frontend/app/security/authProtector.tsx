'use client';
import { useEffect, useState } from 'react';
import { redirect } from 'next/navigation';
import { KEY_BEARER_TOKEN } from '../constants/Constants';
import { isNotEmpty } from '../utils/Utils';

export default function authProtector(Component: any) {
    return function IsAuth(props: any) {
        const [auth, setAuth] = useState<boolean | null>(null);
        console.log('Checking auth protector');
        useEffect(() => {
            if (typeof window !== 'undefined' && isNotEmpty(localStorage.getItem(KEY_BEARER_TOKEN))) {
                console.log('Auth state: true');
                setAuth(true);
            } else {
                console.log('Auth state: false');
                setAuth(false);
            }
        }, []);

        if (auth == false) {
            redirect('/auth/login');
        }
        if (auth == true) {
            return <Component {...props} />;
        }
    };
}
