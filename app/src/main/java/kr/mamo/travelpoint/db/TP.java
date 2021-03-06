package kr.mamo.travelpoint.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.db.table.Travel;
import kr.mamo.travelpoint.db.table.TravelHistory;
import kr.mamo.travelpoint.db.table.TravelPoint;
import kr.mamo.travelpoint.db.table.User;
import kr.mamo.travelpoint.provider.TravelPointProvider;

/**
 * Created by alucard on 2015-07-16.
 */
public class TP {
    public static boolean validateUser(Context context, String email, String password, int type) {
        ContentResolver resolver = context.getContentResolver();
        boolean result = false;
        kr.mamo.travelpoint.db.domain.User user = readUser(context, email);
        if (null != user) {
            if (type == user.getType() && password.equals(user.getPassword())) {
                result = true;
            } else {
                result = false;
            }
        } else {
            result = createUser(context, email, password, type);
            user = readUser(context, email);
        }

        if (result && null != user) {
            user.setSignIn(true);
            updateUser(context, user);
        }
        return result;
    }

    public static kr.mamo.travelpoint.db.domain.User autoLogin(Context context) {
        ArrayList<kr.mamo.travelpoint.db.domain.User> list = readUserList(context);
        for (kr.mamo.travelpoint.db.domain.User user : list) {
            if (user.isSignIn()) {
                return user;
            }
        }
        return null;
    }

    public static void signOut(Context context) {
        ArrayList<kr.mamo.travelpoint.db.domain.User> list = readUserList(context);
        for (kr.mamo.travelpoint.db.domain.User user : list) {
            if (user.isSignIn()) {
                user.setSignIn(false);
                updateUser(context, user);
            }
        }
    }

    public static void createTravelHistory(Context context, kr.mamo.travelpoint.db.domain.TravelPoint travelPoint, String imagePath, double latitude, double longitude, String diary) {
        kr.mamo.travelpoint.db.domain.User user = autoLogin(context);
        createTravelHistory(context, user.getNo(), travelPoint.getTravelNo(), travelPoint.getNo(), imagePath, latitude, longitude, diary);
    }

    // create
    private static boolean createUser(Context context, String email, String password, int type) {
        ContentResolver resolver = context.getContentResolver();

        ContentValues row = new ContentValues();
        row.put(User.Schema.COLUMN.EMAIL.getName(), email);
        row.put(User.Schema.COLUMN.PASSWORD.getName(), password);
        row.put(User.Schema.COLUMN.TYPE.getName(), type);
        return null != resolver.insert(TravelPointProvider.USER_URI, row);
    }
    private static boolean createTravelHistory(Context context, int userNo, int travelNo, int travelPointNo, String imagePath, double latitude, double longitude, String diary) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues row = new ContentValues();
        row.put(TravelHistory.Schema.COLUMN.USER_NO.getName(), userNo);
        row.put(TravelHistory.Schema.COLUMN.TRAVEL_NO.getName(), travelNo);
        row.put(TravelHistory.Schema.COLUMN.TRAVEL_POINT_NO.getName(), travelPointNo);
        row.put(TravelHistory.Schema.COLUMN.IMAGE_PATH.getName(), imagePath);
        row.put(TravelHistory.Schema.COLUMN.LATITUDE.getName(), latitude);
        row.put(TravelHistory.Schema.COLUMN.LONGITUDE.getName(), longitude);
        row.put(TravelHistory.Schema.COLUMN.DIARY.getName(), diary);

