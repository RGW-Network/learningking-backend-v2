'use client';
import { KEY_BEARER_TOKEN, KEY_WFP_DETAILS, KEY_IS_LOGGED_IN, KEY_RANDOM_DEVICE_ID, KEY_REFRESH_TOKEN, KEY_SELECTED_APPLICATION_MODULE, KEY_USER_DETAILS } from '../constants/Constants';
import { Module } from '../constants/Module';
import { generateId, isNotEmpty } from './Utils';

export class UserSessionUtils {
    /**
     * This is used to get the user's bearer token.
     *
     * @returns
     */
    static getBearerToken() {
        return localStorage.getItem(KEY_BEARER_TOKEN);
    }

    static isLoggedIn() {
        console.log('Login Token: ' + this.getBearerToken());
        return isNotEmpty(this.getBearerToken());
    }

    /**
     * This is used to get the user's refresh token.
     *
     * @returns
     */
    static getRefreshToken() {
        return localStorage.getItem(KEY_REFRESH_TOKEN);
    }
    /**
     * This method is used to clear the localstorage and redirect the user to the login screen
     */
    static clearLocalStorageAndLogout() {
        // remove all
        localStorage.clear();
        localStorage.setItem(KEY_BEARER_TOKEN, '');
        localStorage.setItem(KEY_REFRESH_TOKEN, '');
        localStorage.setItem(KEY_USER_DETAILS, '');
        localStorage.setItem(KEY_IS_LOGGED_IN, '');
        localStorage.setItem(KEY_RANDOM_DEVICE_ID, '');
        window.location.href = '/auth/login';
    }

    /**
     * This method is use to set the user's bearer token.
     *
     * @param bearerToken
     */
    static setUserAuthToken(bearerToken: string) {
        localStorage.setItem(KEY_BEARER_TOKEN, bearerToken);
    }

    /**
     * This method is used to set the user's refresh token.
     *
     * @param refreshToken
     */
    static setUserRefreshToken(refreshToken: string) {
        localStorage.setItem(KEY_REFRESH_TOKEN, refreshToken);
    }

    /**
     * This method is used to save a JSON object containing user details to local storage.
     *
     * @param userDetails
     */
    static setUserDetails(userDetails: {}) {
        localStorage.setItem(KEY_USER_DETAILS, JSON.stringify(userDetails));
        localStorage.setItem(KEY_IS_LOGGED_IN, 'loggedIn');
    }

    /**
     * This method is used to get a JSON object containing user details
     * @returns
     */
    static getUserDetails() {
        if (localStorage.getItem(KEY_USER_DETAILS)) return JSON.parse(localStorage.getItem(KEY_USER_DETAILS)!);
        return {};
    }
    /**
     * This method is used to determine whether the logged in user is a super admin
     *
     */
    static isSuperAdmin() {
        let userDetails = UserSessionUtils.getUserDetails();
        return userDetails?.isASuperAdmin;
    }

    /**
     * This returns the email of the currently logged in user
     * @returns string
     */
    static getUserEmail() {
        let userDetails = this.getUserDetails();
        return userDetails?.email;
    }

    /**
     *
     * @param module
     */
    static setSelectedUserModule(module: Module) {
        localStorage.setItem(KEY_SELECTED_APPLICATION_MODULE, module.toString());
    }

    /**
     *
     */
    static getDeviceId() {
        var deviceId = localStorage.getItem(KEY_RANDOM_DEVICE_ID);
        if (deviceId === null || deviceId.length === 0) localStorage.setItem(KEY_RANDOM_DEVICE_ID, generateId(40));
        return localStorage.getItem(KEY_RANDOM_DEVICE_ID);
    }
}
