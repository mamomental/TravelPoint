package kr.mamo.travelpoint.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by alucard on 2015-07-13.
 */
public interface Table {
    public String getTableName();
    public void createTable(SQLiteDatabase db);
    public void upgradeTable(SQLiteDatabase db, int oldVersion, int newVersion);
    public void dropTable(SQLiteDatabase db);
}
