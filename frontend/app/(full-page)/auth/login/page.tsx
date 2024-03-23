'use client';
import React, { useState } from 'react';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { useRouter } from 'next/navigation';
import { Page } from '../../../../types/layout';
import { classNames } from 'primereact/utils';
import { useContext } from 'react';
import { LayoutContext } from '../../../../layout/context/layoutcontext';
import { UserSessionUtils } from '@/app/utils/UserSessionUtils';
import { BaseApiServiceImpl } from '@/app/api/BaseApiServiceImpl';
import { MessageUtils } from '@/app/utils/MessageUtils';

const Login: Page = () => {
    const router = useRouter();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const navigateToDashboard = () => {
        UserSessionUtils.setUserAuthToken('SomeToken');
        router.push('/');
    };
    const { layoutConfig } = useContext(LayoutContext);
    const filledInput = layoutConfig.inputStyle === 'filled';
    /**
     * This submits a save user request to the backoffice
     */
    const doLogin = () => {
        setIsSaving(true);
        new BaseApiServiceImpl('/v1/auth/login')
            .postRequestWithJsonResponse({ username, password, rememberMe: true })
            .then(async (response) => {
                UserSessionUtils.setUserAuthToken(response.accessToken);
                UserSessionUtils.setUserRefreshToken(response.refreshToken);
                UserSessionUtils.setUserDetails(response.user);

                router.push('/');
                setIsSaving(false);
            })
            .catch((error) => {
                setIsSaving(false);
                MessageUtils.showErrorMessage('Login Failed', error.message);
            });
    };

    return (
        <div className={classNames('surface-ground h-screen w-screen flex align-items-center justify-content-center', { 'p-input-filled': filledInput })}>
            <div className="surface-card py-7 px-5 sm:px-7 shadow-2 flex flex-column w-11 sm:w-30rem" style={{ borderRadius: '14px' }}>
                <h1 className="font-bold text-2xl mt-0 mb-2">Learningking Admin</h1>
                <p className="text-color-secondary mb-4">
                    Welcome to the <strong>Learningking Academy</strong>, sign in to continue.
                </p>

                <span className="p-input-icon-left mb-4">
                    <i className="pi pi-user"></i>
                    <InputText disabled={isSaving} value={username} onChange={(e) => setUsername(e.target.value)} type="text" placeholder="Email" className="w-full" />
                </span>

                <span className="p-input-icon-left mb-4">
                    <i className="pi pi-key"></i>
                    <InputText disabled={isSaving} value={password} onChange={(e) => setPassword(e.target.value)} type="password" placeholder="Password" className="w-full" />
                </span>

                <Button loading={isSaving} label="Sign In" className="mb-4" onClick={doLogin}></Button>
            </div>
        </div>
    );
};

export default Login;
