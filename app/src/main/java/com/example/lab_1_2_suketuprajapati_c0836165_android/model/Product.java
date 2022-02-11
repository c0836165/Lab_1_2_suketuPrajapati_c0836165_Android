package com.example.lab_1_2_suketuprajapati_c0836165_android.model;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "productlist")
public class Product {

    @PrimaryKey(autoGenerate = true)
    int id;
    String Name;
    String Description;
    String Price;
    String lat;
    String lon;

    @Ignore
    public Product(String Name, String Description, String Price, String lat, String lon) {
        this.Name = Name;
        this.Description = Description;
        this.Price = Price;
        this.lat = lat;
        this.lon = lon;
    }

    public Product(int id, String Name, String Description, String Price, String lat, String lon) {
        this.id = id;
        this.Name = Name;
        this.Description = Description;
        this.Price = Price;
        this.lat = lat;
        this.lon = lon;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
