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
import kr.mamo.travelpoint.constant.ConstantsDB;
import kr.mamo.travelpoint.db.DBManager;

public class TravelPointProvider extends ContentProvider {
    public static final String  AUTHORITY    = "kr.mamo.travelpoint.travelpointprovider";
    public static final Uri     CONTENT_URI  = Uri.parse("content://" + AUTHORITY);
//    public static final Uri     USER_URI  = Uri.withAppendedPath(CONTENT_URI, "User");
    public static final Uri     USER_URI  = Uri.parse("content://" + AUTHORITY + "User");

    static final int USER_EMAIL = 1;
    static final UriMatcher Matcher;
    static{
        Matcher = new UriMatcher(UriMatcher.NO_MATCH);
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
        Log.i(Constants.LOGCAT_TAGNAME, "insert : " + uri.toString());
        if (uri.toString().equals(TravelPointProvider.USER_URI.toString())) {
            Log.i(Constants.LOGCAT_TAGNAME, "uri is user " + values.get("email"));

            long row = db.insert("User", null, values);
            if (row > 0) {
                Uri notiuri = ContentUris.withAppendedId(USER_URI, row);
                getContext().getContentResolver().notifyChange(notiuri, null);
                return notiuri;
            }
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
                String[] projection2 = {"_id", "email"};
                Log.i(Constants.LOGCAT_TAGNAME, "test 11");
                SQLiteQueryBuilder qb = new SQLiteQueryBuilder(); // 쿼리 문장 생성
                Log.i(Constants.LOGCAT_TAGNAME, "test 12");
                qb.setTables(ConstantsDB.ConstantsTableUser.TABLE_NAME);

                Map<String, String>  sNotesProjectionMap = new HashMap<String, String>();
                sNotesProjectionMap.put("_id", "_id");
                sNotesProjectionMap.put("email", "email");
                qb.setProjectionMap(sNotesProjectionMap);
                Log.i(Constants.LOGCAT_TAGNAME, "test 13 : " + uri.getPathSegments().get(1));
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
