package com.nothio.bucher;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.nothio.bucher.databinding.ActivityMainBinding;
import com.nothio.bucher.model.Section;
import com.nothio.bucher.util.SectionAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MyApp appState;
    List<Section> sectionz = new ArrayList<Section>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        appState = ((MyApp) getApplicationContext());
        try {
            sectionz = appState.sectionDao.queryForEq("parent", 0);
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        binding.list.setAdapter(new SectionAdapter(MainActivity.this, sectionz));
    }
}
