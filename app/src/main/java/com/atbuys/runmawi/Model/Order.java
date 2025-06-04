package com.atbuys.runmawi.Model;

import java.util.Map;

public class Order {
    String amount;
    String currency;
    String receipt;
    String partial_payment;
    Map<String, String> notes;

    public Order(String amount, String currency, String receipt, String partial_payment, Map<String, String> notes) {
        this.amount = amount;
        this.currency = currency;
        this.receipt = receipt;
        this.partial_payment = partial_payment; // "0" for no partial payments, "1" to allow
        this.notes = notes;
    }
}
