package com.demo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Service2Client {
    private final RestTemplate restTemplate;
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    public Service2Client(RestTemplate restTemplate, OAuth2AuthorizedClientManager authorizedClientManager) {
        this.restTemplate = restTemplate;
        this.authorizedClientManager = authorizedClientManager;
    }

    @Value("${service2.url}")
    private String service2Url;

    public String fetchData() {
//        var authRequest = OAuth2AuthorizeRequest
//                .withClientRegistrationId("oauth2-client-credentials")
//                .principal("machine")
//                .build();
//        var client = authorizedClientManager.authorize(authRequest);
//        String token = client.getAccessToken().getTokenValue();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        var response = restTemplate.exchange(
//                service2Url+"/data",
//                HttpMethod.GET,
//                new HttpEntity<>(headers),
//                String.class
//        );
//        return response.getBody();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken){
            String token = jwtAuthenticationToken.getToken().getTokenValue();
            System.out.println("Token: "+token);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            var response = restTemplate.exchange(
                    service2Url+"/data",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class
            );
            return response.getBody();
        }
        return null;
    }
}
