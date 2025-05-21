package org.tasks.myshop.service;

import org.springframework.stereotype.Service;
import org.tasks.myshop.exception.BadChangingBalanceException;
import org.tasks.myshop.repository.PaymentApiRepository;

@Service
public class PaymentApiServiceImpl implements PaymentApiService {

    private final PaymentApiRepository paymentApiRepository;

    public PaymentApiServiceImpl(PaymentApiRepository paymentApiRepository) {
        this.paymentApiRepository = paymentApiRepository;
    }

    @Override
    public Integer getUserbalance() {
        return paymentApiRepository.getUserbalance();
    }

    @Override
    public Integer changeUserBalance(Integer amount) throws BadChangingBalanceException {
        return paymentApiRepository.changeUserBalance(amount);
    }

}
