package com.example.rfarm.constructors;

public class Rabbit {
    String image,rabbit_tag,breed,sex;
    String alive,sold;

    public Rabbit() {
    }

    public Rabbit(String image, String rabbit_tag, String breed, String sex, String alive, String sold) {
        this.image = image;
        this.rabbit_tag = rabbit_tag;
        this.breed = breed;
        this.sex = sex;
        this.alive = alive;
        this.sold = sold;
    }

    public String getImage() {
        return image;
    }

    public String getRabbit_tag() {
        return rabbit_tag;
    }

    public String getBreed() {
        return breed;
    }

    public String getSex() {
        return sex;
    }

    public String getAlive() {
        return alive;
    }

    public String getSold() {
        return sold;
    }
}
