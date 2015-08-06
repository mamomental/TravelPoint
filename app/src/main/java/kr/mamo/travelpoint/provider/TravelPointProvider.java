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
import kr.mamo.travelpoint.db.table.DBManager;
import kr.mamo.travelpoint.db.table.Travel;
import kr.mamo.travelpoint.db.table.TravelHistory;
import kr.mamo.travelpoint.db.table.TravelPoint;
import kr.mamo.travelpoint.db.table.User;

public class TravelPointProvider extends ContentProvider {
    public static final String  AUTHORITY    = "kr.mamo.travelpoint.travelpointprovider";
    public static final Uri     CONTENT_URI  = Uri.parse("content://" + AUTHORITY);
    public static final Uri     USER_URI  = Uri.withAppendedPath(CONTENT_URI, "User");
    public static final Uri     TRAVEL_URI  = Uri.withAppendedPath(CONTENT_URI, "Travel");
    public static final Uri     TRAVEL_POINT_URI  = Uri.withAppendedPath(CONTENT_URI, "TravelPoint");
    public static final Uri     TRAVEL_HISTORY_URI  = Uri.withAppendedPath(CONTENT_URI, "TravelHistory");

    static final int USER_LIST = 1;
    static final int USER_BY_EMAIL = 2;
    static final int TRAVEL_LIST = 3;
    static final int TRAVEL_BY_NO = 4;
    static final int TRAVEL_POINT_BY_TRAVELNO = 5;
    static final int TRAVEL_HISTORY = 6;
    static final int TRAVEL_HISTORY_LIST = 7;
    static final int TRAVEL_HISTORY_BY_NO = 8;
    static final UriMatcher Matcher;
    static{
        Matcher = new UriMatcher(UriMatcher.NO_MATCH);
        Matcher.addURI(AUTHORITY, "User", USER_LIST);
        Matcher.addURI(AUTHORITY, "User/*", USER_BY_EMAIL);
        Matcher.addURI(AUTHORITY, "Travel", TRAVEL_LIST);
        Matcher.addURI(AUTHORITY, "Travel/*", TRAVEL_BY_NO);
        Matcher.addURI(AUTHORITY, "TravelPoint/*", TRAVEL_POINT_BY_TRAVELNO);
        Matcher.addURI(AUTHORITY, "TravelHistory", TRAVEL_HISTORY);
        Matcher.addURI(AUTHORITY, "TravelHistory/*", TRAVEL_HISTORY_BY_NO);
        Matcher.addURI(AUTHORITY, "TravelHistory/*/*", TRAVEL_HISTORY_LIST);
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
        switch (Matcher.match(uri)) {
            case TRAVEL_HISTORY_BY_NO:
                int row = db.delete(TravelHistory.TABLE_NAME, selection, selectionArgs);
                return row;
            default:
                break;
        }
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        switch(Matcher.match(uri)) {
            case USER_BY_EMAIL:
                return "oneuser";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long row = 0;
        Uri notiuri = null;
        switch(Matcher.match(uri)) {
            case USER_LIST:
                row = db.insert(User.TABLE_NAME, null, values);
                if (row > 0) {
                    notiuri = ContentUris.withAppendedId(USER_URI, row);
                }
                break;
            case TRAVEL_HISTORY:
                row = db.insert(TravelHistory.TABLE_NAME, null, values);
                if (row > 0) {
                    notiuri = ContentUris.withAppendedId(TRAVEL_HISTORY_URI, row);
                }
                break;
        }

        if (null != notiuri) {
            getContext().getContentResolver().notifyChange(notiuri, null);
            return notiuri;
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
        String orderby = null;

        switch(Matcher.match(uri)) {
            case USER_LIST:
                qb.setTables(User.TABLE_NAME);

                for (User.Schema.COLUMN column : User.Schema.COLUMN.values()) {
                    sNotesProjectionMap.put(column.getName(), column.getName());
                }
                qb.setProjectionMap(sNotesProjectionMap);

                break;
            case USER_BY_EMAIL:
                qb.setTables(User.TABLE_NAME);

                for (User.Schema.COLUMN column : User.Schema.COLUMN.values()) {
                    sNotesProjectionMap.put(column.getName(), column.getName());
                }
                qb.setProjectionMap(sNotesProjectionMap);

                qb.appendWhere(User.Schema.COLUMN.EMAIL.getName() + "='" + uri.getPathSegments().get(1) + "'");
                break;
            case TRAVEL_LIST:
                qb.setTables(kr.mamo.travelpoint.db.table.Travel.TABLE_NAME);

                for (kr.mamo.travelpoint.db.table.Travel.Schema.COLUMN column : kr.mamo.travelpoint.db.table.Travel.Schema.COLUMN.values()) {
                    sNotesProjectionMap.put(column.getName(), column.getName());
                }
                qb.setProjectionMap(sNotesProjectionMap);
                break;
            case TRAVEL_BY_NO:
                qb.setTables(kr.mamo.travelpoint.db.table.Travel.TABLE_NAME);

                for (kr.mamo.travelpoint.db.table.Travel.Schema.COLUMN column : kr.mamo.travelpoint.db.table.Travel.Schema.COLUMN.values()) {
                    sNotesProjectionMap.put(column.getName(), column.getName());
                }
                qb.setProjectionMap(sNotesProjectionMap);
                qb.appendWhere(Travel.Schema.COLUMN.NO.getName() + "='" + uri.getPathSegments().get(1) + "'");
                break;
            case TRAVEL_POINT_BY_TRAVELNO:
                qb.setTables(kr.mamo.travelpoint.db.table.TravelPoint.TABLE_NAME);

                for (kr.mamo.travelpoint.db.table.TravelPoint.Schema.COLUMN column : kr.mamo.travelpoint.db.table.TravelPoint.Schema.COLUMN.values()) {
                    sNotesProjectionMap.put(column.getName(), column.getName());
                }
                qb.setProjectionMap(sNotesProjectionMap);
                qb.appendWhere(TravelPoint.Schema.COLUMN.TRAVEL_NO.getName() + "='" + uri.getPathSegments().get(1) + "'");
                break;
            case TRAVEL_HISTORY_LIST:
                qb.setTables(kr.mamo.travelpoint.db.table.TravelHistory.TABLE_NAME);

                for (kr.mamo.travelpoint.db.table.TravelHistory.Schema.COLUMN column : kr.mamo.travelpoint.db.table.TravelHistory.Schema.COLUMN.values()) {
                    sNotesProjectionMap.put(column.getName(), column.getName());
                }
                qb.setProjectionMap(sNotesProjectionMap);
                qb.appendWhere(TravelHistory.Schema.COLUMN.USER_NO.getName() + "='" + uri.getPathSegments().get(1) + "' AND " + TravelHistory.Schema.COLUMN.TRAVEL_POINT_NO.getName() + "='" + uri.getPathSegments().get(2) + "'");
                orderby = TravelHistory.Schema.COLUMN.NO + " DESC";
                break;
            case TRAVEL_HISTORY_BY_NO:
                qb.setTables(kr.mamo.travelpoint.db.table.TravelHistory.TABLE_NAME);

                for (kr.mamo.travelpoint.db.table.TravelHistory.Schema.COLUMN column : kr.mamo.travelpoint.db.table.TravelHistory.Schema.COLUMN.values()) {
                    sNotesProjectionMap.put(column.getName(), column.getName());
                }
                qb.setProjectionMap(sNotesProjectionMap);
                qb.appendWhere(TravelHistory.Schema.COLUMN.NO.getName() + "='" + uri.getPathSegments().get(1) + "'");
                break;
        }
        cursor = qb.query(db, null, null, null, null, null, orderby);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch(Matcher.match(uri)) {
            case USER_BY_EMAIL:
                selection = User.Schema.COLUMN.EMAIL.getName() + "='" + uri.getPathSegments().get(1) +"'";
                return db.update(User.TABLE_NAME, values, selection, selectionArgs);
        }
        return 0;
    }
}