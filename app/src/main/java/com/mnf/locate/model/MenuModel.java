package com.mnf.locate.model;

/**
 * Created by Muneef on 31/10/15.
 */
public class MenuModel {
    public String title;
    public String Ads;
    public int image;
    public String id;

    public MenuModel(String name, String cAds, int img, String id){
        this.title = name;
        this.Ads = cAds;
        this.image= img;
        this.id = id;
    }


}
