package app.mbfeedreaders.com.mbfeedreaders.FeedLists;

/**
 * Created by Binaya Gurung on 10/6/16.
 */
public class RssFeed {

    public int id;
    public String rssFeedTitle;
    public String rssFeedAddress;

    public RssFeed(String title, String address) {
        rssFeedTitle = title;
        rssFeedAddress = address;
    }

    public RssFeed (int feedId, String title, String address) {
        id = feedId;
        rssFeedTitle = title;
        rssFeedAddress = address;
    }

}