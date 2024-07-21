package app.mbfeedreaders.com.mbfeedreaders.FeedLists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.mbfeedreaders.com.mbfeedreaders.R;

/**
 * Created by binayagurung on 10/6/16.
 */
public class RssFeedsAdapter extends ArrayAdapter<RssFeed> {

    public RssFeedsAdapter (Context context, List<RssFeed> feeds){
        super(context, 0, feeds);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RssFeed feed = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rss_edit_feed_item_row, parent, false);
        }

        TextView feedAddress = (TextView) convertView.findViewById(R.id.rssEditFeedAddressTextView);
        TextView feedTitle = (TextView) convertView.findViewById(R.id.rssEditFeedTitleTextView);

        feedAddress.setText(feed.rssFeedAddress);
        feedTitle.setText(feed.rssFeedTitle);
        return convertView;
    }

}
