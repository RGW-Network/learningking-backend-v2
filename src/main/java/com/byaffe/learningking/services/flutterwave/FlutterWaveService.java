package com.byaffe.learningking.services.flutterwave;

import com.byaffe.learningking.constants.TransactionMode;
import com.byaffe.learningking.models.payments.AggregatorTransaction;
import com.byaffe.learningking.shared.exceptions.ValidationFailedException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FlutterWaveService {

    private static final Logger logger = LoggerFactory.getLogger(FlutterWaveService.class);
    private static final String ALGORITHM = "DESede";
    private static final String TRANSFORMATION = "DESede/ECB/PKCS5Padding";

    @Value("${flutterwave.public.key}")
    private String flutterwavePublicKey;
    @Value("${flutterwave.encryption.key}")
    private String flutterwaveEncryptionKey;
    @Value("${flutterwave.base.endpoint}")
    private String flutterwaveBaseEndpoint;


    @Value("${flutterwave.secret.key}")
    private String flutterwaveSecretKey;


    private final RestTemplate restTemplate;
    private final Gson gson;
    private static final String redirectUrl = "https://leearningking.academy/success-payment";

    public FlutterWaveService() {
        this.restTemplate = new RestTemplate();
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    }


    public FlutterReponse initiateDeposit(AggregatorTransaction payment) {
        return  initiateCardDepositStandard(payment);
//        if (payment.getMode().equals(TransactionMode.MOBILE_MONEY)) {
//            return initiateMOMODeposit(payment);
//        }
//        if (payment.getMode().equals(TransactionMode.CARD)) {
//            //return initiateCardDeposit(payment);
//            return  initiateCardDepositStandard(payment);
//        }
//        if (payment.getMode().equals(TransactionMode.BANK)) {
//            return initiateBankDeposit(payment);
//        }

    }

    public FlutterReponse initiateMOMODeposit(AggregatorTransaction payment) {
        FlutterwaveMobileDepositDTO fluterwaveRequest = new FlutterwaveMobileDepositDTO();
        fluterwaveRequest.setAmount(payment.getAmountChargedFromUser());
        fluterwaveRequest.setCurrency("USD");
        fluterwaveRequest.setEmail(payment.getStudent().getUserAccount().getEmailAddress());
        fluterwaveRequest.setNetwork(resolveNetwork(payment.getPhoneNumber()));
        fluterwaveRequest.setTx_ref(payment.getSerialNumber());
        fluterwaveRequest.setPhone_number(payment.getPhoneNumber());
        System.out.println("Tx Ref---------->: "+payment.getInternalReference());

        try {
            String url = flutterwaveBaseEndpoint + "/charges?type=" + resolveMomoCountry(fluterwaveRequest.getPhone_number());
            String jsonRequest = gson.toJson(fluterwaveRequest);
            logger.info(String.format("Request from Flutter wave: =%s url:%s", jsonRequest, url));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(flutterwaveSecretKey);

            HttpEntity<FlutterwaveMobileDepositDTO> entity = new HttpEntity<>(fluterwaveRequest, headers);
            FlutterReponse flutterReponse = restTemplate.postForObject(url, entity, FlutterReponse.class);

            //  FlutterReponse flutterReponse = gson.fromJson(jsonResponse, FlutterReponse.class);
            logger.info("Response from Flutterwave: {}", gson.toJson(flutterReponse));
            return flutterReponse;
        } catch (RestClientException e) {
            logger.error("Error during payment initiation: ", e);
            throw new RuntimeException("Error during payment initiation", e);
        }
    }

    public FlutterReponse initiateCardDepositStandard(AggregatorTransaction payment) {
        FlutterWaveStandardDepositRequestDto fluterwaveRequest = getFlutterWaveStandardDepositRequestDto(payment);
        try {
            String url = flutterwaveBaseEndpoint + "/payments";
            String jsonRequest = gson.toJson(fluterwaveRequest);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(flutterwaveSecretKey);

            HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers);
            String jsonResponse = restTemplate.postForObject(url, entity, String.class);

            FlutterReponse flutterReponse = gson.fromJson(jsonResponse, FlutterReponse.class);
            logger.info("Response from FlutterWave Card Deposit: {}", gson.toJson(flutterReponse));
            return flutterReponse;
        } catch (RestClientException e) {
            logger.error("Error during payment initiation: ", e);
            throw new ValidationFailedException("Error during payment initiation"+ e.getMessage());
        }
    }

    private static FlutterWaveStandardDepositRequestDto getFlutterWaveStandardDepositRequestDto(AggregatorTransaction payment) {
        FlutterWaveStandardDepositRequestDto fluterwaveRequest = new FlutterWaveStandardDepositRequestDto();
        fluterwaveRequest.setTx_ref(payment.getInternalReference());
        fluterwaveRequest.setAmount(String.valueOf(payment.getAmountChargedFromUser()));
        fluterwaveRequest.setCurrency(payment.getCurrency().getPgwCode());
        fluterwaveRequest.setRedirect_url(redirectUrl);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail(payment.getStudent().getEmailAddress());
        customerDTO.setName(payment.getStudent().getFullName());
        customerDTO.setPhonenumber(payment.getStudent().getPhoneNumber());
        fluterwaveRequest.setCustomer(customerDTO);

        CustomizationsDTO customizationsDTO= new CustomizationsDTO();
        customizationsDTO.setTitle("Learningking "+payment.getDescription());

        fluterwaveRequest.setCustomizations(customizationsDTO);
        return fluterwaveRequest;
    }


    public FlutterReponse checkPaymentStatus(String transactionId) {
        try {
            String url = String.format("%s/transactions/verify_by_reference?tx_ref=%s", flutterwaveBaseEndpoint, transactionId);

            HttpHeaders headers = new HttpHeaders();
            List<MediaType> acceptableMediaTypes = new java.util.ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            headers.setAccept(acceptableMediaTypes);
            headers.setBearerAuth(flutterwaveSecretKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            FlutterReponse    flutterReponse = restTemplate.exchange(url, HttpMethod.GET,entity, FlutterReponse.class,new HashMap<>()).getBody();
            logger.info("Response for payment status check with transaction ID {}: {}", transactionId, gson.toJson(flutterReponse));
            return flutterReponse;
        } catch (HttpClientErrorException e) {
            // Handle HTTP 4xx errors (client errors)
            logger.error("Error : {} On payment status check for txn : {}", transactionId, e.getMessage());
            FlutterReponse flutterReponse= new FlutterReponse();
            flutterReponse.status="RequestFailed";
            flutterReponse.message=e.getMessage();
            return  flutterReponse;
        } catch (HttpServerErrorException e) {
            // Handle HTTP 5xx errors (server errors)
            logger.error("Error : {} On payment status check for txn : {}", transactionId, e.getMessage());
            FlutterReponse flutterReponse= new FlutterReponse();
            flutterReponse.status="RequestFailed";
            flutterReponse.message=e.getMessage();
            return  flutterReponse;
        } catch (ResourceAccessException e) {
            // Handle I/O errors, like connection timeouts
            logger.error("Error : {} On payment status check for txn : {}", transactionId, e.getMessage());
            FlutterReponse flutterReponse= new FlutterReponse();
            flutterReponse.status="RequestFailed";
            flutterReponse.message=e.getMessage();
            return  flutterReponse;
        } catch (RestClientException e) {
            // Handle other exceptions that might be thrown by RestTemplate
            logger.error("Error : {} On payment status check for txn : {}", transactionId, e.getMessage());
            FlutterReponse flutterReponse= new FlutterReponse();
            flutterReponse.status="RequestFailed";
            flutterReponse.message=e.getMessage();
            return  flutterReponse;
        } catch (Exception e) {
            logger.error("Error : {} On payment status check for txn : {}", transactionId, e.getMessage());
            FlutterReponse flutterReponse= new FlutterReponse();
            flutterReponse.status="RequestFailed";
            flutterReponse.message=e.getMessage();
            return  flutterReponse;
        }
    }

    public static String resolveNetwork(String phoneNumber) {

        if (phoneNumber == null) {
            return null;
        }
        if (Stream.of("+24576", "+24577", "+24578").anyMatch(phoneNumber::startsWith)) {
            return "MTN";
        }
        if (Stream.of("+24570", "+24574", "+24575").anyMatch(phoneNumber::startsWith)) {
            return "AIRTEL";
        }
        return null;
    }

    public static String resolveMomoCountry(String phoneNumber) {

        if (phoneNumber == null) {
            return null;
        }else
        if (Stream.of("+256").anyMatch(phoneNumber::startsWith)) {
            return "mobile_money_uganda";
        }else
        if (Stream.of("+254").anyMatch(phoneNumber::startsWith)) {
            return "mpesa";
        }else
        if (Stream.of("+233").anyMatch(phoneNumber::startsWith)) {
            return "mobile_money_ghana";
        }else
        if (Stream.of("+247").anyMatch(phoneNumber::startsWith)) {
            return "mobile_money_franco";
        }
        return null;
    }

    public static String tripleDESEncrypt(String data, String encryptionKey) {
        final String defaultString = "";
        if (data == null || encryptionKey == null) return defaultString;
        try {
            final byte[] encryptionKeyBytes = encryptionKey.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(encryptionKeyBytes, ALGORITHM);
            final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            final byte[] dataBytes = data.getBytes();
            byte[] encryptedValue = cipher.doFinal(dataBytes);
            return Base64.getEncoder().encodeToString(encryptedValue);
        } catch (Exception exception) {
            return exception.getMessage();
        }
    }
    static class EncryptedDto{
        public String client;

        public EncryptedDto(String client) {
            this.client = client;
        }
    }
    static class OtpDto{
        public String otp;
        public String flw_ref;

        public OtpDto(String otp, String flw_ref) {
            this.otp = otp;
            this.flw_ref = flw_ref;
        }
    }
}
