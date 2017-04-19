package com.nothio.bucher;

import java.util.ArrayList;
import java.util.List;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.nothio.bucher.data.DatabaseHelper;
import com.nothio.bucher.model.Section;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ImageActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	MyApp appState;
	Section section = new Section();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		appState = ((MyApp) getApplicationContext());

		String img = getIntent().getStringExtra("img");

//		GestureImageView image = (GestureImageView) findViewById(R.id.image);
//		image.setImageResource(getResources().getIdentifier(img, "drawable",
//				getPackageName()));
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
