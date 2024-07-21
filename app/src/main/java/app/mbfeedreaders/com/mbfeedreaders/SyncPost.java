package app.mbfeedreaders.com.mbfeedreaders;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.mbfeedreaders.com.mbfeedreaders.FeedLists.DBforFeedlist;
import app.mbfeedreaders.com.mbfeedreaders.FeedLists.RssFeed;
import app.mbfeedreaders.com.mbfeedreaders.OfflineUse.DBforOffline;
import app.mbfeedreaders.com.mbfeedreaders.OfflineUse.OfflineItems;

public class SyncPost extends AppCompatActivity {


    ArrayList<RssItem> rssItems;
    List<RssFeed> rssFeeds;
    List<OfflineItems> feeds;
    int feedCount, feedCountAfter, postCount, itemsCount ;
    int retrievedFeedCount;
    private ProgressBar bar;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_post);

        tv= (TextView)this.findViewById(R.id.tv);
        bar = (ProgressBar) this.findViewById(R.id.progressBar2);
        bar.setVisibility(View.VISIBLE);

        feeds = new DBforOffline(this).GetFeedItems();
        itemsCount = feeds.size();

        rssItems = new ArrayList<RssItem>();
        rssFeeds = new DBforFeedlist(this).getRssFeeds();
        feedCount = rssFeeds.size();
        retrievedFeedCount = 0;

        for (int i = 0; i < rssFeeds.size(); i++) {
            GetFeedItems(rssFeeds.get(i).rssFeedAddress);
        }

    }

    public void GetFeedItems(final String feedAddress) {
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

                                    DBforOffline db = new DBforOffline(SyncPost.this);
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
            postCount = itemsCount - feedCountAfter;
            tv.setText(postCount + " Posts are Fetched!!");
            bar.setVisibility(View.GONE);
            finish();
            // listView.setOnRefresh(this);
           // bar.setVisibility(View.GONE);

            //     listView.setAdapter(new com.feedreaders.appbm.FeedItemAdapter(this, R.layout.activity_main, rssItems));

            Toast.makeText(this, postCount +" Posts are fetched", Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}