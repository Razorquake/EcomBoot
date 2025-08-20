package com.demo.auth_code_flow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AuthCodeFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthCodeFlowApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
	}

	@Bean
	public OAuth2AuthorizedClientManager auth2AuthorizedClientManager(
			ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientService authorizedClientService
	) {
		var manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
				clientRegistrationRepository, authorizedClientService
		);
		OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder()
				.clientCredentials()
				.build();
		manager.setAuthorizedClientProvider(provider);
		return manager;
	}

	@Bean
	public CommandLineRunner run(
			RestTemplate restTemplate,
			OAuth2AuthorizedClientManager authorizedClientManager,
			@Value("${service2.url}") String service2Url
			) {
		return args -> {

			var authRequest = OAuth2AuthorizeRequest
					.withClientRegistrationId("oauth2-client-credentials")
					.principal("machine")
					.build();
			var client = authorizedClientManager.authorize(authRequest);
			String token = client.getAccessToken().getTokenValue();
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(token);
			var response = restTemplate.exchange(
					service2Url+"/data",
					HttpMethod.GET,
					new HttpEntity<>(headers),
					String.class
			);
			System.out.println(response);
		};

	}
}
