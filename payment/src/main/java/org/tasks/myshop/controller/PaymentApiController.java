package org.tasks.myshop.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.tasks.myshop.openapi.api.PaymentApi;

@RestController
public class PaymentApiController implements PaymentApi {

    @Value("${app.payment.balance}")
    private Integer balance;

    @Override
    public ResponseEntity<Integer> paymentUserbalanceGet() {
        return ResponseEntity.ok(balance);
    }

}
