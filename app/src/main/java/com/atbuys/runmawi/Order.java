package com.atbuys.runmawi;

public class Order {

    private String amount,currency, receipt,payment_capture;

    public Order(String amount, String currency, String receipt, String payment_capture) {
        this.amount = amount;
        this.currency = currency;
        this.receipt = receipt;
        this.payment_capture = payment_capture;
    }
}
