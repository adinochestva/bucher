package com.nothio.bucher;

import java.util.ArrayList;
import java.util.List;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;
import com.nothio.bucher.data.DatabaseHelper;
import com.nothio.bucher.model.Section;
import com.nothio.bucher.util.SectionAdapter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	MyApp appState;
	List<Section> sectionz = new ArrayList<Section>();
	Section section = new Section();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		appState = ((MyApp) getApplicationContext());
		try {
			int id = getIntent().getIntExtra("id", 0);
			sectionz = appState.sectionDao.queryForEq("parent", id);
			section = appState.sectionDao.queryForEq("id", id).get(0);
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageView img = (ImageView) findViewById(R.id.img);
		TextView lable = (TextView) findViewById(R.id.lable);

		lable.setText(section.name);

		if (!section.getImg().trim().equalsIgnoreCase(""))
			img.setImageResource(getResources().getIdentifier(section.img,
					"drawable", getPackageName()));
		else {
			img.setVisibility(View.GONE);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lable
					.getLayoutParams();
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.setMargins(5, 5, 5, 5);
			lable.setLayoutParams(params);
		}

		ListView lv = (ListView) findViewById(R.id.list);
		lv.setAdapter(new SectionAdapter(ListActivity.this, sectionz));

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
