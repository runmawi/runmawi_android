package com.atbuys.runmawi;

public class PaypalSubscription {

    public String plan_id,id,status,access_token;

    private links[] links;

    public String getId() {
        return id;
    }

    public links[] getLinks() {
        return links;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getStatus() {
        return status;
    }
    public String getPlan_id() {
        return plan_id;
    }
    public PaypalSubscription(String plan_id) {
        this.plan_id = plan_id;
    }
}
