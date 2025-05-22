package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mobileOTP {

    private String status;
    private String wish_count;
    private String id;
    private String message;
    private String verification_code;
    private String email;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private List<mobileOTP> user_details1;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getVerification_code()
    {
        return verification_code;
    }

    public void setVerification_code(String verification_code)
    {
        this.verification_code=verification_code;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email=email;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message=message;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
