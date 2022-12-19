package com.example.smarter_foodies;

public class User {

    public String Nickname,resume;
    public boolean firstEntry, isChef;

    public User() {
            firstEntry = true;
            isChef = false;
        }
    public User(String name) {
            Nickname = name;
            firstEntry = true;
        isChef = false;
    }
    public User(String name, String re){
        Nickname = name;
        resume = re;
        firstEntry = true;
        isChef = false;
    }

}
