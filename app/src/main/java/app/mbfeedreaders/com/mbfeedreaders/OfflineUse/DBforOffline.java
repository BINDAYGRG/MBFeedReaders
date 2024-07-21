package app.mbfeedreaders.com.mbfeedreaders.OfflineUse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binayagurung on 10/13/2016 AD.
 */

public class DBforOffline extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "OfflineDatabse";

    private static final String TABLE_FEEDS2 = "OfflineTable";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "description";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE = "pubDate";

    public DBforOffline(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RSS_TABLE = "CREATE TABLE " + TABLE_FEEDS2 + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_DESC
                + " TEXT," + KEY_CONTENT + " TEXT," + KEY_DATE
                + " TEXT" + ")";
        db.execSQL(CREATE_RSS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDS2);

        // Create tables again
        onCreate(db);
    }

    public void AddFeedItem(OfflineItems feed){

      //  if(feed.OfflineTitle == KEY_TITLE && feed.OfflineDescription == KEY_TI) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, feed.OfflineTitle);
            values.put(KEY_DESC, feed.OfflineDescription);
            values.put(KEY_CONTENT, feed.OfflinePostContent);
            values.put(KEY_DATE, feed.OfflineDate);
            db.delete(TABLE_FEEDS2, KEY_TITLE + " = ?", new String[] {String.valueOf(feed.OfflineTitle)});
            db.insert(TABLE_FEEDS2, null, values);
        db.close();
    }

    public List<OfflineItems> GetFeedItems() {
        List<OfflineItems> results = new ArrayList<OfflineItems>();

        String selectQuery = "SELECT DISTINCT * FROM " + TABLE_FEEDS2 + " ORDER BY pubDate DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OfflineItems FeedItems = new OfflineItems(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                results.add(FeedItems);
            } while (cursor.moveToNext());
        }

        db.close();
        return results;
    }

    public void deleteRssFeed (OfflineItems feed){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FEEDS2, KEY_ID + " = ?", new String[] {String.valueOf(feed.id)});
        db.close();
    }

    public void deleteallposts (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FEEDS2, null, null);
        db.close();
    }

}
