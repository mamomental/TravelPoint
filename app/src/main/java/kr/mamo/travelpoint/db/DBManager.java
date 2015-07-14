package kr.mamo.travelpoint.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

import kr.mamo.travelpoint.constant.ConstantsDB;

/**
 * Created by alucard on 2015-07-13.
 */
public class DBManager extends SQLiteOpenHelper implements ConstantsDB {
    private Map<String, Table> tables;

    private User user;

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        tables = new HashMap<String, Table>();
        tables.put(ConstantsTableUser.TABLE_NAME, new User(context));
        tables.put(ConstantsTableTravel.TABLE_NAME, new Travel(context));
        tables.put(ConstantsTableTravelPoint.TABLE_NAME, new TravelPoint(context));
        tables.put(ConstantsTableTravelHistory.TABLE_NAME, new TravelHistory(context));

        getWritableDatabase();
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
