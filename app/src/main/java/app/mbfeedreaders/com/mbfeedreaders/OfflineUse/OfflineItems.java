package app.mbfeedreaders.com.mbfeedreaders.OfflineUse;

/**
 * Created by binayagurung on 10/13/2016 AD.
 */

public class OfflineItems {
    public int id;
    public String OfflineTitle;
    public String OfflineDescription;
    public String OfflinePostContent;
    public String OfflineDate;

    public OfflineItems(String title, String description, String content, String date) {
        OfflineTitle = title;
        OfflineDescription = description;
        OfflinePostContent = content;
        OfflineDate = date;
    }

    public OfflineItems(int OfflineFeedId, String title, String description, String content, String date) {
        id = OfflineFeedId;
        OfflineTitle = title;
        OfflineDescription = description;
        OfflinePostContent = content;
        OfflineDate = date;
    }
}
