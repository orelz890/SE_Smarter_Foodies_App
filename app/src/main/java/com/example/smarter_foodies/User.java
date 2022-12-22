package com.example.smarter_foodies;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String name,resume;
    private boolean firstEntry, isChef;
    private String email;
    private String eating;
    private String favorite;
    private String website;
    private List<recipe> liked;
    private List<recipe> cart;


    public User() {
        firstEntry = true;
        isChef = false;
        liked = new ArrayList<>();
        cart = new ArrayList<>();
    }

    public User(String name) {
        this.name = name;
        this.firstEntry = true;
        this.isChef = false;
        this.eating = "";
        this.email = "";
        this.favorite = "";
        this.website = "";
        this.resume = "";
        this.liked = new ArrayList<>();
        this.cart = new ArrayList<>();
    }

    public User(String name, String re){
        this.name = name;
        this.resume = re;
        this.firstEntry = true;
        this.isChef = false;
        this.liked = new ArrayList<>();
        this.cart = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public boolean isFirstEntry() {
        return firstEntry;
    }

    public void setFirstEntry(boolean firstEntry) {
        this.firstEntry = firstEntry;
    }

    public boolean isChef() {
        return isChef;
    }

    public void setChef(boolean chef) {
        isChef = chef;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEating() {
        return eating;
    }

    public void setEating(String eating) {
        this.eating = eating;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<recipe> getLiked() {
        return liked;
    }

    public void setLiked(List<recipe> liked) {
        this.liked = liked;
    }

    public List<recipe> getCart() {
        return cart;
    }

    public void setCart(List<recipe> cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", resume='" + resume + '\'' +
                ", firstEntry=" + firstEntry +
                ", isChef=" + isChef +
                ", email='" + email + '\'' +
                ", eating='" + eating + '\'' +
                ", favorite='" + favorite + '\'' +
                ", website='" + website + '\'' +
                ", liked=" + liked +
                ", cart=" + cart +
                '}';
    }
}
