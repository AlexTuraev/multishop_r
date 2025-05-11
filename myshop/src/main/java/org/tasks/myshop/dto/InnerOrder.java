package org.tasks.myshop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InnerOrder{

    List<OrderDto> orders;
    BigDecimal totalSum;

    public InnerOrder(List<OrderDto> orders, BigDecimal totalSum) {
        this.orders = orders;
        this.totalSum = totalSum;
    }

}