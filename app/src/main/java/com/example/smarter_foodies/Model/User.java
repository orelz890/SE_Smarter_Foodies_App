package com.example.smarter_foodies.Model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String name,resume;
    private boolean firstEntry, isChef;
    private String email;
    private String eating;
    private String favorite;
    private String website;
    private String rating;
    private List<String> liked;
    private List<String> cart;
    private List<String> myRecipes;

    public User() {
        liked = new ArrayList<>();
        cart = new ArrayList<>();
        myRecipes = new ArrayList<>();
        this.eating = "";
        this.email = "";
        this.favorite = "";
        this.website = "";
        this.resume = "";
        this.rating = "";
        this.firstEntry = true;
        this.isChef = false;
    }

    public User(String name) {
        this();
        this.name = name;
    }


    public User(String name, String re){
        this(name);
        this.resume = re;
    }
    public String getRating(){return rating;}

    public void setRating(String x){this.rating = x;}

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

    // liked

    public List<String> getLiked() {
        return liked;
    }

    public void setLiked(List<String> liked) {
        this.liked = liked;
    }

    public void addToLiked(String r) {
        if (!this.liked.contains(r)){
            this.liked.add(r);
        }
    }

    public void addToLiked(List<String> newRecipes) {
        for (String r: newRecipes){
            if (!this.liked.contains(r)){
                this.liked.add(r);
            }
        }
    }
    public void removeFromLiked(List<String> delList) {
        this.liked.removeAll(delList);
    }

    public void removeFromLiked(String r) {
        this.liked.remove(r);
    }


    public void addTomyRecipes(List<String> newRecipes) {
        for (String r: newRecipes){
            if (!this.myRecipes.contains(r)){
                this.myRecipes.add(r);
            }
        }
    }

    // cart


    public List<String> getCart() {
        return cart;
    }

    public void setCart(List<String> cart) {
        this.cart = cart;
    }

    public void addToCart(String r) {
        if (!this.cart.contains(r)){
            this.cart.add(r);
        }
    }

    public void addToCart(List<String> newRecipes) {
        for (String r: newRecipes){
            if (!this.cart.contains(r)){
                this.cart.add(r);
            }
        }
    }

    public void removeFromCart(String r) {
        this.cart.remove(r);
    }

    public void removeFromCart(List<String> delList) {
        this.cart.removeAll(delList);
    }


    // my recipes

    public List<String> getMyRecipes() {
        return myRecipes;
    }

    public void setMyRecipes(List<String> myRecipes) {
        this.myRecipes = myRecipes;
    }

    public void addToUserRecipes(String r) {
        if (!this.myRecipes.contains(r)){
            this.myRecipes.add(r);
        }
    }

    public void addToUserRecipes(List<String> newRecipes) {
        for (String r: newRecipes){
            if (!this.myRecipes.contains(r)){
                this.myRecipes.add(r);
            }
        }
    }

    public void removeFromUserRecipes(String r) {
        if (r != null) {
            this.myRecipes.remove(r);
        }
    }

    public void removeFromUserRecipes(List<String> delList) {
        for(String title: delList){
            removeFromUserRecipes(title);
        }
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
                ", liked=" + liked + '\'' +
                ", myRecipes=" + myRecipes + '\'' +
                ", cart=" + cart +
                '}';
    }
}
