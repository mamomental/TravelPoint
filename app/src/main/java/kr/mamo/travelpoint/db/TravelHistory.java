package kr.mamo.travelpoint.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import kr.mamo.travelpoint.constant.Constants;
import kr.mamo.travelpoint.constant.ConstantsDB;

/**
 * Created by alucard on 2015-07-14.
 */
public class TravelHistory extends AbstractTable {
    public TravelHistory(Context context) {
        super(context);
    }

    @Override
    public String getTableName() {
        return ConstantsDB.ConstantsTableTravelHistory.TABLE_NAME;
    }

    @Override
    protected void doVersionLast(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ");
        builder.append(getTableName());
        builder.append(" (");
        for (ConstantsDB.ConstantsTableTravelHistory.COLUMN column : ConstantsDB.ConstantsTableTravelHistory.COLUMN.values()) {
            if (!column.getName().equals("_id")) {
                builder.append(", ");
            }
            builder.append(column.getName());
            builder.append(" ");
            builder.append(column.getType());
            builder.append(" ");
            builder.append(column.getExtension());
        }

        for (ConstantsDB.ConstantsTableTravelHistory.INDEX index : ConstantsDB.ConstantsTableTravelHistory.INDEX.values()) {
            builder.append(", ");
            builder.append("FOREIGN KEY(");
            builder.append(index.getColumnName());
            builder.append(") REFERENCES ");
            builder.append(index.getReferenceTable());
            builder.append("(");
            builder.append(index.getReferenceColumnName());
            builder.append(")");
        }
        builder.append(");");

        Log.d(Constants.LOGCAT_TAGNAME, "create table travel history : " + builder.toString());

        db.execSQL(builder.toString());
    }

    protected void doVersion1(SQLiteDatabase db) {
        doVersionLast(db);
    }

    protected void doVersion2(SQLiteDatabase db) {
    }
    protected void doVersion3(SQLiteDatabase db) {
    }
}
