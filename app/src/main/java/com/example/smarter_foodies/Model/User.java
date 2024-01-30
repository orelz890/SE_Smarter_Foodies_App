package com.example.smarter_foodies.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

//    private String email;
//    private String eating;
//    private String favorite;
//    private String website;
//    private String rating;
//    private String resume;

    @SerializedName("name")
    private String name;

    @SerializedName("firstEntry")
    private boolean firstEntry;

    @SerializedName("chef")
    private boolean isChef;

    @SerializedName("liked")
    private Map<String,String> liked;

    @SerializedName("cart")
    private Map<String,String> cart;

    @SerializedName("myRecipes")
    private Map<String,String> myRecipes;

    public User() {
        liked = new HashMap<>();
        cart = new HashMap<>();
        myRecipes = new HashMap<>();
        this.firstEntry = true;
        this.isChef = false;
//        this.eating = "";
//        this.email = "";
//        this.favorite = "";
//        this.website = "";
//        this.resume = "";
//        this.rating = "";
    }

    public User(String name) {
        this();
        this.name = name;
    }


    public User(String name, String re){
        this(name);
//        this.resume = re;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

//    public String getRating(){return rating;}
//
//    public void setRating(String x){this.rating = x;}
//    public String getResume() {
//        return resume;
//    }
//
//    public void setResume(String resume) {
//        this.resume = resume;
//    }
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getEating() {
//        return eating;
//    }
//
//    public void setEating(String eating) {
//        this.eating = eating;
//    }
//
//    public String getFavorite() {
//        return favorite;
//    }
//
//    public void setFavorite(String favorite) {
//        this.favorite = favorite;
//    }
//
//    public String getWebsite() {
//        return website;
//    }
//
//    public void setWebsite(String website) {
//        this.website = website;
//    }

    // liked

    public Map<String,String> getLiked() {
        return liked;
    }

    public void setLiked(Map<String,String> liked) {
        this.liked = liked;
    }

    public void addToLiked(String k, String v) {
        if (!this.liked.containsKey(k)){
            this.liked.put(k,v);
        }
    }

    public void addToLiked(Map<String,String> newRecipes) {
        for (String k: newRecipes.keySet()){
            if (!this.liked.containsKey(k)){
                this.liked.put(k, newRecipes.get(k));
            }
        }
    }
    public void removeFromLiked(List<String> delList) {
        for (String k: delList) {
            this.liked.remove(k);
        }
    }

    public void removeFromLiked(String r) {
        this.liked.remove(r);
    }


    // cart


    public Map<String,String> getCart() {
        return cart;
    }

    public void setCart(Map<String,String> cart) {
        this.cart = cart;
    }

    public void addToCart(String k, String v) {
        if (!this.cart.containsKey(k)){
            this.cart.put(k,v);
        }
    }

    public void addToCart(Map<String,String> newRecipes) {
        for (String k: newRecipes.keySet()){
            if (!this.cart.containsKey(k)){
                this.cart.put(k,newRecipes.get(k));
            }
        }
    }

    public void removeFromCart(String r) {
        this.cart.remove(r);
    }

    public void removeFromCart(List<String> delList) {
        for (String k: delList) {
            this.cart.remove(k);
        }
    }


    // my recipes

    public Map<String,String> getMyRecipes() {
        return myRecipes;
    }

    public void setMyRecipes(Map<String,String> myRecipes) {
        this.myRecipes = myRecipes;
    }

    public void addToUserRecipes(String k, String v) {
        if (!this.myRecipes.containsKey(k)){
            this.myRecipes.put(k,v);
        }
    }

    public void addToUserRecipes(Map<String,String> newRecipes) {
        for (String key: newRecipes.keySet()){
            if (!this.myRecipes.containsKey(key)){
                this.myRecipes.put(key, newRecipes.get(key));
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
                ", firstEntry=" + firstEntry +
                ", isChef=" + isChef +
                ", liked=" + liked + '\'' +
                ", myRecipes=" + Arrays.toString(myRecipes.keySet().toArray()) + '\'' +
                ", cart=" + cart +
//                ", resume='" + resume + '\'' +
//                ", email='" + email + '\'' +
//                ", eating='" + eating + '\'' +
//                ", favorite='" + favorite + '\'' +
//                ", website='" + website + '\'' +
                '}';
    }
}
