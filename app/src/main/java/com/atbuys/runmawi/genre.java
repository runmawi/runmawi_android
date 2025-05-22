package com.atbuys.runmawi;

class genre {

    private String name, url,id;
    private String image,genre;

    public genre() {
    }

    public genre(String name, String image, String url, String id , String genre) {
        this.name = name;
        this.image = image;
        this.url = url;
        this.id=id;
        this.genre=genre;

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }




}
