package com.plazza.app.main.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plazza.app.main.MainActivity;
import com.plazza.app.main.MyApp;
import com.plazza.app.main.R;
import com.plazza.app.main.model.FeedItem;

import java.util.List;

public class RssAdapter extends RecyclerView.Adapter<RssAdapter.ViewHolder> {
    private Context context;
    List<FeedItem> nodes;
    public MyApp appState;
    Boolean openInBrowser;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView descr;
        public CardView GridItem;

        public ViewHolder(View v) {
            super(v);

            title = (TextView) v.findViewById(R.id.title);
            descr = (TextView) v.findViewById(R.id.descr);
            GridItem = (CardView) v.findViewById(R.id.GridItem);

        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RssAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View v;

        // create a new view
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rss, parent, false);
        // set the view's size, margins, paddings and layout parameters


        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final FeedItem tweet = nodes.get(position);
        holder.title.setText(tweet.getTitle());
        holder.descr.setText(tweet.getDescription());


        holder.GridItem.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String url = "";
                if (!tweet.getLink().trim().startsWith("http://"))
                    url = "http://";
                url = url + tweet.getLink().trim();


                if (openInBrowser) {
                    Intent browse = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(url));
                    context.startActivity(browse);
                } else {
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra("id", -1);
                    i.putExtra("url", url);
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


    public RssAdapter(Context cnt, MyApp appState, List<FeedItem> nz, Boolean openInBrowser) {
        this.appState = appState;
        this.openInBrowser = openInBrowser;
        context = cnt;
        nodes = nz;
    }


}
