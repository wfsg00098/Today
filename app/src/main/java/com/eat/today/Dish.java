package com.eat.today;

import java.io.Serializable;

/**
 * Created by Li on 2018/10/1.
 */

public class Dish implements Serializable {
    private int id;
    private String name;
    private double price;
    private int count=0;
    private String imgUrl;
    private String type;
    private int calorie;
    private int liked;

    //private String info;

    public Dish(int id, String name, String imgUrl,double price,int liked,int calorie){
        this.id=id;
        this.name=name;
        this.imgUrl = imgUrl;
        this.price=price;
        this.liked = liked;
        this.calorie = calorie;
    }

    public int getId() { return id;}
    public String getName()
    {
        return name;
    }
    public String getImgUrl() {return imgUrl;}
    public int getCount(){
        return count;
    }
    public void addCount(){ this.count++;}
    public void delCount(){ this.count--;}
    public double getPrice(){
        return price;
    }
    public int getCalorie(){return calorie;}
    public int getLiked(){return liked;}

    /*public String getInFo(){
        return info;
    }*/

}
