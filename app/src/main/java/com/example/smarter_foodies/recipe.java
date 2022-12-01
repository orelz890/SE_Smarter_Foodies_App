package com.example.smarter_foodies;

import android.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class recipe {

    //  Must haves:
    private String title;
    private String main_category;
    private String category;
    private ArrayList<Pair<Double, String>> ingredients;
    private String[] directions;
    private int prepTime;
    private int cookingTime;
    private int totalTime;
    private int servings;
    private int protein;
    private int fat;
    private int carbs;
    //  Nice to have additions:
    private double stars;

    private int numOfStarGivers;

    private ArrayList<String> images;
    //  <user_name, comment>
    //  User comments:
    private HashMap<String, ArrayList<String>> comments;
    public recipe() {
        init();
    }

    public recipe(String title, String main_category, String category,
                  String[] ingredients, String[] directions,
                  int prepTime, int cookingTime, int servings, int protein,
                  int fat, int carbs, double stars, ArrayList<String> images, int numOfStarGivers,
                  HashMap<String, ArrayList<String>> comments) {
        init();
        this.setTitle(title);
        this.setMain_category(main_category);
        this.setCategory(category);
        this.setIngredients(ingredients);
        this.setDirections(directions);
        this.setImages(images);
        this.setPrepTime(prepTime);
        this.setCookingTime(cookingTime);
        this.setTotalTime(this.cookingTime + this.prepTime);
        this.setServings(servings);
        this.setProtein(protein);
        this.setCarbs(carbs);
        this.setFat(fat);
        this.setStars(stars);
        this.setNumOfStarGivers(numOfStarGivers);
        this.setComments(comments);
    }

    private void init(){
        this.ingredients = new ArrayList<>();
        this.comments = new HashMap<>();
    }
    public int getNumOfStarGivers() {
        return numOfStarGivers;
    }

    public void setNumOfStarGivers(int numOfStarGivers) {
        this.numOfStarGivers = numOfStarGivers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMain_category() {
        return main_category;
    }

    public void setMain_category(String main_category) {
        this.main_category = main_category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<Pair<Double, String>> getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        StringBuilder ans;
        double quantity = -1;
        for (String curr_str: ingredients){
            String curr = curr_str.trim();
            String[] splited = curr.split(" ");
            if (splited.length > 0){
                quantity =  Double.parseDouble(splited[0]);
            }
            ans = new StringBuilder();
            for (int i = 1; i < splited.length; i++){
                ans.append(splited[i]);
            }
            if (quantity != -1) {
                this.ingredients.add(new Pair<Double, String>(quantity, ans.toString()));
            }
        }
    }


    public String[] getDirections() {
        return directions;
    }

    public void setDirections(String[] directions) {
        this.directions = directions;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> imgs) {
        this.images = imgs;
    }

    public void addImage(String img) {
        this.images.add(img);
    }

    public void delImage(String img) {
        this.images.remove(img);
    }

    public HashMap<String, ArrayList<String>> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, ArrayList<String>> comments) {
        this.comments = comments;
    }
    public void delComment(String name, String comment) {
        Objects.requireNonNull(this.comments.get(name)).remove(comment);
    }

    public void addComment(String name, String comment) {
        if (this.comments.get(name) == null){
            this.comments.put(name, new ArrayList<>());
        }
        Objects.requireNonNull(this.comments.get(name)).add(comment);
    }

    @Override
    public String toString() {
        return "recipe{" +
                "title='" + title + '\'' +
                ", main_category='" + main_category + '\'' +
                ", category='" + category + '\'' +
                ", ingredients=" + ingredients +
                ", directions=" + Arrays.toString(directions) +
                ", prepTime=" + prepTime +
                ", cookingTime=" + cookingTime +
                ", totalTime=" + totalTime +
                ", servings=" + servings +
                ", protein=" + protein +
                ", fat=" + fat +
                ", carbs=" + carbs +
                ", stars=" + stars +
                ", numOfStarGivers=" + numOfStarGivers +
                ", images=" + images +
                ", comments=" + comments +
                '}';
    }
}
