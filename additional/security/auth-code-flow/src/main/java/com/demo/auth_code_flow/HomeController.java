package com.demo.auth_code_flow;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home(OAuth2AuthenticationToken token) {
        return "Welcome "+token.getName()
                + " you are authenticated with subject "+token.getPrincipal()
                + " you are authenticated with scopes "+token.getAuthorities()
                + " you are authenticated with token "+token.getCredentials()
                + " and your email is "+token.getPrincipal().getAttributes().get("email")
                ;
    }
}
