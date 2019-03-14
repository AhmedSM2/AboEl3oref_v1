package com.example.test;

public class Likes {
    public String user_name,post_name;
    public boolean type;

    public Likes(String post_name, boolean type) {
        this.user_name = "me";
        this.post_name = post_name;
        this.type = type;
    }
}
