package com.atbuys.runmawi;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class PaydunyaCheckout {
    public String responseText;
    public String responseCode;
    public String status;
    public String token;
    public String transactionId;
    public String description;
    public static String SUCCESS = "success";
    public static String FAIL = "fail";
    public static String COMPLETED = "completed";
    public static String PENDING = "pending";

    public PaydunyaCheckout() {
    }

    public String getStatus() {
        return this.status;
    }

    public String getResponseText() {
        return this.responseText;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public String getToken() {
        return this.token;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public String getDescription() {
        return this.description;
    }
}

