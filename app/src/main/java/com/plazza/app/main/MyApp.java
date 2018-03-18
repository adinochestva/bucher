package com.plazza.app.main;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.plazza.app.main.data.DatabaseHelper;
import com.plazza.app.main.model.Section;
import com.plazza.app.main.model.Setting;

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
    public Boolean menu = true;
    public volatile Dao<Section, Integer> sectionDao;
    public volatile Dao<Setting, Integer> settingDao;


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        APP_SHARED_PREFS = getApplicationContext().getPackageName() + ".pref";
        try {
            this.sectionDao = new DatabaseHelper(getApplicationContext())
                    .getSectionDao();
            this.settingDao = new DatabaseHelper(getApplicationContext())
                    .getSettingDao();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.appSharedPrefs = getApplicationContext().getSharedPreferences(
                APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();


        if (!getPref("preversion", "0").equalsIgnoreCase(BuildConfig.VERSION_CODE + "")) {
            // upgraded
            prefClear();
        }

        setPref("preversion", BuildConfig.VERSION_CODE + "");


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


    public boolean getSettingBool(String name) {
        try {
            Setting s = settingDao.queryForEq("name", name).get(0);
            return s.val.equalsIgnoreCase("1");
        } catch (Exception e) {
            return false;
        }
    }

    public int getSettingInt(String name) {
        try {
            Setting s = settingDao.queryForEq("name", name).get(0);
            return Integer.parseInt(s.val);
        } catch (Exception e) {
            return 0;
        }
    }

    public String getSetting(String name) {
        try {
            Setting s = settingDao.queryForEq("name", name).get(0);
            return s.val;
        } catch (Exception e) {
            return "";
        }
    }

    public void setSetting(String name, int val) {
        try {
            Setting s = new Setting();
            s.name = name;
            s.val = val + "";
            settingDao.createOrUpdate(s);
        } catch (Exception e) {

        }
    }

}
