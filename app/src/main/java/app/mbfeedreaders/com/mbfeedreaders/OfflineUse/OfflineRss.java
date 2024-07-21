package app.mbfeedreaders.com.mbfeedreaders.OfflineUse;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import app.mbfeedreaders.com.mbfeedreaders.R;
import app.mbfeedreaders.com.mbfeedreaders.RssItemViewActivity;

public class OfflineRss extends AppCompatActivity {

    List<OfflineItems> feeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rss_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        feeds = new DBforOffline(this).GetFeedItems();
        //feeds = new DBforFeedlist(this).getRssFeeds();
        OfflineRssAdapter adapter = new OfflineRssAdapter(this, feeds);

        final ListView listView = (ListView) findViewById(R.id.editRssListView);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(OfflineRss.this);
                dialog.setTitle("Remove Feed");
                dialog.setMessage("Are you sure you want to remove this Post?");

                final int positionToRemove = position;

                dialog.setNegativeButton("No", null);
                dialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        OfflineItems selectedFeed = feeds.get(positionToRemove);
                        new DBforOffline(OfflineRss.this).deleteRssFeed(selectedFeed);

                        feeds = new DBforOffline(OfflineRss.this).GetFeedItems();
                        OfflineRssAdapter adapterNew = new OfflineRssAdapter(OfflineRss.this, feeds);

                        listView.setAdapter(adapterNew);
                    }
                });
                dialog.show();
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final int offlinepostcontent = position;

                OfflineItems selectedFeed = feeds.get(offlinepostcontent);

                feeds = new DBforOffline(OfflineRss.this).GetFeedItems();
                Bundle postInfo = new Bundle();
                postInfo.putString("title", selectedFeed.OfflineTitle);
                postInfo.putString("content", selectedFeed.OfflinePostContent);
                Intent postviewIntent = new Intent(OfflineRss.this, RssItemViewActivity.class);
                postviewIntent.putExtras(postInfo);
                startActivity(postviewIntent);


            }
        });

    }
    @Override
    public void onBackPressed() {
        finish();
    }
}