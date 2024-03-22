
export interface IBaseApiService {

    /**
     * This method is used to make a GET API call to the provided API endpoint url. 
     * It returns any object returned from the server.
     * 
     * @param apiEndpoint
     * @param queryParameters
     */
    getRequest: (apiEndpoint:string,   queryParameters: {}) => any;

    /**
     * This method is used to make a POST API call to the provided API endpoint url. 
     * It returns any object returned from the server.
     * 
     * @param apiEndpoint
     * @param requestBody
     * @param requestHeaders
     */
    postRequest: (apiEndpoint:string,  requestBody: {}, requestHeaders: {}) => any;

    /**
     * This method is used to obtain a refresh token from the server
     */
    refreshTokenRequest: () => any;

}