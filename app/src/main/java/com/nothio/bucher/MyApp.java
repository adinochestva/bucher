package com.nothio.bucher;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.xml.datatype.Duration;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.nothio.bucher.data.DatabaseHelper;
import com.nothio.bucher.model.Section;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
                .getAssets(), "naskh.ttf");
        glyphicon = Typeface.createFromAsset(getApplicationContext()
                .getAssets(), "glyphicons.ttf");

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
