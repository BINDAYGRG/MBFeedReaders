package app.mbfeedreaders.com.mbfeedreaders;

import java.net.MalformedURLException;
import java.net.URL;

import app.mbfeedreaders.com.mbfeedreaders.interfacepackage.Copyable;


/**
 * Created by Binaya Gurung on 10/6/16.
 */

public class RssItem implements Comparable<RssItem>, Copyable<RssItem> {
    public String title;
    public URL link;
    public String description;
    public String content;
    public String pubDate;

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public void setLink(String link) {
        try {
            this.link = new URL(link);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public URL getLink(){
        return this.link;
    }


    public void setDate(String date) {
        this.pubDate = date;
    }


    public String getDate() {
        return this.pubDate;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    // sort by date
    public int compareTo(RssItem another) {
        if (another == null) return 1;
        return another.pubDate.compareTo(pubDate);
    }

    @Override
    public RssItem copy() {
        RssItem copy = createForCopy();
        copyTo(copy);
        return copy;
    }

    @Override
    public RssItem createForCopy() {
        return new RssItem();
    }

    @Override
    public void copyTo(RssItem dest) {
        dest.setTitle(title);
        dest.setDescription(description);
        dest.setLink(link.toExternalForm());
        dest.setDate(pubDate);
        dest.setContent(content);
    }


}