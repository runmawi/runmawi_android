package com.atbuys.runmawi.Model;


public class HomepagecatData {


    private String name,order;
    private category_videos[] category_videos;



    public String getName() { return name; }

    public void setName(String source) { this.name = name; }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }



    public category_videos[] getCategory_videos()
    {
        return category_videos;
    }

    public void setCategory_videos(category_videos[] category_videos)
    {
        this.category_videos= category_videos;
    }



}
