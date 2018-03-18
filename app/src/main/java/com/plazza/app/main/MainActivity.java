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
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends BaseActivity {


    ActivityBaseBinding binding;
    List<Section> sectionz = new ArrayList<Section>();
    Section section = new Section();
    int id, i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_base);

        binding.tLeft.setTypeface(util.glyph(MainActivity.this));
        binding.tRight.setTypeface(util.glyph(MainActivity.this));


        InitSetting(binding.drawerLayout, ((DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_view)), binding.hTLeft, binding.hTRight
                , binding.headerTitle);

        try {
            Intent intent = getIntent();
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                String search = (intent.getStringExtra(SearchManager.QUERY) == null ? intent
                        .getDataString() : intent
                        .getStringExtra(SearchManager.QUERY));
                if (search.isEmpty())
                    finish();

                section = new Section();
                section.descr = search;
                section.name = getString(R.string.searching) + " " + search;
                section.op1 = 1;
                section.op2 = 1;
                section.type = SectionType.Search.value();

            } else {
                id = intent.getIntExtra("id", 0);
                if (id == -1) {
                    section = new Section();
                    section.descr = intent.getStringExtra("url");
                    section.type = SectionType.Link.value();
                } else if (id == 0)
                    section = appState.sectionDao.queryBuilder().where().eq("place", 0)
                            .and().eq("parent", 0).queryForFirst();
                else
                    section = appState.sectionDao.queryForEq("id", id).get(0);
            }

            QueryBuilder<Section, Integer> qb = appState.sectionDao.queryBuilder();
            qb.where().eq("parent", 0);
            qb.where().eq("place", 1);
            qb.orderBy("or", true);
            if (!appState.getSettingBool("menu") || qb.query().size() == 0) {
                appState.menu = false;
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            finish();
        }

        if (section.op1 == 1 && !section.name.trim().equalsIgnoreCase("")) {
            binding.headerTitle.setVisibility(View.VISIBLE);
            binding.headerTitle.setText(section.name);
        }


        if (section.type == SectionType.List.value() || section.type == SectionType.Search.value()) {

            try {

                QueryBuilder<Section, Integer> qb = appState.sectionDao.queryBuilder();
                if (section.type == SectionType.List.value())
                    qb.where().eq("parent", section.id);
                else
                    qb.where().like("descr", "%" + section.descr + "%").or().like("descr2", "%" + section.descr + "%").or().like("descr3", "%" + section.descr + "%")
                            .or().like("name", "%" + section.descr + "%");
                qb.orderBy("or", true);
                sectionz = qb.query();

            } catch (java.sql.SQLException e) {
                finish();
            }


            binding.listView.setVisibility(View.VISIBLE);

            if (section.op1 == 1 && section.op2 == 1) {
                binding.listTitle.setVisibility(View.VISIBLE);
                binding.listTitle.setText(section.name);
                binding.headerTitle.setText(R.string.app_name);
            }


            if (section.op3 == 0)
                binding.list.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            else
                binding.list.setLayoutManager(new GridLayoutManager(MainActivity.this, section.op4));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                binding.list.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
            binding.list.setHasFixedSize(true);
            binding.list.setAdapter(new SectionAdapter(MainActivity.this, appState, sectionz));

        } else if (section.type == SectionType.HTML.value()) {
            binding.web.setVisibility(View.VISIBLE);
            binding.webvu.getSettings().setUseWideViewPort(true);
            binding.webvu.getSettings().setLoadWithOverviewMode(true);
            binding.webvu.getSettings().setBuiltInZoomControls(true);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                binding.webvu.getSettings().setDisplayZoomControls(false);
            stopProgress();
            binding.web.setBackgroundColor(Color.parseColor(section.descr2.trim().isEmpty() ? "#ffffff" : "#" + section.descr2));
            binding.webvu.loadUrl("file:///android_asset/content/" + section.id + ".html");
        } else if (section.type == SectionType.Link.value()) {
            binding.web.setVisibility(View.VISIBLE);
            String url = "";
            if (!section.descr.trim().startsWith("http://"))
                url = "http://";
            url = url + section.descr.trim();
            binding.webvu.getSettings().setJavaScriptEnabled(true);
            binding.webvu.clearCache(true);
            binding.webvu.setWebViewClient(new SSLTolerentWebViewClient());
            binding.webvu.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            binding.webvu.setWebChromeClient(new WebChromeClient() {

                public void onProgressChanged(WebView view, int progress) {
                    startProgress();
                    if (progress == 100) {
                        stopProgress();
                        binding.webvu.requestFocus(View.FOCUS_DOWN);
                    }
                }
            });
            binding.webvu.loadUrl(url);
            binding.webvu.requestFocus(View.FOCUS_DOWN);
        } else if (section.type == SectionType.Rss.value()) {

            binding.mainprogress.setVisibility(View.VISIBLE);
            binding.list.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                binding.list.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
            binding.list.setHasFixedSize(true);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
            RssService service = retrofit.create(RssService.class);
            Call<Feed> req = service.getItems(section.descr);
            req.enqueue(new Callback<Feed>() {
                @Override
                public void onResponse(Call<Feed> call, Response<Feed> response) {

                    binding.mainprogress.setVisibility(View.GONE);
                    binding.listView.setVisibility(View.VISIBLE);

                    List<FeedItem> mItems = new ArrayList<>();
                    mItems = response.body().getmChannel().getFeedItems();

                    binding.list.setAdapter(new RssAdapter(MainActivity.this, appState, mItems, section.op2 == 1));
                }

                @Override
                public void onFailure(Call<Feed> call, Throwable throwable) {
                    binding.mainprogress.setVisibility(View.GONE);
                    binding.listView.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, R.string.Error_ServerFailed, Toast.LENGTH_LONG).show();
                }
            });


        } else if (section.type == SectionType.Contact.value()) {
            binding.contact.setVisibility(View.VISIBLE);

            if (section.descr != null && !section.descr.toString().trim().isEmpty())
                binding.topDescr.setText(section.descr);
            else
                binding.topDescr.setVisibility(View.GONE);

            if (section.op2 == 1) {
                binding.subjectLayout.setVisibility(View.VISIBLE);
                binding.subject.setHint(section.descr3);

                if (section.img != null && !section.img.toString().trim().isEmpty())
                    binding.subject.setText(section.img);
            } else
                binding.subjectLayout.setVisibility(View.GONE);

            binding.body.setHint(section.descr2);

            binding.send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (binding.body.getText().toString().trim().length() < 10) {
                        binding.bodyLayout.setError(getString(R.string.Error_MinimumLength10));
                        return;
                    } else
                        binding.bodyLayout.setError("");

                    if (section.op2 == 1)
                        if (binding.subject.getText().toString().trim().length() < 10) {
                            binding.subjectLayout.setError(getString(R.string.Error_MinimumLength10));
                            return;
                        } else
                            binding.subjectLayout.setError("");


                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{section.name});
                    i.putExtra(Intent.EXTRA_SUBJECT, (section.op2 == 1) ? binding.subject.getText().toString() : section.img);
                    i.putExtra(Intent.EXTRA_TEXT, binding.body.getText().toString());
                    try {
                        startActivity(Intent.createChooser(i, "ارسال ایمیل ...."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(MainActivity.this, R.string.Error_EmailApp, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (section.type == SectionType.Gallery.value()) {
            binding.gallery.setVisibility(View.VISIBLE);


            try {

                QueryBuilder<Section, Integer> qb = appState.sectionDao.queryBuilder();
                qb.where().eq("parent", id);
                qb.orderBy("or", true);
                sectionz = qb.query();

            } catch (java.sql.SQLException e) {
                finish();
            }


            binding.tLeft.setVisibility(View.VISIBLE);
            binding.tRight.setVisibility(View.VISIBLE);

            binding.tRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoadGallerImage(true, false);
                }
            });
            binding.tLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoadGallerImage(false, false);
                }
            });
            LoadGallerImage(false, true);
        } else if (section.type == SectionType.Image.value()) {
            binding.gallery.setVisibility(View.VISIBLE);

            binding.tLeft.setVisibility(View.GONE);
            binding.tRight.setVisibility(View.GONE);

            sectionz = new ArrayList<>();
            sectionz.add(section);


            LoadGallerImage(false, true);
        } else if (section.type == SectionType.Video.value()) {
            binding.video.setVisibility(View.VISIBLE);

            binding.progress.bringToFront();
            binding.vid.setMediaController(new MediaController(this));
            binding.vid.setVideoURI(Uri.parse(section.descr));
            binding.video.requestFocus();
            binding.vid.start();
            binding.vid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {

                    binding.videoProgress.setVisibility(View.GONE);
                    binding.video.bringToFront();
                    mp.start();

                }
            });
        }


    }

    void LoadGallerImage(Boolean next, Boolean inistial) {
        if (inistial)
            i = 0;
        else {
            if (next) {
                if (i < (sectionz.size() - 1))
                    i++;
                else
                    i = 0;
            } else {
                if (i == 0)
                    i = sectionz.size() - 1;
                else
                    i--;
            }
        }

        Section sec = sectionz.get(i);

        if (sec.op2 == 1) {
            binding.galleryProgress.setVisibility(View.VISIBLE);
            Glide.with(MainActivity.this).load(sec.descr).listener(new RequestListener<String, GlideDrawable>() {

                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    binding.galleryProgress.setVisibility(View.GONE);
                    binding.image.setVisibility(View.VISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    binding.galleryProgress.setVisibility(View.GONE);
                    binding.image.setVisibility(View.VISIBLE);
                    return false;
                }
            }).crossFade().into(binding.image);
        } else {
            try {
                binding.galleryProgress.setVisibility(View.GONE);
                binding.image.setVisibility(View.VISIBLE);
//                binding.image.setImageDrawable(Drawable.createFromStream(getAssets().open("content/images/" + sec.img), null));
                binding.image.setImageDrawable(util.getDrawableForStore(MainActivity.this, "content/images/" + sec.img));
            } catch (Exception ex) {
                return;
            }
        }

        if (sec.descr2 != null && !sec.descr2.trim().isEmpty()) {
            binding.imageDescr.setVisibility(View.VISIBLE);
            binding.imageDescr.setText(sec.descr2);
        } else
            binding.imageDescr.setVisibility(View.GONE);

    }


    void startProgress() {
        binding.progress.setVisibility(View.VISIBLE);
    }

    void stopProgress() {
        binding.progress.setVisibility(View.GONE);
    }

}
