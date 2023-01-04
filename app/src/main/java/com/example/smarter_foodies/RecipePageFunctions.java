package com.example.smarter_foodies;

import android.content.Intent;

import java.util.*;

public class RecipePageFunctions {


    public static String is_full(String x) {
        if (x != null)
            return x;
        else
            return "---";
    }

    public static String[] List_of_string_to_array(List<String> l) {
        String[] ans = new String[l.size()];
        for (int i = 0; i < l.size(); i++) {
            ans[i] = l.get(i);
        }
        return ans;
    }

    public static String ingaridiants(String[] ingardiants) {
        String ingardiant = "";
        if (ingardiants != null) {
            for (int i = 0; i < ingardiants.length; i++) {
                ingardiant = ingardiant + ingardiants[i] + "\n";
            }
            return ingardiant;
        }
        return " no inga V2 ";
    }

    public static String directions(String[] direcations) {
        String direcation = "";
        if (direcations != null) {
            for (int i = 0; i < direcations.length; i++) {
                direcation = direcation + "STEP " + i + ": " + direcations[i] + "\n\n";
            }
            return direcation;
        }
        return "no dic V2 ";
    }
    public static boolean isNumeric(String s){
        if (s == null)
            return false;
        try{
            double d = Double.parseDouble(s);
        }
        catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    public static int Proper_time_int(String time) {
        if (time != null) {
            try {
                String[] temp = time.split(" ");
                if (temp.length < 3) {
                    if (temp[1].equals("hrs")) {
                        return ((Integer.parseInt(temp[0])) * 60);
                    }
                    return Integer.parseInt(temp[0]);
                }
                else {
                    if (!isNumeric(temp[0])){
                        return ((Integer.parseInt(temp[1]) * 60) + Integer.parseInt(temp[3]));}
                    else{
                        return ((Integer.parseInt(temp[0]) * 60) + Integer.parseInt(temp[2]));}

                }
            } catch (Exception e) {
                System.out.println("error:could not convert- " + time);
            }
        }
        return -1;
    }

    public static void setIntentContent(Intent intent, recipe res) {
        //List<String> images = myFoodList.get(foodViewHolder.getAdapterPosition()).getImages();

        intent.putExtra("recipeImage", RecipePageFunctions.List_of_string_to_array(res.getImages()));

        intent.putExtra("CategoryAndSub", res.getCategory());
        intent.putExtra("name", res.getTitle());
        intent.putExtra("copyRights", res.getCopy_rights());

        intent.putExtra("carbs", res.getCarbs());
        intent.putExtra("protein", res.getProtein());
        intent.putExtra("fats", res.getFat());
        intent.putExtra("calories", res.getCalories());

        intent.putExtra("ingardiants", RecipePageFunctions.List_of_string_to_array(res.getIngredients()));
        intent.putExtra("HowToMake", RecipePageFunctions.List_of_string_to_array(res.getDirections()));

        intent.putExtra("prepTime", res.getPrepTime());
        intent.putExtra("cookTime", res.getCookingTime());
        intent.putExtra("servings", res.getServings());


    }
}
