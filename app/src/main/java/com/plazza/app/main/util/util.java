package com.plazza.app.main.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;

import com.plazza.app.main.BaseActivity;
import com.plazza.app.main.MainActivity;
import com.plazza.app.main.MyApp;
import com.plazza.app.main.SettingActivity;
import com.plazza.app.main.model.Section;

import java.util.ArrayList;

public class util {

    static Typeface kodakFont, yekanFont, glyph;

    public static Typeface kodakFont(Context c) {
        if (kodakFont == null)
            kodakFont = Typeface.createFromAsset(c.getAssets(), "fonts/BKoodak.ttf");
        return kodakFont;
    }

    public static Typeface yekanFont(Context c) {
        if (yekanFont == null)
            yekanFont = Typeface.createFromAsset(c.getAssets(), "fonts/BYekan.ttf");
        return yekanFont;
    }

    public static Typeface glyph(Context c) {
        if (yekanFont == null)
            yekanFont = Typeface.createFromAsset(c.getAssets(), "fonts/glyph.ttf");
        return yekanFont;
    }


    public static void handleCallBack(Context context, MyApp appState, Section tweet) {
        if (tweet.type == SectionType.Call.value()) {
            appState.temp = "tel:"
                    + tweet.descr.trim().replace("call-", "")
                    .replace("#", Uri.encode("#"));

            ArrayList<String> requiredPermission = new ArrayList<String>() {{
                add(Manifest.permission.CALL_PHONE);
            }};

            if (((BaseActivity) context).gotPermission(requiredPermission, 1))
                ((BaseActivity) context).Call();
        } else if (tweet.type == SectionType.Link.value() && tweet.op2 == 1) {
            String url = "";
            if (!tweet.descr.trim().startsWith("http://"))
                url = "http://";
            url = url + tweet.descr.trim();
            Intent browse = new Intent(Intent.ACTION_VIEW, Uri
                    .parse(url));
            context.startActivity(browse);

        } else {
            if (tweet.descr != null && tweet.descr.equalsIgnoreCase("|setting|")) {
                Intent i = new Intent(context, SettingActivity.class);
                context.startActivity(i);
            } else {
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("id", tweet.id);
                context.startActivity(i);
            }
        }
    }

    public static Drawable getDrawableForStore(Activity activity, String img) {
        Bitmap thumbnail = null;
        try {
            thumbnail = BitmapFactory.decodeStream(activity.getAssets().open(img));
        } catch (Exception ex) {
            Log.e("getThumbnail()", ex.getMessage());
            return null;
        }

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float scaledDensity = metrics.density;
        int width = thumbnail.getWidth();
        int height = thumbnail.getHeight();

        if (scaledDensity < 1) {

            width = (int) (width * scaledDensity);
            height = (int) (height * scaledDensity);
        } else {
            width = (int) (width + width * (scaledDensity - 1));
            height = (int) (height + height * (scaledDensity - 1));
        }

        thumbnail = Bitmap.createScaledBitmap(thumbnail, width, height, true);
        Drawable d = new BitmapDrawable(activity.getResources(), thumbnail);

        return d;

    }

}
