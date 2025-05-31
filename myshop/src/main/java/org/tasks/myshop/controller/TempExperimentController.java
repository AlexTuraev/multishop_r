package org.tasks.myshop.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import org.tasks.myshop.service.facade.impl.PurchaseFcdServiceImpl;

import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class TempExperimentController {

    private final OAuth2AuthorizedClientManager manager;

    public TempExperimentController(OAuth2AuthorizedClientManager manager) {
        this.manager = manager;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        OAuth2AuthorizedClient client = manager.authorize(OAuth2AuthorizeRequest
                .withClientRegistrationId("myshop")
                .principal("system")
                .build()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        RestClient restClient = RestClient.create("http://localhost:8280");
//        WebClient restClient = WebClient.create("http://localhost:8280");

        ResponseEntity<Integer> responseEntity = restClient.get()
                .uri("/payment/userbalance")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .retrieve()
                .toEntity(Integer.class);

        Integer userBalance = responseEntity.getBody();

        var balance = restClient.post()
                .uri("/payment/userbalance")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Подставляем токен доступа в заголовок Authorization
                .body(Map.of("amount", 5))
                .retrieve()
                .toEntity(Integer.class);

        userBalance = balance.getBody();

        System.out.println(responseEntity.getStatusCode());

        return responseEntity;
    }

}
