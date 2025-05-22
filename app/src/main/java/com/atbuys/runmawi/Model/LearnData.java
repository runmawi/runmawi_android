package com.atbuys.runmawi.Model;


public class LearnData {


    private String name;
    private String id;
    private learn[] category_series;



    public String getCurrent_page() { return name; }

    public void setCurrent_page(String value) { this.name = value; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public learn[] getData() { return category_series; }

    public void setData(learn[] data) { this.category_series = category_series; }


}
