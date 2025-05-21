package org.tasks.myshop.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.tasks.myshop.exception.BadChangingBalanceException;
import org.tasks.myshop.openapi.api.PaymentApi;
import org.tasks.myshop.openapi.domain.PaymentUserbalancePostRequest;
import org.tasks.myshop.service.PaymentApiService;

@RestController
public class PaymentApiController implements PaymentApi {

    private final PaymentApiService paymentApiService;

    public PaymentApiController(PaymentApiService paymentApiService) {
        this.paymentApiService = paymentApiService;
    }

    @Override
    public ResponseEntity<Integer> paymentUserbalanceGet() {
        return ResponseEntity.ok(paymentApiService.getUserbalance());
    }

    @Override
    public ResponseEntity<Integer> paymentUserbalancePost(PaymentUserbalancePostRequest paymentUserbalancePostRequest) {
        Integer userbalance;
        try {
            userbalance = paymentApiService.changeUserBalance(paymentUserbalancePostRequest.getAmount());
            return ResponseEntity.ok(userbalance);
        }
        catch (BadChangingBalanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
