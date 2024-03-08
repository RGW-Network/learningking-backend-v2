/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.byaffe.learningking.utilities;

import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class EgoSMSClient {

    //  public static String BASE_URL = "https://sandbox.egosms.co/api/v1/json/";
    /**
     * Generates a JSON Object from the supplied message object
     *
     * @param phoneNumbers
     * @param message
     * @param senderID
     * @param apiUsername
     * @param apiPassword
     * @return
     * @throws JSONException
     */
    public static JSONObject generateEgoSmsJsonObject(Set<String> phoneNumbers, String message, String senderID, String apiUsername, String apiPassword) throws JSONException, ValidationFailedException {
        JSONObject requestJson = new JSONObject();
        JSONArray msgdata = new JSONArray();
        JSONObject userdata = new JSONObject();
        userdata.put("username", apiUsername);
        userdata.put("password", apiPassword);

        if (phoneNumbers.isEmpty()) {
            throw new ValidationFailedException("Missing/Invalid Phone Numbers(s)");
        }

        for (String phoneNumber : phoneNumbers) {
            msgdata.put(new JSONObject()
                    .put("senderid", senderID)
                    .put("number", phoneNumber)
                    .put("message", message));

        }

        requestJson.put("method", "SendSms");
        requestJson.put("userdata", userdata);
        requestJson.put("msgdata", msgdata);

        System.out.println(requestJson.toString());

        return requestJson;

    }

    public static JSONObject generateEgoSmsJsonObject(String phoneNumber, String message, String senderID, String apiUsername, String apiPassword) throws JSONException, ValidationFailedException {
        JSONObject requestJson = new JSONObject();
        JSONArray msgdata = new JSONArray();
        JSONObject userdata = new JSONObject();
        userdata.put("username", apiUsername);
        userdata.put("password", apiPassword);

       
        msgdata.put(new JSONObject()
                .put("senderid", senderID)
                .put("number", phoneNumber)
                .put("message", message));

        requestJson.put("method", "SendSms");
        requestJson.put("userdata", userdata);
        requestJson.put("msgdata", msgdata);

        System.out.println(requestJson.toString());

        return requestJson;

    }

  

    /**
     * Sends a massage through the provides JSON Object
     *
     * @param requestJson
     * @return
     * @throws IOException
     * @throws org.json.JSONException
     */
    public static EgoResponse sendEgoSmsMessages(String BASE_URL, JSONObject requestJson) throws IOException, ValidationFailedException, JSONException {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        try {
            System.out.println("Starting Send Operation...");

            HttpPost request = new HttpPost(BASE_URL + "/api/v1/json/");
            StringEntity params = new StringEntity(requestJson.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            System.out.println("Excecuting Request...");

            HttpResponse response = httpClient.execute(request);

            // CONVERT RESPONSE TO STRING
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("Recieved String Response========>\n" + result);

            // CONVERT RESPONSE STRING TO JSON ARRAY
            JSONObject jsonResult = new JSONObject(result);

            if (response == null || response.getStatusLine().getStatusCode() != 200) {
                throw new ValidationFailedException("Action Failed");
            }

            System.out.println("=======Recieved Json Response========\n" + jsonResult.toString());

            // Creating a Gson Object 
            Gson gson = new Gson();

            // Converting json to object 
            // first parameter should be prpreocessed json 
            // and second should be mapping class 
            EgoResponse egoResponse = gson.fromJson(jsonResult.toString(), EgoResponse.class);

            // return object 
            return egoResponse;

        } catch (IOException ex) {
            System.out.println("=======Some Error Occured=====");
            System.out.println(ex);
            throw new ValidationFailedException(ex.getLocalizedMessage());
            // handle exception here
        } finally {

            httpClient.close();

        }

    }

//    public static EgoResponse fetchEgosmsBalance(String BASE_URL, String username, String password) throws ValidationFailedException {
//        final JSONObject egoSmsDetails = new JSONObject();
//        final Gson gson = new Gson();
//        EgoResponse balanceResponse = null;
//
//        try {
//            egoSmsDetails.put("method", "Balance");
//            egoSmsDetails.put("userdata",
//                    new JSONObject().put("username", username)
//                            .put("password", password));
//            WebResource resource = Client.create(new DefaultClientConfig())
//                    .resource(BASE_URL + "/api/v1/json/");
//            final Builder webResource = resource.accept(MediaType.APPLICATION_JSON);
//            webResource.type(MediaType.APPLICATION_JSON);
//            ClientResponse clientResponse = webResource.post(ClientResponse.class, egoSmsDetails.toString());
//            String stringResponse = clientResponse.getEntity(String.class);
//            if (clientResponse.getStatus() == 200) {
//
//                balanceResponse = gson.fromJson(stringResponse, EgoResponse.class);
//                System.out.println("Account balance " + balanceResponse.getBalance());
//                return balanceResponse;
//            } else {
//                throw new ValidationFailedException("Invalid Ego Credential");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return balanceResponse;
//    }

}
