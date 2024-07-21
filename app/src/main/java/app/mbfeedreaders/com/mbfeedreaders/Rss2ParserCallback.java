package app.mbfeedreaders.com.mbfeedreaders;

import java.util.List;

/**
 * Created by binayagurung on 10/12/2016 AD.
 */

public abstract class Rss2ParserCallback {

    public abstract void onFeedParsed(List<RssItem> items);

    public abstract void onError(Exception ex);
}