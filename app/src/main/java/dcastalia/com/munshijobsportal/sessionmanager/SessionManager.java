package dcastalia.com.munshijobsportal.sessionmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import dcastalia.com.munshijobsportal.activity.LoginActivity;

/**
 * Created by PT on 3/5/2017.
 */

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "dcastalia";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME="name";
    public static final String KEY_PHONE= "phone";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PASSPORT = "passport";
    public static final String KEY_VERIFICATION_CODE= "code";
    public static final String KEY_ID = "id";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String  id , String name ,  String phone, String passport ,String verificationCode){
        // Storing login value as TRUE

        editor.putString(KEY_NAME,name);
        // Storing name in pref
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_PASSPORT,passport);
        editor.putString(KEY_ID,id);
        editor.putString(KEY_VERIFICATION_CODE,verificationCode);
        // commit changes
        editor.commit();
    }
    public void setLoginSession(boolean isLoggedIn){
        editor.putBoolean(IS_LOGIN, isLoggedIn);
        editor.commit();
    }

    public void setPassword(String password){
        editor.putString(KEY_PASSWORD , password);
        editor.commit();
    }
    public void setUserName(String userName){
        editor.putString(KEY_NAME , userName);
        editor.commit();
    }
    public void setPhone(String phone){
        editor.putString(KEY_PHONE , phone);
        editor.commit();
    }

        public String getUserId(){
        return pref.getString(KEY_ID, null);
    }


    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }



    }
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_PASSPORT, pref.getString(KEY_PASSPORT, null));
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        user.put(KEY_VERIFICATION_CODE,pref.getString(KEY_VERIFICATION_CODE,null));
        // return user
        return user;
    }

    public boolean isLoggedIn(){

        return pref.getBoolean(IS_LOGIN, false);
    }
    public void logoutUser(){
        editor.putBoolean(IS_LOGIN,false);
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);
    }


}