        return null != resolver.insert(TravelPointProvider.TRAVEL_HISTORY_URI, row);
    }

    public static int deleteTravelHistory(Context context, int travelHistoryNo) {
        ContentResolver resolver = context.getContentResolver();
        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, TravelHistory.TABLE_NAME + "/" + travelHistoryNo);
        String selection = TravelHistory.Schema.COLUMN.NO.getName() + "=?";
        return resolver.delete(idUri, selection, new String[] {String.valueOf(travelHistoryNo)});
    }
    // read
    private static ArrayList<kr.mamo.travelpoint.db.domain.User> readUserList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        ArrayList<kr.mamo.travelpoint.db.domain.User> list = new ArrayList<kr.mamo.travelpoint.db.domain.User>();
        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, User.TABLE_NAME);
        Cursor cursor = resolver.query(idUri, null, null, null, null);
        while (cursor.moveToNext()) {
            list.add(parseUser(cursor));
        }
        return list;
    }

    public static ArrayList<kr.mamo.travelpoint.db.domain.TravelHistory> readTravelHistoryList(Context context, int travelPointNo) {
        kr.mamo.travelpoint.db.domain.User user = TP.autoLogin(context);
        ContentResolver resolver = context.getContentResolver();
        ArrayList<kr.mamo.travelpoint.db.domain.TravelHistory> list = new ArrayList<kr.mamo.travelpoint.db.domain.TravelHistory>();
        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, TravelHistory.TABLE_NAME + "/" + user.getNo() + "/" + travelPointNo);
        Cursor cursor = resolver.query(idUri, null, null, null, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                list.add(parseTravelHistory(cursor, user.getNo()));
            }
        }
        return list;
    }

    public static kr.mamo.travelpoint.db.domain.TravelHistory readTravelHistory(Context context, int travelHistoryNo) {
        kr.mamo.travelpoint.db.domain.User user = TP.autoLogin(context);

        ContentResolver resolver = context.getContentResolver();
        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, TravelHistory.TABLE_NAME + "/" + travelHistoryNo);
        Cursor cursor = resolver.query(idUri, null, null, null, null);
        if (cursor.moveToNext()) {
            return parseTravelHistory(cursor, user.getNo());
        }
        return null;
    }

    public static ArrayList<kr.mamo.travelpoint.db.domain.TravelPoint> readTravelPointList(Context context, int travelNo) {
        ContentResolver resolver = context.getContentResolver();
        ArrayList<kr.mamo.travelpoint.db.domain.TravelPoint> list = new ArrayList<kr.mamo.travelpoint.db.domain.TravelPoint>();
        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, TravelPoint.TABLE_NAME + "/" + travelNo);
        Cursor cursor = resolver.query(idUri, null, null, null, null);
        while (cursor.moveToNext()) {
            list.add(parseTravelPoint(cursor));
        }
        return list;
    }

    public static kr.mamo.travelpoint.db.domain.Travel readTravel(Context context, int travelNo) {
        ContentResolver resolver = context.getContentResolver();
        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, Travel.TABLE_NAME + "/" + travelNo);
        Cursor cursor = resolver.query(idUri, null, null, null, null);
        while (cursor.moveToNext()) {
            return parseTravel(cursor);
        }
        return null;
    }

    public static ArrayList<kr.mamo.travelpoint.db.domain.Travel> readTravelList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        ArrayList<kr.mamo.travelpoint.db.domain.Travel> list = new ArrayList<kr.mamo.travelpoint.db.domain.Travel>();
        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, Travel.TABLE_NAME);
        Cursor cursor = resolver.query(idUri, null, null, null, null);
        while (cursor.moveToNext()) {
            list.add(parseTravel(cursor));
        }
        return list;
    }

    public static kr.mamo.travelpoint.db.domain.User readUser(Context context, String email) {
        ContentResolver resolver = context.getContentResolver();

        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, User.TABLE_NAME + "/" + email);
        Cursor cursor = resolver.query(idUri, null, null, null, null);
        while (cursor.moveToNext()) {
            return parseUser(cursor);
        }
        return null;
    }

    // uddate
    public static void updateUser(Context context, kr.mamo.travelpoint.db.domain.User user) {
        ContentResolver resolver = context.getContentResolver();

        ContentValues row = new ContentValues();
        row.put(User.Schema.COLUMN.EMAIL.getName(), user.getEmail());
        row.put(User.Schema.COLUMN.PASSWORD.getName(), user.getPassword());
        row.put(User.Schema.COLUMN.TYPE.getName(), user.getType());
        row.put(User.Schema.COLUMN.SIGN_IN.getName(), user.isSignIn() ? 1 : 0);
        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, User.TABLE_NAME + "/" + user.getEmail());
        resolver.update(idUri, row, null, null);

        kr.mamo.travelpoint.db.domain.User user2 = readUser(context, user.getEmail());
    }

    private static kr.mamo.travelpoint.db.domain.User parseUser(Cursor cursor) {
        int no = cursor.getInt(cursor.getColumnIndex(User.Schema.COLUMN.NO.getName()));
        String email = cursor.getString(cursor.getColumnIndex(User.Schema.COLUMN.EMAIL.getName()));
        String pw = cursor.getString(cursor.getColumnIndex(User.Schema.COLUMN.PASSWORD.getName()));
        int type = cursor.getInt(cursor.getColumnIndex(User.Schema.COLUMN.TYPE.getName()));
        boolean signIn = (1 == cursor.getInt(cursor.getColumnIndex(User.Schema.COLUMN.SIGN_IN.getName()))) ? true : false;
        return new kr.mamo.travelpoint.db.domain.User(no, email, pw, type, signIn);
    }

    private static kr.mamo.travelpoint.db.domain.Travel parseTravel(Cursor cursor) {
        int no = cursor.getInt(cursor.getColumnIndex(Travel.Schema.COLUMN.NO.getName()));
        String name = cursor.getString(cursor.getColumnIndex(Travel.Schema.COLUMN.NAME.getName()));
        double latitude = cursor.getDouble(cursor.getColumnIndex(Travel.Schema.COLUMN.LATITUDE.getName()));
        double longitude = cursor.getDouble(cursor.getColumnIndex(Travel.Schema.COLUMN.LONGITUDE.getName()));
        String description = cursor.getString(cursor.getColumnIndex(Travel.Schema.COLUMN.DESCRIPTION.getName()));
        return new kr.mamo.travelpoint.db.domain.Travel(no, name, latitude, longitude, description);
    }

    private static kr.mamo.travelpoint.db.domain.TravelHistory parseTravelHistory(Cursor cursor, int userNo) {
        int no = cursor.getInt(cursor.getColumnIndex(TravelHistory.Schema.COLUMN.NO.getName()));
        int travelNo = cursor.getInt(cursor.getColumnIndex(TravelHistory.Schema.COLUMN.TRAVEL_NO.getName()));
        int travelPointNo = cursor.getInt(cursor.getColumnIndex(TravelHistory.Schema.COLUMN.TRAVEL_POINT_NO.getName()));
        String imagePath = cursor.getString(cursor.getColumnIndex(TravelHistory.Schema.COLUMN.IMAGE_PATH.getName()));
        double latitude = cursor.getDouble(cursor.getColumnIndex(TravelHistory.Schema.COLUMN.LATITUDE.getName()));
        double longitude = cursor.getDouble(cursor.getColumnIndex(TravelHistory.Schema.COLUMN.LONGITUDE.getName()));
        String diary = cursor.getString(cursor.getColumnIndex(TravelHistory.Schema.COLUMN.DIARY.getName()));
        String createDate = cursor.getString(cursor.getColumnIndex(TravelHistory.Schema.COLUMN.CREATE_DATE.getName()));
        return new kr.mamo.travelpoint.db.domain.TravelHistory(no, userNo, travelNo, travelPointNo, imagePath, latitude, longitude, diary, createDate);
    }

    private static kr.mamo.travelpoint.db.domain.TravelPoint parseTravelPoint(Cursor cursor) {
        int no = cursor.getInt(cursor.getColumnIndex(TravelPoint.Schema.COLUMN.NO.getName()));
        int travelNo = cursor.getInt(cursor.getColumnIndex(TravelPoint.Schema.COLUMN.TRAVEL_NO.getName()));
        String name = cursor.getString(cursor.getColumnIndex(TravelPoint.Schema.COLUMN.NAME.getName()));
        double latitude = cursor.getDouble(cursor.getColumnIndex(TravelPoint.Schema.COLUMN.LATITUDE.getName()));
        double longitude = cursor.getDouble(cursor.getColumnIndex(TravelPoint.Schema.COLUMN.LONGITUDE.getName()));
        String description = cursor.getString(cursor.getColumnIndex(TravelPoint.Schema.COLUMN.DESCRIPTION.getName()));
        return new kr.mamo.travelpoint.db.domain.TravelPoint(no, travelNo, name, latitude, longitude, description);
    }
}
