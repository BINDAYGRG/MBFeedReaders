package app.mbfeedreaders.com.mbfeedreaders;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Binaya Gurung on 10/6/16.
 */
public class FeedItemAdapter extends ArrayAdapter<RssItem> {

    public FeedItemAdapter(Context context, int activity_main, ArrayList<RssItem> items){
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RssItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rss_feed_item_row, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.rssFeedItemTitleTextView);
        TextView description = (TextView) convertView.findViewById(R.id.rssFeedItemDescriptionTextView);
        TextView largeLetter = (TextView) convertView.findViewById(R.id.rssFeedItemLetterTextView);
        TextView date = (TextView) convertView.findViewById(R.id.rssFeedItemDateTextView);

        title.setText(item.getDate());
        date.setText(item.getDate());
        description.setText(Html.fromHtml(item.getDescription()));
        largeLetter.setText(item.getTitle().substring(0, 1));
        return convertView;
    }

}
