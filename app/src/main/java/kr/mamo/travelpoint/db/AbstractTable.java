package kr.mamo.travelpoint.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by alucard on 2015-07-13.
 */
public abstract class AbstractTable implements Table {
    @Override
    public void createTable(SQLiteDatabase db) {
        doVersionLast(db);
    }

    @Override
    public void upgradeTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= newVersion) {
            doVersion(db, oldVersion);
            upgradeTable(db, oldVersion + 1, newVersion);
        }
    }

    @Override
    public void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + getTableName());
    }

//    protected abstract String getTableName();
    protected abstract void doVersionLast(SQLiteDatabase db);
    protected abstract void doVersion1(SQLiteDatabase db);
    protected abstract void doVersion2(SQLiteDatabase db);
    protected abstract void doVersion3(SQLiteDatabase db);

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
