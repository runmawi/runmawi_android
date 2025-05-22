package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class sendotp {

    private String status;
    private String wish_count;
    private String id;
    private String message;
    private String verify;
    private String mobile;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private List<sendotp> user_details1;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getVerify()
    {
        return verify;
    }

    public void setVerify(String verify)
    {
        this.verify= verify;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile=mobile;
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
