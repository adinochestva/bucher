package com.plazza.app.main;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.plazza.app.main.data.DatabaseHelper;
import com.plazza.app.main.model.Section;
import com.plazza.app.main.util.PostParser;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    MyApp appState;
    Section section = new Section();
    PostParser pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        appState = ((MyApp) getApplicationContext());
        pp = new PostParser(DetailActivity.this, appState);
        try {
            int id = getIntent().getIntExtra("id", 0);
            section = appState.sectionDao.queryForEq("id", id).get(0);
        } catch (java.sql.SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ImageView img = (ImageView) findViewById(R.id.img);
        TextView lable = (TextView) findViewById(R.id.lable);

        lable.setText(section.name);

        if (!section.img.trim().equalsIgnoreCase(""))
            try {
                img.setImageDrawable(Drawable.createFromStream(getAssets().open(section.img), null));
            } catch (Exception e) {
            }
        else {
            img.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lable
                    .getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(5, 5, 5, 5);
            lable.setLayoutParams(params);
        }

        LinearLayout descr = (LinearLayout) findViewById(R.id.descr);
        pp.showBBCode(descr, section.descr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
