package com.cloudsings.spotify.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class Authorize {

    final String SPOTIFY_AUTH_URL = "https://accounts.spotify.com/api/token";

    RestTemplate restTemplate =new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
}
