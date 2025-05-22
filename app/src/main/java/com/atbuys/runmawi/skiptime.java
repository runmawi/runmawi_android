package com.atbuys.runmawi;

import java.util.HashMap;
import java.util.Map;

public class skiptime {


    private String skip_time;
    private String intro_time;


    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getSkip_time() {
        return skip_time;
    }

    public void setSkip_time(String skip_time) {
        this.skip_time = skip_time;
    }



    public String getIntro_time()
    {

        return intro_time;
    }

    public void setIntro_time(String skip_time)
    {
        this.intro_time =intro_time;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
