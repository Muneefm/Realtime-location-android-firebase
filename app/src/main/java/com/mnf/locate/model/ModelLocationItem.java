package com.mnf.locate.model;

/**
 * Created by muneef on 21/03/17.
 */

public class ModelLocationItem {
    private String name;
    private double lat;
    private double longi;

    public ModelLocationItem(){

    }
    public ModelLocationItem(String name,double lat,double longi){
        this.name = name;
        this.lat = lat;
        this.longi = longi;
    }


    public String getName(){
        return name;
    }
    public void  setName(String name){
        this.name = name;
    }


    public double getLat(){
        return lat;
    }
    public void  setLat(double lat){
        this.lat = lat;
    }


    public double getLongi(){
        return longi;
    }
    public void  setLongi(double longi){
        this.longi = longi;
    }



}