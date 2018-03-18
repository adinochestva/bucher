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
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.j256.ormlite.stmt.QueryBuilder;
import com.plazza.app.main.databinding.ActivityBaseBinding;
import com.plazza.app.main.databinding.ActivitySettingBinding;
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

public class SettingActivity extends BaseActivity {


    ActivitySettingBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        InitSetting(binding.drawerLayout, ((DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_view)), binding.hTLeft, binding.hTRight
                , binding.headerTitle);

        binding.FontSizeVal.setProgress(appState.getSettingInt("fontsize"));
        binding.FontSizeNr.setText(binding.FontSizeVal.getProgress() + "");

        switch (appState.getSettingInt("font")) {
            case 0:
                binding.radioYekan.setChecked(true);
                break;
            case 1:
                binding.radioKoodak.setChecked(true);
                break;
        }

        binding.FontSizeVal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.FontSizeNr.setText(progress + "");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appState.setSetting("font", binding.radioYekan.isChecked() ? 0 : 1);
                appState.setSetting("fontsize", binding.FontSizeVal.getProgress());
                finish();
            }
        });

    }


}
