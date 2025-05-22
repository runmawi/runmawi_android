package com.atbuys.runmawi;

public class Emp {
    private String name;
    private String address;
    private boolean gender;
    public Emp(String name, boolean gender, String address){
        this.name=name;
        this.gender=gender;
        this.address=address;
    }
    public String getName(){
        return  name;
    }
    public Boolean isGender(){
        return gender;
    }
    public String getAddress(){
        return address;
    }
}