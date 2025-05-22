package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.Map;

public class ReferralDetails {

    private String referral_token;
    private String referrer_count;
    private String earned_coupon;
    private String referrer_name;
    private String used_coupon;
    private String available_coupon;
    private String media;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getReferral_token() {
        return referral_token;
    }

    public void setReferral_token(String referral_token) {
        this.referral_token = referral_token;
    }

    public String getReferrer_count() {
        return referrer_count;
    }

    public void setReferrer_count(String referrer_count) {
        this.referrer_count = referrer_count;
    }

    public String getEarned_coupon() {
        return earned_coupon;
    }

    public void setEarned_coupon(String earned_coupon) {
        this.earned_coupon = earned_coupon;
    }

    public String getReferrer_name() {
        return referrer_name;
    }

    public void setReferrer_name(String referrer_name) {
        this.referrer_name = referrer_name;
    }

    public String getUsed_coupon() {
        return used_coupon;
    }

    public void setUsed_coupon(String usedCoupon) {
        this.used_coupon = usedCoupon;
    }


    public String getAvailable_coupon() {
        return available_coupon;
    }

    public void setAvailable_coupon(String available_coupon) {
        this.available_coupon=available_coupon;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
