package app.mbfeedreaders.com.mbfeedreaders.OfflineUse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;

import app.mbfeedreaders.com.mbfeedreaders.R;

/**
 * Created by binayagurung on 10/14/2016 AD.
 */

public class OfflinePostViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_offlinepostview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        WebView webView = (WebView) findViewById(R.id.OfflinePostView);
        //String url = this.getIntent().getStringExtra("url");

        Bundle bundle = this.getIntent().getExtras();
		/*
		 * If you have content in your rss feed, you can show it directly.
		 * Otherwise, load the content from link
		 *
		 */
        String OfflinePostInfo = bundle.getString("content");
        //webview bug, need to convert this special character
        OfflinePostInfo = "<link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" />" + OfflinePostInfo;

        webView.loadDataWithBaseURL("file:///android_asset/", OfflinePostInfo,  "text/html", "utf-8", null);
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
         String title = bundle.getString("title");
        this.setTitle(title);
    }
    @Override
    public void onBackPressed() {
        finish();
    }

}
