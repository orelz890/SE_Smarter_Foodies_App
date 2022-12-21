package com.example.smarter_foodies;

public class User {

    public String Nickname,resume;
    public boolean firstEntry, isChef;
    public String Email;
    public String Eating;
    public String Favrorit;
    public String Wabsite;

    public User() {
            firstEntry = true;
            isChef = false;
        }
    public User(String name) {
            Nickname = name;
            firstEntry = true;
            isChef = false;
            Eating= " ";
            Email = " ";
            Favrorit = " ";
            Wabsite = " " ;




    }
    public User(String name, String re){
        Nickname = name;
        resume = re;
        firstEntry = true;
        isChef = false;
    }

}
