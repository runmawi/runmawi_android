package com.atbuys.runmawi.Adapter;

import com.google.gson.annotations.SerializedName;

public class series_imgae {

    public String status;
    @SerializedName("Message")
    public String message;
    @SerializedName("Series_image")
    public SeriesImage series_image;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SeriesImage getSeries_image() {
        return series_image;
    }

    public void setSeries_image(SeriesImage series_image) {
        this.series_image = series_image;
    }
}
