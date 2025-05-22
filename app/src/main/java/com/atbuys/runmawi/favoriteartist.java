package com.atbuys.runmawi;

class favoriteartist {

    private String name1, name2;
    private String image;


    public favoriteartist(String name1, String image, String name2 ) {
        this.name1 = name1;
        this.image = image;
        this.name2 = name2;

    }


    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }


}
