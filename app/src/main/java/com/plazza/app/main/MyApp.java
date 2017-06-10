package com.plazza.app.main;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.plazza.app.main.data.DatabaseHelper;
import com.plazza.app.main.model.Section;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;

public class MyApp extends Application {
    private String APP_SHARED_PREFS;
    private SharedPreferences appSharedPrefs;
    private Editor prefsEditor;
    public String temp = "";
    public volatile Dao<Section, Integer> sectionDao;
    public volatile Typeface kodakFont, glyphicon;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        APP_SHARED_PREFS = getApplicationContext().getPackageName() + ".pref";
        try {
            this.sectionDao = new DatabaseHelper(getApplicationContext())
                    .getSectionDao();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        kodakFont = Typeface.createFromAsset(getApplicationContext()
                .getAssets(), "fonts/BKoodak.ttf");
        glyphicon = Typeface.createFromAsset(getApplicationContext()
                .getAssets(), "fonts/BYekan.ttf");

        this.appSharedPrefs = getApplicationContext().getSharedPreferences(
                APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();


        if (!getPref("preversion", "0").equalsIgnoreCase(BuildConfig.VERSION_CODE + "")) {
            // upgraded
            prefClear();
            setFontSize(15);
        }

        setPref("preversion", BuildConfig.VERSION_CODE + "");


    }

    public int getFontSize() {
        return getPrefInt("fontsize");
    }

    public void setFontSize(int fontSize) {
        setPref("fontsize", fontSize);
    }

    // Pref
    public String getPref(String key, String def) {
        return appSharedPrefs.getString(key, def);
    }

    public int getPrefInt(String key) {
        try {
            return appSharedPrefs.getInt(key, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public Boolean getPrefBoolean(String key) {
        return appSharedPrefs.getBoolean(key, true);
    }

    public void setPref(String key, String text) {
        prefsEditor.putString(key, text);
        prefsEditor.commit();
    }

    public void setPref(String key, Boolean text) {
        prefsEditor.putBoolean(key, text);
        prefsEditor.commit();
    }

    public void prefClear() {
        prefsEditor.clear();
        prefsEditor.commit();
    }

    public void setPref(String key, int text) {
        prefsEditor.putInt(key, text);
        prefsEditor.commit();
    }

}
