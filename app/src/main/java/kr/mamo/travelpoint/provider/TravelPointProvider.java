package kr.mamo.travelpoint.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import kr.mamo.travelpoint.db.table.DBManager;
import kr.mamo.travelpoint.db.table.TravelPoint;
import kr.mamo.travelpoint.db.table.User;

public class TravelPointProvider extends ContentProvider {
    public static final String  AUTHORITY    = "kr.mamo.travelpoint.travelpointprovider";
    public static final Uri     CONTENT_URI  = Uri.parse("content://" + AUTHORITY);
    public static final Uri     USER_URI  = Uri.withAppendedPath(CONTENT_URI, "User");
    public static final Uri     TRAVEL_URI  = Uri.withAppendedPath(CONTENT_URI, "Travel");
    public static final Uri     TRAVEL_POINT_URI  = Uri.withAppendedPath(CONTENT_URI, "TravelPoint");

    static final int USER = 1;
    static final int USER_EMAIL = 2;
    static final int TRAVEL = 3;
    static final int TRAVEL_POINT = 4;
    static final UriMatcher Matcher;
    static{
        Matcher = new UriMatcher(UriMatcher.NO_MATCH);
        Matcher.addURI(AUTHORITY, "User", USER);
        Matcher.addURI(AUTHORITY, "User/*", USER_EMAIL);
        Matcher.addURI(AUTHORITY, "Travel", TRAVEL);
        Matcher.addURI(AUTHORITY, "TravelPoint/*", TRAVEL_POINT);

    }

    private DBManager dbManager;
    private SQLiteDatabase db;
    public TravelPointProvider() {
    }

    @Override
    public void shutdown() {
        db.close();
        dbManager.close();
        super.shutdown();
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
                long row = db.insert(User.TABLE_NAME, null, values);
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
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder(); // 쿼리 문장 생성
        Map<String, String> sNotesProjectionMap = new HashMap<String, String>();
        switch(Matcher.match(uri)) {
            case USER :
                qb.setTables(User.TABLE_NAME);

                for (User.Schema.COLUMN column : User.Schema.COLUMN.values()) {
                    sNotesProjectionMap.put(column.getName(), column.getName());
                }
                qb.setProjectionMap(sNotesProjectionMap);

                break;
            case USER_EMAIL :
                qb.setTables(User.TABLE_NAME);

                for (User.Schema.COLUMN column : User.Schema.COLUMN.values()) {
                    sNotesProjectionMap.put(column.getName(), column.getName());
                }
                qb.setProjectionMap(sNotesProjectionMap);

                qb.appendWhere(User.Schema.COLUMN.EMAIL.getName() + "='" + uri.getPathSegments().get(1) + "'");
                break;
            case TRAVEL :
                qb.setTables(kr.mamo.travelpoint.db.table.Travel.TABLE_NAME);

                for (kr.mamo.travelpoint.db.table.Travel.Schema.COLUMN column : kr.mamo.travelpoint.db.table.Travel.Schema.COLUMN.values()) {
                    sNotesProjectionMap.put(column.getName(), column.getName());
                }
                qb.setProjectionMap(sNotesProjectionMap);
                break;
            case TRAVEL_POINT :
                qb.setTables(kr.mamo.travelpoint.db.table.TravelPoint.TABLE_NAME);

                for (kr.mamo.travelpoint.db.table.TravelPoint.Schema.COLUMN column : kr.mamo.travelpoint.db.table.TravelPoint.Schema.COLUMN.values()) {
                    sNotesProjectionMap.put(column.getName(), column.getName());
                }
                qb.setProjectionMap(sNotesProjectionMap);
                qb.appendWhere(TravelPoint.Schema.COLUMN.TRAVEL_NO.getName() + "='" + uri.getPathSegments().get(1) + "'");
                break;
        }
        cursor = qb.query(db, null, null, null, null, null, null);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch(Matcher.match(uri)) {
            case USER_EMAIL:
                selection = User.Schema.COLUMN.EMAIL.getName() + "='" + uri.getPathSegments().get(1) +"'";
                return db.update(User.TABLE_NAME, values, selection, selectionArgs);
        }
        return 0;
    }
}