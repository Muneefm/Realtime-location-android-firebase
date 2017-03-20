package com.mnf.locate.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Muneef on 09/07/2016.
 */

public class PreferensHandler {
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context c;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "settings_pref";
    final String searchPref= "count";
    final String login_username= "username";
    final String login_password= "password";



    final String user_login = "bool_login";
    final String user_id = "user_id";
    final String user_name = "user_name";
    final String user_mob = "user_mob";
   Set<String> set = new HashSet<String>();
final String menuItem = "item";


    @SuppressLint("CommitPrefEdits")
    public PreferensHandler(Context context) {
        this.c = context;
        pref = c.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setLoginB(boolean var){
        Log.e("TAG","in Set"+var );
        editor.putBoolean(user_login, var);
        editor.commit();
    }
    public boolean getLoginB(){
        return pref.getBoolean(user_login, false);
    }

    public void setEmailuser(String var){
        editor.putString(login_username, var);
        editor.commit();
    }

    public String getEmailuser(){
        return pref.getString(login_username, "");
    }

    public void setMenuItems(HashSet<String> var){
        editor.putStringSet(menuItem, var);
        editor.commit();
    }

    public Set<String> getMenuItems(){
        return pref.getStringSet(menuItem, set);
    }


   /* public void setPassuser(String var){
        editor.putString(login_password, var);
        editor.commit();
    }
    public String getPassuser(){
        return pref.getString(login_password, "");
    }




    public void setUserId(String var){
        editor.putString(user_id, var);
        editor.commit();
    }
    public String getUserId(){
        return pref.getString(user_id, "");
    }


    public void setUserName(String var){
        editor.putString(user_name, var);
        editor.commit();
    }
    public String getUserName(){
        return pref.getString(user_name, "");
    }


    public void setUserMob(String var){
        editor.putString(user_mob, var);
        editor.commit();
    }
    public String getUserMob(){
        return pref.getString(user_mob, "");
    } */

}
