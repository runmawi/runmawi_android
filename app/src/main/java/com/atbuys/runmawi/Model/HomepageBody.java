package com.atbuys.runmawi.Model;


public class HomepageBody {

    private String status;
    private HomepageData[] Home_page;
    private HomepagecatData[] data;


    public String getStatus() { return status; }

    public void setStatus(String value) { this.status = value; }

    public HomepageData[] getHome_page() { return Home_page; }

    public void setHome_page(HomepageData[] value) { this.Home_page = value; }

    public HomepagecatData[] getData()
    {
        return data;
    }

    public void setData(HomepagecatData[] data) {
        this.data = data;
    }
}
