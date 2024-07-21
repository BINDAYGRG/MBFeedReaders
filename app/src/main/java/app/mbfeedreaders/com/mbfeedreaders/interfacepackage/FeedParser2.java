package app.mbfeedreaders.com.mbfeedreaders.interfacepackage;

import java.util.List;

import app.mbfeedreaders.com.mbfeedreaders.RssItem;

/**
 * Created by binayagurung on 10/12/2016 AD.
 */

public interface FeedParser2 {
    List<RssItem> parse();
}