package com.byaffe.learningking.shared.utils;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class RestService {

    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public CountryApiResponseDTO getPostsPlainJSON() {
        try {
            String url = "https://countriesnow.space/api/v0.1/countries/iso";
            HttpHeaders headers = new HttpHeaders();
            headers.add("user-agent", "Application");
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<CountryApiResponseDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, CountryApiResponseDTO.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                return null;
            }
        }catch (Exception exception){
            exception.printStackTrace();
            return  null;
        }
    }
}