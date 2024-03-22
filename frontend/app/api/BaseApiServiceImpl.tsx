import { BACKEND_KNOWN_HTTP_ERROR_CODES } from "../constants/Constants";
import { INTERNAL_SERVER_ERROR } from "../constants/ErrorMessages";
import { BASE_URL_ENDPOINT_PATH } from "../utils/EndpointsUtils";
import { UserSessionUtils } from "../utils/UserSessionUtils";
import { IBaseApiService } from "./IBaseApiService";

export class BaseApiServiceImpl implements IBaseApiService {
  apiEndpoint: string;
  requestHeaders: {} = {
    "Content-Type": "application/json",
    Authorization: "Bearer " + UserSessionUtils.getBearerToken(),
    deviceId: UserSessionUtils.getDeviceId(),
  };

  multiPartRequestHeaders: {} = {
    // "Content-Type": "multipart/form-data; boundary=??",//- Upload with form data wont work if you specify this header
    Authorization: "Bearer " + UserSessionUtils.getBearerToken(),
    deviceId: UserSessionUtils.getDeviceId(),
  };

  /**
   * This is constructor is used to initialize the API service endpoint to be used for this call.
   *
   * @param apiEndpoint
   */
  constructor(apiEndpoint: string) {
    this.apiEndpoint = BASE_URL_ENDPOINT_PATH + apiEndpoint;
  }

  /**
   * This method is used to make a GET api request to the provided constructor API endpoint.
   *
   * @param queryParameters
   * @returns
   */
  async getRequest(queryParameters: {}) {
    return await fetch(
      this.apiEndpoint + "?" + new URLSearchParams(queryParameters),
      {
        method: "GET",
        headers: this.requestHeaders,
      }
    );
  }

  /**
   * This method is used to make a GET api request to the provided constructor API endpoint.
   * This returns a JSON response or redirects to the login screen if a 401 is detected.
   *
   * @param queryParameters
   * @returns
   */
  async getRequestWithJsonResponse(queryParameters: {}) {
    return this.getRequest(queryParameters).then(async (response) => {
      if (response.ok) {
        return response.json();
      } else if (BACKEND_KNOWN_HTTP_ERROR_CODES.includes(response.status)) {
        let data: any = await response.json();
        let errorMessage: string =
          data?.responseMessage ?? INTERNAL_SERVER_ERROR;
        throw new TypeError(errorMessage);
      } else if (response.status === 401) {
        UserSessionUtils.clearLocalStorageAndLogout();
      } else {
        throw new TypeError(INTERNAL_SERVER_ERROR);
      }
    });
  }

  /**
   * This method is used to make a POST MULTIPART API request to the provided constructor endpoint.
   *
   * @param requestBody
   * @returns
   */
  async postRequestMultiPart(requestBody: FormData) {
    return await fetch(this.apiEndpoint, {
      method: "POST",
      headers: this.multiPartRequestHeaders,
      body: requestBody,
    });
  }
  /**
   * This method is used to make a POST API request to the provided constructor endpoint.
   *
   * @param requestBody
   * @returns
   */
  async postRequest(requestBody: {}, requestHeaders: {}) {
    return await fetch(this.apiEndpoint, {
      method: "POST",
      headers: requestHeaders,
      body: requestBody !== null ? JSON.stringify(requestBody) : "",
    });
  }

  /**
   * This method is used to make a POST API request to the provided constructor endpoint.
   * This returns a JSON response or redirects to the login screen if a 401 is detected.
   *
   * @param requestBody
   * @returns
   */
  async postRequestWithJsonResponse(requestBody: any) {
    return this.postRequest(requestBody, this.requestHeaders).then(
      async (response) => {
        if (response.ok) {
          return response.json();
        } else if (BACKEND_KNOWN_HTTP_ERROR_CODES.includes(response.status)) {
          let data: any = await response.json();
          let errorMessage: string = data?.message ?? INTERNAL_SERVER_ERROR;
          throw new TypeError(errorMessage);
        } else if (response.status === 401) {
          UserSessionUtils.clearLocalStorageAndLogout();
        } else {
          throw new TypeError(INTERNAL_SERVER_ERROR);
        }
      }
    );
  }

  /**
   * This method is used to make a POST API request to the provided constructor endpoint.
   * This returns a JSON response or redirects to the login screen if a 401 is detected.
   *
   * @param requestBody
   * @returns
   */
  async postMultipartWithJsonResponse(requestBody: FormData) {
    return this.postRequestMultiPart(requestBody).then(async (response) => {
      if (response.ok) {
        return response.json();
      } else if (BACKEND_KNOWN_HTTP_ERROR_CODES.includes(response.status)) {
        let data: any = await response.json();
        let errorMessage: string = data?.message ?? INTERNAL_SERVER_ERROR;
        throw new TypeError(errorMessage);
      } else if (response.status === 401) {
        UserSessionUtils.clearLocalStorageAndLogout();
      } else {
        throw new TypeError(INTERNAL_SERVER_ERROR);
      }
    });
  }

  /**
   * This method is used to obtain a refresh token from the server
   */
  async refreshTokenRequest() {
    let requestBody = { token: UserSessionUtils.getRefreshToken() };
    return await fetch(this.apiEndpoint, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(requestBody),
    });
  }

  /**
   * This method is used to make a PUT API request to the provided constructor endpoint.
   *
   * @param requestBody
   * @returns
   */
  async putRequest(requestBody: {}, requestHeaders: {}) {
    return await fetch(this.apiEndpoint, {
      method: "PUT",
      headers: requestHeaders,
      body: requestBody !== null ? JSON.stringify(requestBody) : "",
    });
  }

  /**
   * This method is used to make a PUT API request to the provided constructor endpoint.
   * This returns a JSON response or redirects to the login screen if a 401 is detected.
   *
   * @param requestBody
   * @returns
   */
  async putRequestWithJsonResponse(requestBody: any) {
    return this.putRequest(requestBody, this.requestHeaders).then(
      async (response) => {
        if (response.ok) {
          return response.json();
        } else if (BACKEND_KNOWN_HTTP_ERROR_CODES.includes(response.status)) {
          let data: any = await response.json();
          let errorMessage: string =
            data?.responseMessage ?? INTERNAL_SERVER_ERROR;
          throw new TypeError(errorMessage);
        } else if (response.status === 401) {
          UserSessionUtils.clearLocalStorageAndLogout();
        } else {
          throw new TypeError(INTERNAL_SERVER_ERROR);
        }
      }
    );
  }
}
