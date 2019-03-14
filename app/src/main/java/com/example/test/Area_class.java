package com.example.test;

public class Area_class
{
  private   String Area_name;
  private   double Area_latitude , Area_longitude;

    public Area_class()
    {

    }

    public Area_class(String area_name, double area_latitude, double area_longitude)
    {
        Area_name = area_name;
        Area_latitude = area_latitude;
        Area_longitude = area_longitude;
    }

    public String getArea_name()
    {
        return Area_name;
    }

    public void setArea_name(String area_name)
    {
        Area_name = area_name;
    }

    public double getArea_latitude()
    {
        return Area_latitude;
    }

    public void setArea_latitude(double area_latitude)
    {
        Area_latitude = area_latitude;
    }

    public double getArea_longitude()
    {
        return Area_longitude;
    }

    public void setArea_longitude(double area_longitude)
    {
        Area_longitude = area_longitude;
    }
}
