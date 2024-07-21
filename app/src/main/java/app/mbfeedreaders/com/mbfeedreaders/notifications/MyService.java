package app.mbfeedreaders.com.mbfeedreaders.notifications;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.mbfeedreaders.com.mbfeedreaders.FeedItemAdapter;
import app.mbfeedreaders.com.mbfeedreaders.FeedLists.DBforFeedlist;
import app.mbfeedreaders.com.mbfeedreaders.FeedLists.RssFeed;
import app.mbfeedreaders.com.mbfeedreaders.MainActivity;
import app.mbfeedreaders.com.mbfeedreaders.OfflineUse.DBforOffline;
import app.mbfeedreaders.com.mbfeedreaders.OfflineUse.OfflineItems;
import app.mbfeedreaders.com.mbfeedreaders.R;
import app.mbfeedreaders.com.mbfeedreaders.Rss2Parser;
import app.mbfeedreaders.com.mbfeedreaders.RssItem;
import app.mbfeedreaders.com.mbfeedreaders.Rss2ParserCallback;

public class MyService extends IntentService {


    ArrayList<RssItem> rssItems;
    List<RssFeed> rssFeeds;
    List<OfflineItems> feeds;
    int feedCount;
    int retrievedFeedCount;
    private FeedItemAdapter postAdapter;
    private ListView listView;


    public MyService() {
        super(null);

    }

        @Override
    protected void onHandleIntent(Intent intent) {

            Toast.makeText(this, "Latest Posts are fetched", Toast.LENGTH_LONG).show();
        rssItems = new ArrayList<RssItem>();
        rssFeeds = new DBforFeedlist(this).getRssFeeds();
        feedCount = rssFeeds.size();
        retrievedFeedCount = 0;

        for (int i = 0; i < rssFeeds.size(); i++) {
            GetFeedItems(rssFeeds.get(i).rssFeedAddress);
        }}

    public void GetFeedItems(String feedAddress) {
        try {
            Rss2Parser parser = new Rss2Parser(feedAddress,
                    new Rss2ParserCallback() {
                        @Override
                        public void onFeedParsed(List<RssItem> items) {
                            for (int i = 0; i < 10
                                    ; i++) {
                                RssItem item = new RssItem();
                                item.setTitle(items.get(i).getTitle());
                                item.setDescription(items.get(i).getDescription());
                                item.setLink(String.valueOf(items.get(i).getLink()));
                                item.setContent(items.get(i).getContent());
                                item.setContent(items.get(i).getDate());
                                Log.d("ITEMS RECEIVED", items.get(i).getTitle());
                                rssItems.add(item);
                                DBforOffline db = new DBforOffline(MyService.this);
                                OfflineItems feeditem = new OfflineItems(items.get(i).getTitle(), items.get(i).getDescription(), items.get(i).getContent(), items.get(i).getDate());
                                db.AddFeedItem(feeditem);
                            }
                            PopulateListView();
                        }

                        @Override
                        public void onError(Exception ex) {
                            PopulateListView();
                        }
                    });
            parser.parseAsync();
        } catch (Exception e) {
            Toast.makeText(this, "Address not found: " + feedAddress, Toast.LENGTH_LONG).show();
            PopulateListView();
        }
    }

    public void PopulateListView() {
        retrievedFeedCount++;
        if (retrievedFeedCount == feedCount) {
            Log.d("FEEDS RETRIEVED", "got all feeds");
            // listView.setOnRefresh(this);
            // bar.setVisibility(View.GONE);

            //     listView.setAdapter(new com.feedreaders.appbm.FeedItemAdapter(this, R.layout.activity_main, rssItems));
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            Toast.makeText(this, "Latest Posts are fetched", Toast.LENGTH_LONG).show();

        }


    }




    private void showNotification(String title, String summary, String feedTitle) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(feedTitle).setContentText(title)
                .setAutoCancel(true);
        // Creates an Intent that shows the title and a description of the feed
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("summary", summary);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int mId = 1;
        mNotificationManager.notify(mId, mBuilder.build());
    }

}
