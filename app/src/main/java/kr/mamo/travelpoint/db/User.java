package kr.mamo.travelpoint.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.constant.ConstantsDB;

/**
 * Created by alucard on 2015-07-13.
 */
public class User extends AbstractTable {
    @Override
    public String getTableName() {
        return ConstantsDB.ConstantsTableUser.TABLE_NAME;
    }

    @Override
    protected void doVersionLast(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ");
        builder.append(getTableName());
        builder.append(" (");
        for (ConstantsDB.ConstantsTableUser.COLUMN column : ConstantsDB.ConstantsTableUser.COLUMN.values()) {
            if (!column.getName().equals("_id")) {
                builder.append(", ");
            }
            builder.append(column.getName());
            builder.append(" ");
            builder.append(column.getType());
            builder.append(" ");
            builder.append(column.getExtension());
        }
        builder.append(");");

        db.execSQL(builder.toString());
    }

    protected void doVersion1(SQLiteDatabase db) {
        doVersionLast(db);
    }
    protected void doVersion2(SQLiteDatabase db) {
        Log.i(Constants.LOGCAT_TAGNAME, "not yet");
    }

    protected void doVersion3(SQLiteDatabase db) {
        Log.i(Constants.LOGCAT_TAGNAME, "not yet");
    }
}
