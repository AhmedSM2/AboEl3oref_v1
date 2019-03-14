package com.example.test;

public class user_class
{
    private String user_username;
    private String user_fullname;
    private String user_userAddress;

    public user_class(String user_username, String user_fullname, String user_userAddress)
    {
        this.user_username = user_username;
        this.user_fullname = user_fullname;
        this.user_userAddress = user_userAddress;
    }

    public String getUser_username()
    {
        return user_username;
    }

    public void setUser_username(String user_username)
    {
        this.user_username = user_username;
    }

    public String getUser_fullname()
    {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname)
    {
        this.user_fullname = user_fullname;
    }

    public String getUser_userAddress()
    {
        return user_userAddress;
    }

    public void setUser_userAddress(String user_userAddress)
    {
        this.user_userAddress = user_userAddress;
    }


}
