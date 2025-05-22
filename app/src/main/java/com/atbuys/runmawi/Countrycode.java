package com.atbuys.runmawi;

import com.google.gson.annotations.SerializedName;

public class Countrycode {
    @SerializedName("name")
    private String name;

    @SerializedName("dial_code")
    private String dial_code;

    @SerializedName("code")
    private String code;

    public Countrycode(String name, String code, String dial_code) {
        this.name = name;
        this.code = code;
        this.dial_code = dial_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDial_code() {
        return dial_code;
    }

    public void setDial_code(String dial_code) {
        this.dial_code = dial_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
