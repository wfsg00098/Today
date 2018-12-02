package com.eat.today;

import java.io.Serializable;

/**
 * Created by Li on 2018/10/1.
 */

public class Dish implements Serializable {
    private int id;
    private String name;
    private int imgId;
    private double price;
    private int count=0;
    //private String info;

    public Dish(int id, String name, int imgId, double price){
        this.id=id;
        this.name=name;
        this.imgId=imgId;
        this.price=price;
    }

    public int getId() { return id;}
    public String getName()
    {
        return name;
    }
    public int getImgId(){
        return imgId;
    }
    public int getCount(){
        return count;
    }
    public void addCount(){ this.count++;}
    public void delCount(){ this.count--;}
    public double getPrice(){
        return price;
    }
    /*public String getInFo(){
        return info;
    }*/

}
