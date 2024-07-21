package app.mbfeedreaders.com.mbfeedreaders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.mbfeedreaders.com.mbfeedreaders.FeedLists.AddFeedActivity;
import app.mbfeedreaders.com.mbfeedreaders.FeedLists.EditRssFeedActivity;
import app.mbfeedreaders.com.mbfeedreaders.FeedLists.RssFeed;
import app.mbfeedreaders.com.mbfeedreaders.OfflineUse.DBforOffline;
import app.mbfeedreaders.com.mbfeedreaders.OfflineUse.OfflineItems;
import app.mbfeedreaders.com.mbfeedreaders.OfflineUse.OfflineRssAdapter;
import app.mbfeedreaders.com.mbfeedreaders.notifications.MyService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<RssItem> rssItems;
    List<RssFeed> rssFeeds;
    List<OfflineItems> feeds;
    int itemscount;
    int retrievedFeedCount;
    private FeedItemAdapter postAdapter;
    private ListView listView;
    private ProgressBar bar;
    private TextView tv1;
    private AlarmManager alarm;
    private PendingIntent pintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       Menu bar1 = (Menu) this.findViewById(R.id.action_delete_posts);
        // bar.setVisibility(View.VISIBLE);
        //bar.setVisibility(View.GONE);

        //bar1.setGroupEnabled(0, true);

        tv1 = (TextView)this.findViewById(R.id.tv1);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent=getIntent();
        if(intent.hasExtra("title")){
         //   titleTextView.setText(intent.getStringExtra("title"));
         //   contentTextView.setText(intent.getStringExtra("summary"));
        }

       startNotifications();

        Intent serviceIntent = new Intent(this, MyService.class);
        pintent = PendingIntent.getService(this, 0, serviceIntent, 0);

        feeds = new DBforOffline(this).GetFeedItems();
        itemscount = feeds.size();
        if(itemscount==0)
        {
            tv1.setVisibility(View.VISIBLE);
        }
        //feeds = new DBforFeedlist(this).getRssFeeds();
        OfflineRssAdapter adapter = new OfflineRssAdapter(this, feeds);

        final ListView listView = (ListView) findViewById(R.id.rssFeedItemListView);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Remove Feed");
                dialog.setMessage("Are you sure you want to remove this Post?");

                final int positionToRemove = position;

                dialog.setNegativeButton("No", null);
                dialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        OfflineItems selectedFeed = feeds.get(positionToRemove);
                        new DBforOffline(MainActivity.this).deleteRssFeed(selectedFeed);

                        feeds = new DBforOffline(MainActivity.this).GetFeedItems();
                        OfflineRssAdapter adapterNew = new OfflineRssAdapter(MainActivity.this, feeds);
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

                feeds = new DBforOffline(MainActivity.this).GetFeedItems();
                Bundle postInfo = new Bundle();
                postInfo.putString("title", selectedFeed.OfflineTitle);
                postInfo.putString("content", selectedFeed.OfflinePostContent);
                Intent postviewIntent = new Intent(MainActivity.this, RssItemViewActivity.class);
                postviewIntent.putExtras(postInfo);
                startActivity(postviewIntent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_sync_posts) {

            Intent intent = new Intent(this, SyncPost.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.menu_my_feeds) {

            Intent intent = new Intent(this, EditRssFeedActivity.class);
            startActivity(intent);
            return true;
        }

        else if (id == R.id.action_delete_posts) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Delete All Posts");
            dialog.setMessage("Are you sure you want to delete all the posts?");
            dialog.setNegativeButton("No", null);
            dialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which){
                    new DBforOffline(MainActivity.this).deleteallposts();
                    feeds = new DBforOffline(MainActivity.this).GetFeedItems();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,"All Posts are deleted successfully", Toast.LENGTH_LONG).show();
                }
            });
            dialog.show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.SyncPosts) {

            Intent intent = new Intent(this, SyncPost.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.AddWebsite) {

            Intent intent = new Intent(this, AddFeedActivity.class);
            startActivity(intent);
            return true;

        }
        if (id == R.id.Allposts) {



            feeds = new DBforOffline(this).GetFeedItems();
            //feeds = new DBforFeedlist(this).getRssFeeds();
            final OfflineRssAdapter adapter = new OfflineRssAdapter(this, feeds);

            final ListView listView = (ListView) findViewById(R.id.rssFeedItemListView);

            listView.setAdapter(adapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("Remove Feed");
                    dialog.setMessage("Are you sure you want to remove this Post?");

                    final int positionToRemove = position;

                    dialog.setNegativeButton("No", null);
                    dialog.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which){
                            OfflineItems selectedFeed = feeds.get(positionToRemove);
                            new DBforOffline(MainActivity.this).deleteRssFeed(selectedFeed);

                            feeds = new DBforOffline(MainActivity.this).GetFeedItems();
                            OfflineRssAdapter adapterNew = new OfflineRssAdapter(MainActivity.this, feeds);

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

                    feeds = new DBforOffline(MainActivity.this).GetFeedItems();
                    Bundle postInfo = new Bundle();
                    postInfo.putString("title", selectedFeed.OfflineTitle);
                    postInfo.putString("content", selectedFeed.OfflinePostContent);
                    Intent postviewIntent = new Intent(MainActivity.this, RssItemViewActivity.class);
                    postviewIntent.putExtras(postInfo);
                    startActivity(postviewIntent);
                }
            });

        }
         else if (id == R.id.my_feeds) {

            Intent intent = new Intent(this, EditRssFeedActivity.class);
            startActivity(intent);
            return true;

        }


        else if (id == R.id.nav_about) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("About Us");
            dialog.setMessage("About Us");

            dialog.setNegativeButton("Ok", null);
            dialog.show();
        }

        else if (id == R.id.nav_exit) {

            finish();
            System.exit(1);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void startNotifications() {

      //  Toast.makeText(this, "Notifications Service Started!!!!!!!!!!", Toast.LENGTH_LONG).show();

        Calendar cal = Calendar.getInstance();

        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start every 30 seconds
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                60 * 1000, pintent);
    }
}
