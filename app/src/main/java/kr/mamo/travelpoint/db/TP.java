package kr.mamo.travelpoint.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import kr.mamo.travelpoint.db.table.User;
import kr.mamo.travelpoint.provider.TravelPointProvider;

/**
 * Created by alucard on 2015-07-16.
 */
public class TP {
    public static boolean validateUser(Context context, String email, String password) {
        ContentResolver resolver = context.getContentResolver();
        kr.mamo.travelpoint.db.domain.User user = readUser(context, email);
        if (null != user) {
            if (password.equals(user.getPassword())) {
                return true;
            }
        } else {
            boolean result = createUser(context, email, password);
            if (result) {
                user = readUser(context, email);
                user.setSignIn(true);
                updateUser(context, user);
            }
            return result;
        }
        return false;
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
