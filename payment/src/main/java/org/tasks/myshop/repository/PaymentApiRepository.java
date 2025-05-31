package org.tasks.myshop.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.tasks.myshop.exception.BadChangingBalanceException;

@Repository
public class PaymentApiRepository {

    @Value("${app.payment.balance}")
    private Integer balance;

    public Integer getUserbalance() {
        return balance;
    }

    public Integer changeUserBalance(Integer amount) throws BadChangingBalanceException {
        if(amount > balance) {
            throw new BadChangingBalanceException();
        }

        balance -= amount;
        return balance;
    }

}
