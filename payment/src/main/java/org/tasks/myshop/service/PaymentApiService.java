package org.tasks.myshop.service;

import org.tasks.myshop.exception.BadChangingBalanceException;

public interface PaymentApiService {

    Integer getUserbalance();

    Integer changeUserBalance(Integer amount) throws BadChangingBalanceException;

}
