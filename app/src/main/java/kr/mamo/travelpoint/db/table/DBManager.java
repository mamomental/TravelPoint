package kr.mamo.travelpoint.db.table;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

import kr.mamo.travelpoint.constant.Constants;

/**
 * Created by alucard on 2015-07-13.
 */
public class DBManager extends SQLiteOpenHelper {
    private Map<String, Table> tables;

    private User user;

    public DBManager(Context context) {
        super(context, Constants.DB.DATABASE_NAME, null, Constants.DB.DATABASE_VERSION);

        tables = new HashMap<String, Table>();
        tables.put(User.TABLE_NAME, new User(context));
        tables.put(Travel.TABLE_NAME, new Travel(context));
        tables.put(TravelPoint.TABLE_NAME, new TravelPoint(context));
        tables.put(TravelHistory.TABLE_NAME, new TravelHistory(context));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Table table : tables.values()) {
            table.createTable(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Table table : tables.values()) {
            table.upgradeTable(db, oldVersion, newVersion);
        }
    }
}
