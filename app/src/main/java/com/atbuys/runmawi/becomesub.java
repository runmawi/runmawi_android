package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class becomesub {

    private String status;
    private String Message;


    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private List<becomesub> user_details1;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public String getMessage()
    {
        return Message;
    }

    public void setMessage(String message)
    {
        this.Message=Message;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
