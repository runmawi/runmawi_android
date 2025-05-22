package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.Map;

public class seriestitle {


    private String status;
    private String series_status;


    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeries_status()
    {
        return  series_status;
    }


    public void setSeries_status(String series_status)
    {

        this.series_status = series_status;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
