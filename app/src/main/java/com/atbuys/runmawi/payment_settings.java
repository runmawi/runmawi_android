package com.atbuys.runmawi;

public class payment_settings {
    private String id;
    private String live_mode;
    private String test_secret_key;
    private String test_publishable_key;
    private String live_secret_key;
    private String live_publishable_key;
    private String payment_type;
    private String status,paystack_test_secret_key,paystack_test_publishable_key,CinetPay_APIKEY,CinetPay_SITE_ID;
    private String live_paypal_username,live_paypal_password;
    private String paydunya_masterkey,paydunya_test_PublicKey,paydunya_test_PrivateKey,paydunya_test_token;
    private String paydunya_live_PublicKey,paydunya_live_PrivateKey,paydunya_live_token;


    public String getPaystack_test_secret_key() {
        return paystack_test_secret_key;
    }

    public String getPaydunya_masterkey() {
        return paydunya_masterkey;
    }

    public String getPaydunya_test_PublicKey() {
        return paydunya_test_PublicKey;
    }

    public String getPaydunya_test_PrivateKey() {
        return paydunya_test_PrivateKey;
    }

    public String getPaydunya_test_token() {
        return paydunya_test_token;
    }

    public String getPaydunya_live_PublicKey() {
        return paydunya_live_PublicKey;
    }

    public String getPaydunya_live_PrivateKey() {
        return paydunya_live_PrivateKey;
    }

    public String getPaydunya_live_token() {
        return paydunya_live_token;
    }

    public String getLive_paypal_username() {
        return live_paypal_username;
    }

    public String getLive_paypal_password() {
        return live_paypal_password;
    }

    public String getCinetPay_APIKEY() {
        return CinetPay_APIKEY;
    }

    public String getCinetPay_SITE_ID() {
        return CinetPay_SITE_ID;
    }

    public String getPaystack_test_publishable_key() {
        return paystack_test_publishable_key;
    }

    public String getId() {
        return id;
    }

    public String getLive_mode() {
        return live_mode;
    }

    public String getTest_secret_key() {
        return test_secret_key;
    }

    public String getTest_publishable_key()
    {
        return test_publishable_key;
    }

    public String getLive_secret_key()
    {
        return live_secret_key;
    }

    public String getLive_publishable_key()
    {
        return live_publishable_key;
    }

    public String getPayment_type()
    {
        return payment_type;
    }

    public String getStatus()
    {
        return status;
    }
}
