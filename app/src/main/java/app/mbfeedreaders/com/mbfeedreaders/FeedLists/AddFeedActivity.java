package app.mbfeedreaders.com.mbfeedreaders.FeedLists;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.mbfeedreaders.com.mbfeedreaders.R;

public class AddFeedActivity extends AppCompatActivity {

    public String finalfeedaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button saveButton = (Button) findViewById(R.id.addFeedButton);
        final EditText feedAddress = (EditText) findViewById(R.id.addFeedEditText);
        final EditText feedName = (EditText) findViewById(R.id.addFeedNameEditText);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedAddress.getText().length() < 6) {
                    Toast.makeText(getApplicationContext(), "Rss Feed address is too short", Toast.LENGTH_LONG).show();
                    return;
                }

                DBforFeedlist db = new DBforFeedlist(AddFeedActivity.this);
                finalfeedaddress = "http://" + feedAddress.getText().toString() + "/feed/";
                RssFeed feeditem = new RssFeed(feedName.getText().toString(), finalfeedaddress);
                db.addRssFeed(feeditem);
                Toast.makeText(getApplicationContext(), "Rss Feed Added!", Toast.LENGTH_LONG).show();
                feedAddress.setText("");
                feedName.setText("");
            }
        });
    }
}