package com.plazza.app.main.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plazza.app.main.MyApp;
import com.plazza.app.main.R;
import com.plazza.app.main.model.Section;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private Context context;
    MyApp appState;
    List<Section> nodes;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView lbl_cat;
        public ImageView img;
        public TextView simg;
        public RelativeLayout catholder;
        public RelativeLayout imgholder;

        public ViewHolder(View v) {
            super(v);

            lbl_cat = (TextView) v.findViewById(R.id.catlbl);
            img = (ImageView) v.findViewById(R.id.img);
            simg = (TextView) v.findViewById(R.id.simg);
            catholder = (RelativeLayout) v
                    .findViewById(R.id.catholder);
            imgholder = (RelativeLayout) v
                    .findViewById(R.id.imgholder);

        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v;

        // create a new view
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_menu, parent, false);
        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Section tweet = nodes.get(position);
        holder.lbl_cat.setText(tweet.name);
        holder.simg.setTypeface(util.glyph(context));

        if (tweet.icon != null && !tweet.icon.trim().equalsIgnoreCase("")) {
            holder.imgholder.setVisibility(View.VISIBLE);
            try {
                if (tweet.descr != null && tweet.descr.equalsIgnoreCase("|setting|")) {
                    holder.simg.setText(tweet.icon);
                    holder.img.setVisibility(View.GONE);
                    holder.simg.setVisibility(View.VISIBLE);
                } else {
                    holder.img.setImageDrawable(Drawable.createFromStream(context.getAssets().open(tweet.icon), null));
                    holder.img.setVisibility(View.VISIBLE);
                    holder.simg.setVisibility(View.GONE);
                }
            } catch (Exception e) {
            }

        } else {
            holder.imgholder.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.lbl_cat
                    .getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(2, 2, 10, 2);
            holder.lbl_cat.setLayoutParams(params);
        }

        holder.catholder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                util.handleCallBack(context, appState, tweet);


            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return nodes.size();


    }


    public MenuAdapter(Activity act, List<Section> nz) {
        context = act;
        appState = ((MyApp) act.getApplicationContext());
        nodes = nz;
    }


}