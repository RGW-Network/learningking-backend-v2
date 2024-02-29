/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.utilities;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sers.webutils.model.exception.ValidationFailedException;

/**
 *
 */
public class WordPressClient {

    public static String BASE_URL = "https://lktechops.wpengine.com";

    /**
     * Sends a massage through the provides JSON Object
     *
     * @param path
     * @param headers
     * @param requestJson
     * @return
     * @throws IOException
     * @throws org.sers.webutils.model.exception.ValidationFailedException
     * @throws org.json.JSONException
     */
    public static WordpressResponse doLogin(JSONObject requestJson, Map<String, String> headers) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            System.out.println("Starting WP Auth...");

            HttpPost request = new HttpPost(BASE_URL + "/wp-json/learndash/token");
            StringEntity params = new StringEntity(requestJson.toString());
            request.addHeader("content-type", "application/json");
            if (headers != null) {
               for (Map.Entry<String,String> entry : headers.entrySet()) {
                 request.addHeader(entry.getKey(), entry.getValue());
               }
            }
            request.setEntity(params);
            System.out.println("Excecuting Request...");

            HttpResponse response = httpClient.execute(request);

            // CONVERT RESPONSE TO STRING
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("Recieved String Response========>\n" + result);

            // CONVERT RESPONSE STRING TO JSON ARRAY
            JSONObject jsonResult = new JSONObject(result);

            System.out.println("=======Recieved Json Response========\n" + jsonResult.toString());

            // Creating a Gson Object 
            Gson gson = new Gson();

            // Converting json to object 
            // first parameter should be prpreocessed json 
            // and second should be mapping class 
            WordpressResponse egoResponse = gson.fromJson(jsonResult.toString(), WordpressResponse.class);
egoResponse.statusCode=response.getStatusLine().getStatusCode();
            httpClient.close();
            // return object 
            return egoResponse;

        } catch (Exception ex) {
            System.out.println("=======Some Error Occured=====");
            System.out.println(ex);
            return new WordpressResponse(500, ex.getMessage());
            // handle exception here
        }

    }

    
    
    /**
     * Sends a massage through the provides JSON Object
     *
     * @param path
     * @param headers
     * @param requestJson
     * @return
     * @throws IOException
     * @throws org.sers.webutils.model.exception.ValidationFailedException
     * @throws org.json.JSONException
     */
    public static WordpressRegistrationResponse doRegister(JSONObject requestJson, Map<String, String> headers) {

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            System.out.println("Starting WP Reg...");

            HttpPost request = new HttpPost(BASE_URL + "/wp-json/wp/v2/users");
            StringEntity params = new StringEntity(requestJson.toString());
            request.addHeader("content-type", "application/json");
            if (headers != null) {
              for (Map.Entry<String,String> entry : headers.entrySet()) {
                 request.addHeader(entry.getKey(), entry.getValue());
               }
            }
            request.setEntity(params);
            System.out.println("Excecuting Request...");

            HttpResponse response = httpClient.execute(request);

            // CONVERT RESPONSE TO STRING
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("Recieved String Response========>\n" + result);

            // CONVERT RESPONSE STRING TO JSON ARRAY
            JSONObject jsonResult = new JSONObject(result);

            System.out.println("=======Recieved Json Response========\n" + jsonResult.toString());

            // Creating a Gson Object 
            Gson gson = new Gson();

            // Converting json to object 
            // first parameter should be prpreocessed json 
            // and second should be mapping class 
            WordpressRegistrationResponse egoResponse = gson.fromJson(jsonResult.toString(), WordpressRegistrationResponse.class);
            egoResponse.statusCode=response.getStatusLine().getStatusCode();
httpClient.close();
            // return object 
            return egoResponse;

        } catch (Exception ex) {
            System.out.println("=======Some Error Occured=====");
            System.out.println(ex);
            return new WordpressRegistrationResponse(500, ex.getMessage());
            // handle exception here
        }

    }

  
    public static EgoResponse fetchEgosmsBalance(String BASE_URL, String username, String password) throws ValidationFailedException {
        final JSONObject egoSmsDetails = new JSONObject();
        final Gson gson = new Gson();
        EgoResponse balanceResponse = null;

        try {
            egoSmsDetails.put("method", "Balance");
            egoSmsDetails.put("userdata",
                    new JSONObject().put("username", username)
                            .put("password", password));
            WebResource resource = Client.create(new DefaultClientConfig())
                    .resource(BASE_URL + "/api/v1/json/");
            final Builder webResource = resource.accept(MediaType.APPLICATION_JSON);
            webResource.type(MediaType.APPLICATION_JSON);
            ClientResponse clientResponse = webResource.post(ClientResponse.class, egoSmsDetails.toString());
            String stringResponse = clientResponse.getEntity(String.class);
            if (clientResponse.getStatus() == 200) {

                balanceResponse = gson.fromJson(stringResponse, EgoResponse.class);
                System.out.println("Account balance " + balanceResponse.getBalance());
                return balanceResponse;
            } else {
                throw new ValidationFailedException("Invalid Ego Credential");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return balanceResponse;
    }

}
