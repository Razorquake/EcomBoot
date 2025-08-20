package com.razorquake.user.service;

import com.razorquake.user.dto.UserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class KeyCloakAdminService {
    @Value("${keycloak.admin.username}")
    private String adminUsername;
    @Value("${keycloak.admin.password}")
    private String adminPassword;
    @Value("${keycloak.server.url}")
    private String keycloakServerUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.client.id}")
    private String clientId;
    @Value("${keycloak.client.uid}")
    private String clientUid;


    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("username", adminUsername);
        params.add("password", adminPassword);
        params.add("grant_type", "password");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map>  responseEntity = restTemplate.postForEntity(
                keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                request, Map.class
        );
        assert responseEntity.getBody() != null;
        return responseEntity.getBody().get("access_token").toString();
    }

    public String createUser(String token, UserRequest user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", user.getUsername());
        userPayload.put("email", user.getEmail());
        userPayload.put("enabled", true);
        userPayload.put("firstName", user.getFirstName());
        userPayload.put("lastName", user.getLastName());
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", user.getPassword());
        credentials.put("temporary", false);
        userPayload.put("credentials", List.of(credentials));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(userPayload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                keycloakServerUrl + "/admin/realms/" + realm + "/users",
                request, String.class
        );
        if (!HttpStatus.CREATED.equals(response.getStatusCode())) {
            throw new RuntimeException("Failed to create user");
        }
        URI location = response.getHeaders().getLocation();
        if (location == null) {
            throw new RuntimeException("Failed to get created user location");
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);

    }

    private Map<String, Object> getRealmRoleRepresentation(
            String token,
            String roleName
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                keycloakServerUrl + "/admin/realms/" + realm + "/clients/"+ clientUid + "/roles/" + roleName,
                HttpMethod.GET,
                entity,
                Map.class
        );
        return response.getBody();
    }

    public void assignRealmRoleToUser(String userId, String roleName) {
        String token = getAccessToken();
        Map<String, Object> roleRepresentation = getRealmRoleRepresentation(token, roleName);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(List.of(roleRepresentation), headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                keycloakServerUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/clients/" + clientUid,
                entity,
                Void.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(
                    "Failed to assign role " + roleName +
                            " to user " + userId +
                            ": HTTP status code" + response.getStatusCode()
            );
        }
    }
}
