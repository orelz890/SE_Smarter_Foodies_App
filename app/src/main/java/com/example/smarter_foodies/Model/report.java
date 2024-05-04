package com.example.smarter_foodies.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class report {


    @SerializedName("verbal_abuse")
    private int verbal_abuse;


    @SerializedName("inappropriate_image")
    private int inappropriate_image;


    @SerializedName("hate_speech")
    private int hate_speech;


    @SerializedName("offensive_name")
    private int offensive_name;


    @SerializedName("reporters")
    private List<String> reporters;

    @SerializedName("recipe_ref")
    private String recipe_ref;

    public report() {
        this("");
    }

    public report(String ref) {
        verbal_abuse = 0;
        inappropriate_image = 0;
        hate_speech = 0;
        offensive_name = 0;
        reporters = new ArrayList<>();
        recipe_ref = ref;
    }

    public report(int verbal_Abuse, int inappropriate_Image, int hate_Speech, int offensive_Name, List<String> reporters, String recipe_Ref) {
        verbal_abuse = verbal_Abuse;
        inappropriate_image = inappropriate_Image;
        hate_speech = hate_Speech;
        offensive_name = offensive_Name;
        this.reporters = reporters;
        recipe_ref = recipe_Ref;
    }

    public void IncrementReports(boolean image, boolean name, boolean verbal, boolean hate) {

        if (image) {
            this.inappropriate_image++;
        }
        if (name) {
            this.offensive_name++;
        }
        if (verbal) {
            this.verbal_abuse++;
        }
        if (hate) {
            this.hate_speech++;
        }
    }

    public String getRecipe_ref() {
        return recipe_ref;
    }

    public void setRecipe_ref(String recipe_ref) {
        this.recipe_ref = recipe_ref;
    }

    public List<String> getReporters() {
        return reporters;
    }

    public void setReporters(List<String> reporters) {
        this.reporters = reporters;
    }

    public void addReporter(String uid) {
        reporters.add(uid);
    }

    public int getVerbal_abuse() {
        return verbal_abuse;
    }

    public void setVerbal_abuse(int verbal_abuse) {
        this.verbal_abuse = verbal_abuse;
    }

    public void increment_abuse(int by){
        this.verbal_abuse += by;
    }

    public int getInappropriate_image() {
        return inappropriate_image;
    }

    public void setInappropriate_image(int inappropriate_image) {
        this.inappropriate_image = inappropriate_image;
    }

    public void increment_inappropriate(int by){
        this.inappropriate_image += by;
    }

    public int getHate_speech() {
        return hate_speech;
    }

    public void setHate_speech(int hate_speech) {
        this.hate_speech = hate_speech;
    }

    public void increment_hate(int by){
        this.hate_speech += by;
    }

    public int getOffensive_name() {
        return offensive_name;
    }

    public void setOffensive_name(int offensive_name) {
        this.offensive_name = offensive_name;
    }

    public void increment_offensive(int by){
        this.offensive_name += by;
    }
}
