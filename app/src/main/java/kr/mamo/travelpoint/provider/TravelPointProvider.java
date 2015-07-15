package kr.mamo.travelpoint.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.db.DBManager;
import kr.mamo.travelpoint.db.User;

public class TravelPointProvider extends ContentProvider {
    public static final String  AUTHORITY    = "kr.mamo.travelpoint.travelpointprovider";
    public static final Uri     CONTENT_URI  = Uri.parse("content://" + AUTHORITY);
    public static final Uri     USER_URI  = Uri.withAppendedPath(CONTENT_URI, "User");

    static final int USER = 1;
    static final int USER_EMAIL = 2;
    static final UriMatcher Matcher;
    static{
        Matcher = new UriMatcher(UriMatcher.NO_MATCH);
        Matcher.addURI(AUTHORITY, "User", USER);
        Matcher.addURI(AUTHORITY, "User/*", USER_EMAIL);

    }

    private DBManager dbManager;
    private SQLiteDatabase db;
    public TravelPointProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        switch(Matcher.match(uri)) {
            case USER_EMAIL :
                return "oneuser";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch(Matcher.match(uri)) {
            case USER:
                long row = db.insert("User", null, values);
                Log.i(Constants.LOGCAT_TAGNAME, "row : " + row);
                if (row > 0) {
                    Uri notiuri = ContentUris.withAppendedId(USER_URI, row);
                    getContext().getContentResolver().notifyChange(notiuri, null);
                    return notiuri;
                }
                break;
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        dbManager = new DBManager(getContext());
        db = dbManager.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        Log.i(Constants.LOGCAT_TAGNAME, "uri : " + uri.toString());
        Log.i(Constants.LOGCAT_TAGNAME, "match : " + Matcher.match(uri));
        switch(Matcher.match(uri)) {
            case USER_EMAIL :
                String[] projection2 = {User.Schema.COLUMN.NO.getName(), User.Schema.COLUMN.EMAIL.getName()};
                SQLiteQueryBuilder qb = new SQLiteQueryBuilder(); // 쿼리 문장 생성
                qb.setTables(User.TABLE_NAME);

                Map<String, String>  sNotesProjectionMap = new HashMap<String, String>();
                sNotesProjectionMap.put(User.Schema.COLUMN.NO.getName(), User.Schema.COLUMN.NO.getName());
                sNotesProjectionMap.put(User.Schema.COLUMN.EMAIL.getName(), User.Schema.COLUMN.EMAIL.getName());
                qb.setProjectionMap(sNotesProjectionMap);
                qb.appendWhere("email='" + uri.getPathSegments().get(1) + "'");
                cursor = qb.query(db, null, null, null, null, null, null);
                Log.i(Constants.LOGCAT_TAGNAME, "test 15");
                break;
        }

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
