package com.cloudsings.service.spotify;

import com.cloudsings.model.spotify.SpotifyTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class Authorize {

    final String SPOTIFY_AUTH_URL = "https://accounts.spotify.com/api/token";

    @Value("${spotify.auth_cred}")
    private String b24encodedAuthString;

    private static String accessToken;

    public static String getAccessToken() {
        return accessToken;
    }

    @Scheduled(fixedRateString = "${spotify.auth_refresh_interval}")
    public void refreshAccessToken(){
        accessToken = getAccessTokenFromSpotify();
    }


    private String getAccessTokenFromSpotify() {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Basic " + b24encodedAuthString);

        MultiValueMap<String, String> body= new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> requestHttp =new HttpEntity<MultiValueMap<String, String>>(body, headers);
        ResponseEntity<?> result = restTemplate.exchange(SPOTIFY_AUTH_URL, HttpMethod.POST, requestHttp, SpotifyTokenResponse.class);

        SpotifyTokenResponse response = (SpotifyTokenResponse)result.getBody();
        return response.getAccess_token();
    }
}
