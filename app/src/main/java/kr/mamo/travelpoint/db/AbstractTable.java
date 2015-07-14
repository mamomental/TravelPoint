package kr.mamo.travelpoint.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import kr.mamo.travelpoint.constant.Constants;

/**
 * Created by alucard on 2015-07-13.
 */
public abstract class AbstractTable implements Table {
    protected Context context;
    public AbstractTable(Context context) {
        this.context = context;
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        doVersionLast(db);
    }

    @Override
    public void upgradeTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= newVersion) {
            Log.d(Constants.LOGCAT_TAGNAME, "AbstractTable::upgradeTable doVersion " + oldVersion);
            doVersion(db, oldVersion);
            upgradeTable(db, oldVersion + 1, newVersion);
        }
    }

    @Override
    public void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + getTableName());
    }

    protected abstract void doVersionLast(SQLiteDatabase db);
    protected abstract void doVersion1(SQLiteDatabase db);
    protected abstract void doVersion2(SQLiteDatabase db);
    protected abstract void doVersion3(SQLiteDatabase db);

    protected void setInitialData(SQLiteDatabase db, int version) {
        AssetManager manager = context.getAssets();
        try {
            InputStream is = manager.open("db/" + getTableName() + version + ".txt");

            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = r.readLine()) != null) {
                Log.d(Constants.LOGCAT_TAGNAME, "query::" + line);
                db.execSQL(line);
            }

            if (null != is) is.close();
            if (null != r) r.close();
        } catch (IOException e) {
            Log.e("ErrorMessage : ", e.getMessage());
        }
    }
    private void doVersion(SQLiteDatabase db, int version) {
        switch (version) {
            case 1:
                doVersion1(db);
                break;
            case 2:
                doVersion2(db);
                break;
            case 3:
                doVersion3(db);
                break;
            default:
                doVersionLast(db);
                break;
        }
    }
}
