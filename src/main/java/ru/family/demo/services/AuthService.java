package ru.family.demo.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static ru.family.demo.utils.Constants.AUTHENTICATION;
import static ru.family.demo.utils.Constants.CLIENT_INFO;


@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${zuul.routes.auth.url}")
    private String AUTH_URL;

    private final RestTemplate restTemplate;


    public UserInfo authenticate(String username) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(AUTH_URL + AUTHENTICATION)
                .queryParam("email", username);

        HttpEntity<?> entity = new HttpEntity<>(provideHeaders());

        ResponseEntity<UserInfo> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                UserInfo.class);
        return response.getBody();
    }


    private HttpHeaders provideHeaders(){
        var headers = new HttpHeaders();
        headers.set("X-FAMILY-APP-ID", "FAMILY");
        return headers;
    }
}
