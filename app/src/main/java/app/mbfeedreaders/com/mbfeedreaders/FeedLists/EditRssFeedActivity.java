package app.mbfeedreaders.com.mbfeedreaders.FeedLists;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import app.mbfeedreaders.com.mbfeedreaders.R;

public class EditRssFeedActivity extends AppCompatActivity {

    List<RssFeed> feeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rss_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        feeds = new DBforFeedlist(this).getRssFeeds();
        //feeds = new DBforFeedlist(this).getRssFeeds();
        RssFeedsAdapter adapter = new RssFeedsAdapter(this, feeds);

        final ListView listView = (ListView) findViewById(R.id.editRssListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditRssFeedActivity.this);
                dialog.setTitle("Remove Feed");
                dialog.setMessage("Are you sure you want to remove this feed?");

                final int positionToRemove = position;

                dialog.setNegativeButton("No", null);
                dialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        RssFeed selectedFeed = feeds.get(positionToRemove);
                        new DBforFeedlist(EditRssFeedActivity.this).deleteRssFeed(selectedFeed);

                        feeds = new DBforFeedlist(EditRssFeedActivity.this).getRssFeeds();
                        RssFeedsAdapter adapterNew = new RssFeedsAdapter(EditRssFeedActivity.this, feeds);

                        listView.setAdapter(adapterNew);
                    }
                });
                dialog.show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}