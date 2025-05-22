package com.atbuys.runmawi;

public class CustomerId {

    public String email,message,status,customer,plan,amount;
    private data data;

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public data getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public CustomerId(String email, String amount, String plan) {
        this.email = email;
        this.plan = plan;
        this.amount = amount;
    }

    public CustomerId(String email) {
        this.email = email;
    }

    public CustomerId(String customer, String plan) {
        this.customer = customer;
        this.plan = plan;
    }
}
