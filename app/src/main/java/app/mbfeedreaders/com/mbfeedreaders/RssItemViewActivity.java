package app.mbfeedreaders.com.mbfeedreaders;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class RssItemViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss_item_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        WebView webView = (WebView) findViewById(R.id.rssViewItemWebView);
        //String url = this.getIntent().getStringExtra("url");

        Bundle bundle = this.getIntent().getExtras();
		/*
		 * If you have content in your rss feed, you can show it directly.
		 * Otherwise, load the content from link
		 *
		 */
        String postContent = bundle.getString("content");
        //webview bug, need to convert this special character
        postContent = "<link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\" />" + postContent;

        /* postContent = postContent.replace("%", "%25");
        postContent = postContent.replace("'", "%27");
        postContent = postContent.replace("#", "%23");
        */

        webView.loadDataWithBaseURL("file:///android_asset/", postContent,  "text/html", "utf-8", null);
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //webView.loadData(postContent, "text/html; charset=utf-8", "utf-8");
       // webView.loadUrl(bundle.getString("link"));for website don't provide content in rss, using this to load the website link
        String title = bundle.getString("title");
        this.setTitle(title);
    }
        /*
        webView.loadUrl(url);
        this.setTitle("You Are Viewing a RSS Item");*/

    @Override
    public void onBackPressed() {
        finish();
    }

}
