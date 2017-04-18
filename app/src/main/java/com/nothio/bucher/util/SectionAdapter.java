package com.nothio.bucher.util;

import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nothio.model.Section;
import com.nothio.rooter.DetailActivity;
import com.nothio.rooter.ListActivity;
import com.nothio.rooter.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.Animator;

public class SectionAdapter extends ArrayAdapter<Section> {
	List<Section> nodes;
	int resourceDrawable;
	public int lastPosition = -1;
	public Boolean gotAnimation = false;

	// public DrawableManager dmanager;
	// public ImageLoader imgloader;

	public SectionAdapter(Context cnt, List<Section> nz) {
		super(cnt, R.layout.section_row, nz);
		resourceDrawable = R.layout.section_row;
		nodes = nz;
		// dmanager = new DrawableManager(a);
		// imgloader = new ImageLoader(a, true);
	}

	public static class ViewHolder {
		TextView lable;
		ImageView img;
		CardView GridItem;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		final ViewHolder holder;
		// if (v == null) {
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(resourceDrawable, null);
		holder = new ViewHolder();
		holder.lable = (TextView) v.findViewById(R.id.lable);
		holder.img = (ImageView) v.findViewById(R.id.img);
		// holder.up = (ImageView) v.findViewById(R.id.vote_up);
		// holder.down = (ImageView) v.findViewById(R.id.vote_down);
		holder.GridItem = (CardView) v.findViewById(R.id.GridItem);

		// v.setTag(holder);
		// } else
		// holder = (ViewHolder) v.getTag();

		final Section tweet = nodes.get(position);
		holder.lable.setText(tweet.name);

		if (!tweet.getImg().trim().equalsIgnoreCase(""))
			holder.img.setImageResource(getContext().getResources()
					.getIdentifier(tweet.img, "drawable",
							getContext().getPackageName()));
		else {
			holder.img.setVisibility(View.GONE);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.lable
					.getLayoutParams();
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.setMargins(5, 5, 5, 5);
			holder.lable.setLayoutParams(params);
		}
		holder.GridItem.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
		holder.GridItem.setForeground(getContext().getResources().getDrawable(
				R.color.griditem_white_selector));
		holder.GridItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (tweet.getDescr().trim().startsWith("call-")) {

					String url = "tel:"
							+ tweet.descr.trim().replace("call-", "")
									.replace("#", Uri.encode("#"));

					Intent intent = new Intent("android.intent.action.CALL",
							Uri.parse(url));

					getContext().startActivity(intent);
				} else if (!tweet.getDescr().trim().equalsIgnoreCase("")) {
					Intent i = new Intent(getContext(), DetailActivity.class);
					i.putExtra("id", tweet.id);
					getContext().startActivity(i);
				} else {
					Intent i = new Intent(getContext(), ListActivity.class);
					i.putExtra("id", tweet.id);
					getContext().startActivity(i);
				}
			}
		});

		if (position == (lastPosition + 1)) {
			ObjectAnimator
					.ofFloat(v, "translationY",
							parent.getMeasuredHeight() >> 1, 0)
					.setDuration(300).start();
		}

		if (position > lastPosition)
			lastPosition = position;

		return v;
	}

}
