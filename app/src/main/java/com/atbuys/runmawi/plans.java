package com.atbuys.runmawi;

public class plans {
    private String id;
    private String plans_name;
    private String price;
    private String type;
    private String billing_interval;
    private String plan_id;
    private String video_quality;
    private String resolution;
    private String devices;
    private boolean show;


    public String getId() {
        return id;
    }

    public String getPlans_name() {
        return plans_name;
    }

    public String getPrice() {
        return price;
    }

    public String getType()
    {
        return type;
    }

    public String getBilling_interval()
    {
        return billing_interval;
    }

    public String getPlan_id() {
        return plan_id;
    }


    public String getVideo_quality()
    {
        return video_quality;
    }

    public String getResolution()
    {
        return resolution;
    }

    public String getDevices()
    {
        return devices;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
