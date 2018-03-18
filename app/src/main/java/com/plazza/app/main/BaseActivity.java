package com.plazza.app.main;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.j256.ormlite.stmt.QueryBuilder;
import com.plazza.app.main.databinding.ActivityBaseBinding;
import com.plazza.app.main.model.Feed;
import com.plazza.app.main.model.FeedItem;
import com.plazza.app.main.model.Section;
import com.plazza.app.main.service.RssService;
import com.plazza.app.main.util.PermissionUtil;
import com.plazza.app.main.util.RssAdapter;
import com.plazza.app.main.util.SectionAdapter;
import com.plazza.app.main.util.SectionType;
import com.plazza.app.main.util.util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class BaseActivity extends AppCompatActivity {


    DrawerLayout drawerLayout;
    DrawerFragment drawerFragment;
    MyApp appState;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
                closeDrawer();
            else
                openDrawer();

            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                closeDrawer();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(Gravity.RIGHT);
    }

    public void openDrawer() {
        if (appState.menu)
            drawerLayout.openDrawer(Gravity.RIGHT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appState = ((MyApp) getApplicationContext());
    }

    protected void InitSetting(DrawerLayout _drawerLayout, DrawerFragment _drawerFragment,TextView left,TextView right,TextView header) {
        drawerLayout = _drawerLayout;
        drawerFragment = _drawerFragment;

        header.setText(R.string.app_name);

        right.setTypeface(util.glyph(BaseActivity.this));
        left.setTypeface(util.glyph(BaseActivity.this));

        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openDrawer();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSearchRequested();
            }
        });

        right.setVisibility(appState.getSettingBool("menuBTN") ? View.VISIBLE : View.GONE);
        left.setVisibility(appState.getSettingBool("findBTN") ? View.VISIBLE : View.GONE);


    }

    public Boolean gotPermission(ArrayList<String> requiredPermission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = PermissionUtil.permissionToAsk(BaseActivity.this, requiredPermission);
            if (permissions.size() > 0) {
                ActivityCompat.requestPermissions(BaseActivity.this, permissions.toArray(new String[permissions.size()])
                        ,
                        requestCode);
                return false;
            } else
                return true;
        } else
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                boolean perform = true;

                for (int i = 0, len = permissions.length; i < len; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                        perform = false;

                }
                if (perform)
                    Call();
                else
                    Toast.makeText(BaseActivity.this, getString(R.string.Error_NoPermission), Toast.LENGTH_SHORT).show();

                return;
            }
        }
    }

    public void Call() {
        Intent intent = new Intent("android.intent.action.CALL",
                Uri.parse(appState.temp));

        startActivity(intent);
    }

    public class SSLTolerentWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
