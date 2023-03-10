package com.example.labcityworld.models;

import com.example.labcityworld.app.MyApplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class City extends RealmObject {
    @PrimaryKey
    private int id;
    private String name;
    private String description;
    private String image;
    private float stars;

    public City() {

    }

    public City(String name, String description, String backgroundImage, float stars) {
        this.id = MyApplication.CityID.incrementAndGet();
        this.name = name;
        this.description = description;
        this.image = backgroundImage;
        this.stars = stars;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String backgroundImage) {
        this.image = backgroundImage;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }
}
