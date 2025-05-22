package com.atbuys.runmawi;

public class active_payment_settings {
    private String id;
    private String live_mode;
    private String test_secret_key;
    private String test_publishable_key;
    private String live_secret_key;
    private String live_publishable_key;
    private String payment_type;
    private String status;




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
