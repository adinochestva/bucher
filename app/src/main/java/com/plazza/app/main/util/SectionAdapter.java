package com.plazza.app.main.util;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plazza.app.main.MyApp;
import com.plazza.app.main.R;
import com.plazza.app.main.model.Section;

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
                .inflate(R.layout.row_section, parent, false);
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

        if (tweet.icon != null && !tweet.icon.trim().equalsIgnoreCase("")) {
            try {
                holder.img.setImageDrawable(Drawable.createFromStream(context.getAssets().open(tweet.icon), null));
                holder.img.setVisibility(View.VISIBLE);
            } catch (Exception e) {
            }

        } else {
            holder.img.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.lable
                    .getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(5, 5, 5, 5);
            holder.lable.setLayoutParams(params);
        }
        holder.GridItem.setOnClickListener(new OnClickListener() {

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


    public SectionAdapter(Context cnt, MyApp appState, List<Section> nz) {
        this.appState = appState;
        context = cnt;
        nodes = nz;
    }


}
