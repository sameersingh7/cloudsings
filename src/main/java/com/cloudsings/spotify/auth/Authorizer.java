package com.cloudsings.spotify.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class Authorizer {

    final String SPOTIFY_AUTH_URL = "https://accounts.spotify.com/api/token";

    @Value("${spotify.auth_cred}")
    String b24encodedAuthString;

    @Scheduled(fixedRateString = "${spotify.auth_refresh_interval}")
    public void doAuthorize() {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Basic " + b24encodedAuthString);

        MultiValueMap<String, String> body= new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> requestHttp =new HttpEntity<MultiValueMap<String, String>>(body, headers);
        ResponseEntity<?> result = restTemplate.exchange(SPOTIFY_AUTH_URL, HttpMethod.POST, requestHttp, AuthorizerResponse.class);

        AuthorizerResponse response = (AuthorizerResponse)result.getBody();
        System.out.println(response.getAccess_token());
    }
}
