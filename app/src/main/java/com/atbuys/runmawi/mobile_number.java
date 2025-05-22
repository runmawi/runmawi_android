package com.atbuys.runmawi;

public class mobile_number {

    public String mobile_number,ccode;


    public mobile_number(String mobile_number, String ccode) {
        this.mobile_number = mobile_number;
        this.ccode = ccode;
    }

    public String getCcode() {
        return ccode;
    }

    public String getMobile_number() {
        return mobile_number;
    }
}
