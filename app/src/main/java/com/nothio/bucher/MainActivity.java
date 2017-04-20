package com.nothio.bucher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nothio.bucher.databinding.ActivityMainBinding;
import com.nothio.bucher.model.Section;
import com.nothio.bucher.util.PermissionUtil;
import com.nothio.bucher.util.SectionAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MyApp appState;
    List<Section> sectionz = new ArrayList<Section>();
    Section section = new Section();
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        appState = ((MyApp) getApplicationContext());
        try {
            id = getIntent().getIntExtra("id", 0);
            sectionz = appState.sectionDao.queryForEq("parent", id);
            if (id != 0)
                section = appState.sectionDao.queryForEq("id", id).get(0);
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        binding.list.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            binding.list.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        }
        binding.list.setHasFixedSize(true);
        binding.list.setAdapter(new SectionAdapter(MainActivity.this, appState, sectionz));

        if (!section.img.trim().equalsIgnoreCase("")) {
            binding.img.setVisibility(View.VISIBLE);
            binding.img.setImageResource(getResources().getIdentifier(section.img,
                    "drawable", getPackageName()));
        } else
            binding.img.setVisibility(View.GONE);

        if (!section.name.trim().equalsIgnoreCase("")) {
            binding.label.setVisibility(View.VISIBLE);
            binding.label.setText(section.name);
        } else
            binding.label.setVisibility(View.GONE);


    }

    public Boolean gotPermission(ArrayList<String> requiredPermission, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = PermissionUtil.permissionToAsk(MainActivity.this, requiredPermission);
            if (permissions.size() > 0) {
                ActivityCompat.requestPermissions(MainActivity.this, permissions.toArray(new String[permissions.size()])
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
                    Toast.makeText(MainActivity.this, getString(R.string.Error_NoPermission), Toast.LENGTH_SHORT).show();

                return;
            }
        }
    }

    public void Call() {
        Intent intent = new Intent("android.intent.action.CALL",
                Uri.parse(appState.temp));

        startActivity(intent);
    }
}
