package app.mbfeedreaders.com.mbfeedreaders.OfflineUse;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.mbfeedreaders.com.mbfeedreaders.R;

/**
 * Created by binayagurung on 10/13/2016 AD.
 */
public class OfflineRssAdapter extends ArrayAdapter<OfflineItems> {

    public OfflineRssAdapter (Context context, List<OfflineItems> feeds){
        super(context, 0, feeds);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OfflineItems feed = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rss_feed_item_row, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.rssFeedItemTitleTextView);
        TextView description = (TextView) convertView.findViewById(R.id.rssFeedItemDescriptionTextView);
        TextView Letter = (TextView) convertView.findViewById(R.id.rssFeedItemLetterTextView);
        TextView date1 = (TextView) convertView.findViewById(R.id.rssFeedItemDateTextView);


        String dateString = feed.OfflineDate;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(dateString);
            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
            java.sql.Timestamp timeStampNow = new Timestamp((new java.util.Date()).getTime());

            long secondDiff = timeStampNow.getTime() / 1000 - timeStampDate.getTime() / 1000;
            int minuteDiff = (int) (secondDiff / 60);
            int hourDiff = (int) (secondDiff / 3600);
            int dayDiff = daysBetween(date, new Date()) - 1;
            if (dayDiff > 0) {
                date1.setText(("Posted " + dayDiff + " days ago."));
            } else if (hourDiff > 0) {
                date1.setText(("Posted " + hourDiff + " hours ago."));
            } else if (minuteDiff > 0) {
                date1.setText(("Posted " + minuteDiff + " minutes ago."));
            } else if (secondDiff > 0) {
                date1.setText(("Posted " + secondDiff + " seconds ago."));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        title.setText(feed.OfflineTitle);
        description.setText(Html.fromHtml(feed.OfflineDescription));
        Letter.setText(feed.OfflineTitle);

        return convertView;
    }
    public static int daysBetween(Date startDate, Date endDate) {
        int daysBetween = 0;
        while (startDate.before(endDate)) {
            startDate.setTime(startDate.getTime() + 86400000);
            daysBetween++;
        }
        return daysBetween;
    }


}
