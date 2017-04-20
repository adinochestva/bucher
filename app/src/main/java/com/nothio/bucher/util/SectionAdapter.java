package com.nothio.bucher.util;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.animation.ObjectAnimator;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

import com.nothio.bucher.DetailActivity;
import com.nothio.bucher.MainActivity;
import com.nothio.bucher.MyApp;
import com.nothio.bucher.R;
import com.nothio.bucher.model.Section;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {
    private Context context;
    List<Section> nodes;
    public MyApp appState;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView lable;
        public ImageView img;
        public CardView GridItem;

        public ViewHolder(View v) {
            super(v);

            lable = (TextView) v.findViewById(R.id.lable);
            img = (ImageView) v.findViewById(R.id.img);
            GridItem = (CardView) v.findViewById(R.id.GridItem);

        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public SectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v;

        // create a new view
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.section_row, parent, false);
        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Section tweet = nodes.get(position);
        holder.lable.setText(tweet.name);

        if (tweet.img != null && !tweet.img.trim().equalsIgnoreCase("")) {
            holder.img.setImageResource(context.getResources()
                    .getIdentifier(tweet.img, "drawable",
                            context.getPackageName()));
            holder.img.setVisibility(View.VISIBLE);
        } else {
            holder.img.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.lable
                    .getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(5, 5, 5, 5);
            holder.lable.setLayoutParams(params);
        }
        holder.GridItem.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
//		holder.GridItem.setForeground(getContext().getResources().getDrawable(
//				R.color.griditem_white_selector));
        holder.GridItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (tweet.descr != null && tweet.descr.trim().startsWith("call-")) {

                    appState.temp = "tel:"
                            + tweet.descr.trim().replace("call-", "")
                            .replace("#", Uri.encode("#"));

                    ArrayList<String> requiredPermission = new ArrayList<String>() {{
                        add(Manifest.permission.CALL_PHONE);
                    }};

                    if (((MainActivity) context).gotPermission(requiredPermission, 1))
                        ((MainActivity) context).Call();

                } else if (tweet.descr != null && !tweet.descr.trim().equalsIgnoreCase("")) {
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("id", tweet.id);
                    context.startActivity(i);
                } else {
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra("id", tweet.id);
                    context.startActivity(i);
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return nodes.size();
    }


    public SectionAdapter(Context cnt, MyApp appState, List<Section> nz) {
        this.appState = appState;
        context = cnt;
        nodes = nz;
    }


}
