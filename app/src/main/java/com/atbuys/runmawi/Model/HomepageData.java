package com.atbuys.runmawi.Model;


public class HomepageData {


    private String source,header_name;
    private data[] data;
    private category_videos[] category_videos;


    public String getSource() { return source; }

    public void setSource(String source) { this.source = source; }

    public String getHeader_name()
    {
        return header_name;
    }

    public void setHeader_name(String header_name) {
        this.header_name = header_name;
    }


    public data[] getData() { return data; }

    public void setData(data[] data) { this.data = data; }

    public category_videos[] getCategory_videos()
    {
        return category_videos;
    }



}
