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
import kr.mamo.travelpoint.db.table.TravelPoint;
import kr.mamo.travelpoint.db.table.User;
import kr.mamo.travelpoint.provider.TravelPointProvider;

/**
 * Created by alucard on 2015-07-16.
 */
public class TP {
    public static boolean validateUser(Context context, String email, String password) {
        ContentResolver resolver = context.getContentResolver();
        boolean result = false;
        kr.mamo.travelpoint.db.domain.User user = readUser(context, email);
        if (null != user) {
            if (password.equals(user.getPassword())) {
                result = true;
            }
        } else {
            result = createUser(context, email, password);
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

    public static void signIn(Context context, String email, String password) {
        createUser(context, email, password);


//        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
//        prefs.putString(Constants.Preference.Account.email, email);
//        prefs.commit();
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
    // create
    private static boolean createUser(Context context, String email, String password) {
        ContentResolver resolver = context.getContentResolver();

        ContentValues row = new ContentValues();
        row.put(User.Schema.COLUMN.EMAIL.getName(), email);
        row.put(User.Schema.COLUMN.PASSWORD.getName(), password);
        return null != resolver.insert(TravelPointProvider.USER_URI, row);
    }
    // read
    private static ArrayList<kr.mamo.travelpoint.db.domain.User> readUserList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        ArrayList<kr.mamo.travelpoint.db.domain.User> list = new ArrayList<kr.mamo.travelpoint.db.domain.User>();
        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, User.TABLE_NAME);
        Cursor cursor = resolver.query(idUri, null, null, null, null);
        while (cursor.moveToNext()) {
            int no = cursor.getInt(cursor.getColumnIndex(User.Schema.COLUMN.NO.getName()));
            String email = cursor.getString(cursor.getColumnIndex(User.Schema.COLUMN.EMAIL.getName()));
            String pw = cursor.getString(cursor.getColumnIndex(User.Schema.COLUMN.PASSWORD.getName()));
            boolean signIn = (1 == cursor.getInt(cursor.getColumnIndex(User.Schema.COLUMN.SIGN_IN.getName()))) ? true : false;
            list.add(new kr.mamo.travelpoint.db.domain.User(no, email, pw, signIn));
        }
        return list;
    }

    public static ArrayList<kr.mamo.travelpoint.db.domain.TravelPoint> readTravelPointList(Context context, int travelNo) {
        Log.i(Constants.LOGCAT_TAGNAME, "1");
        ContentResolver resolver = context.getContentResolver();
        Log.i(Constants.LOGCAT_TAGNAME, "2");
        ArrayList<kr.mamo.travelpoint.db.domain.TravelPoint> list = new ArrayList<kr.mamo.travelpoint.db.domain.TravelPoint>();
        Log.i(Constants.LOGCAT_TAGNAME, "3");
        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, TravelPoint.TABLE_NAME + "/" + travelNo);
        Log.i(Constants.LOGCAT_TAGNAME, "4");
        Cursor cursor = resolver.query(idUri, null, null, null, null);
        Log.i(Constants.LOGCAT_TAGNAME, "5");
        while (cursor.moveToNext()) {
            int no = cursor.getInt(cursor.getColumnIndex(TravelPoint.Schema.COLUMN.NO.getName()));
            String name = cursor.getString(cursor.getColumnIndex(TravelPoint.Schema.COLUMN.NAME.getName()));
            list.add(new kr.mamo.travelpoint.db.domain.TravelPoint(no, name));
        }
        Log.i(Constants.LOGCAT_TAGNAME, "6");
        return list;
    }

    public static ArrayList<kr.mamo.travelpoint.db.domain.Travel> readTravelList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        ArrayList<kr.mamo.travelpoint.db.domain.Travel> list = new ArrayList<kr.mamo.travelpoint.db.domain.Travel>();
        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, Travel.TABLE_NAME);
        Cursor cursor = resolver.query(idUri, null, null, null, null);
        while (cursor.moveToNext()) {
            int no = cursor.getInt(cursor.getColumnIndex(Travel.Schema.COLUMN.NO.getName()));
            String name = cursor.getString(cursor.getColumnIndex(Travel.Schema.COLUMN.NAME.getName()));
            String description = cursor.getString(cursor.getColumnIndex(Travel.Schema.COLUMN.DESCRIPTION.getName()));
            list.add(new kr.mamo.travelpoint.db.domain.Travel(no, name, description));
        }
        return list;
    }

    private static kr.mamo.travelpoint.db.domain.User readUser(Context context, String email) {
        ContentResolver resolver = context.getContentResolver();

        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, User.TABLE_NAME + "/" + email);
        Cursor cursor = resolver.query(idUri, null, null, null, null);
        while (cursor.moveToNext()) {
            int no = cursor.getInt(cursor.getColumnIndex(User.Schema.COLUMN.NO.getName()));
            String pw = cursor.getString(cursor.getColumnIndex(User.Schema.COLUMN.PASSWORD.getName()));
            boolean signIn = (1 == cursor.getInt(cursor.getColumnIndex(User.Schema.COLUMN.SIGN_IN.getName()))) ? true : false;
            return new kr.mamo.travelpoint.db.domain.User(no, email, pw, signIn);
        }
        return null;
    }

    // uddate
    private static void updateUser(Context context, kr.mamo.travelpoint.db.domain.User user) {
        ContentResolver resolver = context.getContentResolver();

        ContentValues row = new ContentValues();
        row.put(User.Schema.COLUMN.EMAIL.getName(), user.getEmail());
        row.put(User.Schema.COLUMN.PASSWORD.getName(), user.getPassword());
        row.put(User.Schema.COLUMN.SIGN_IN.getName(), user.isSignIn() ? 1 : 0);
        Uri idUri = Uri.withAppendedPath(TravelPointProvider.CONTENT_URI, User.TABLE_NAME + "/" + user.getEmail());
        resolver.update(idUri, row, null, null);
    }
}
