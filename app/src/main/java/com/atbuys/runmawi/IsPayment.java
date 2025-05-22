package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.Map;

public class IsPayment {


    private String is_payment;
    //private String settings;


    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getIs_payment() {
        return is_payment;
    }

    public void setIs_payment(String is_payment) {
        this.is_payment = is_payment;
    }




    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
