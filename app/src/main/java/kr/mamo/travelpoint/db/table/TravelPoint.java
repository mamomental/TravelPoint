package kr.mamo.travelpoint.db.table;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by alucard on 2015-07-14.
 */
public class TravelPoint extends AbstractTable {
    public static final String TABLE_NAME = "TravelPoint";


    public TravelPoint(Context context) {
        super(context);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }



    @Override
    protected void doCreateTable(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE ");
        builder.append(getTableName());
        builder.append(" (");
        for (Schema.COLUMN column : Schema.COLUMN.values()) {
            if (!column.getName().equals("no")) {
                builder.append(", ");
            }
            builder.append(column.getName());
            builder.append(" ");
            builder.append(column.getType());
        }

        for (Schema.FKEY fkey : Schema.FKEY.values()) {
            builder.append(", ");
            builder.append("FOREIGN KEY(");
            builder.append(fkey.getColumnName());
            builder.append(") REFERENCES ");
            builder.append(fkey.getReference());
        }
        builder.append(");");
        db.execSQL(builder.toString());
    }

    public interface Schema {
        public enum COLUMN {
            NO ("no", "INTEGER PRIMARY KEY AUTOINCREMENT"),
            TRAVEL_NO("travelNo", "INTEGER"),
            NAME("name", "TEXT"),
            LATITUDE("latitude", "DOUBLE"),
            LONGITUDE("longitude", "DOUBLE"),
            DESCRIPTION("description", "TEXT")
            ;

            private String name;
            private String type;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            COLUMN(String name, String type) {
                this.name = name;
                this.type = type;
            }
        }

        public enum FKEY {
            FK_TRAVEL_ID (Schema.COLUMN.TRAVEL_NO.getName(), Travel.TABLE_NAME + "(" + User.Schema.COLUMN.NO.getName() + ")")
            ;
            private String columnName;
            private String reference;

            public String getColumnName() {
                return columnName;
            }

            public void setColumnName(String columnName) {
                this.columnName = columnName;
            }

            public String getReference() {
                return reference;
            }

            public void setReference(String reference) {
                this.reference = reference;
            }

            FKEY(String columnName, String reference) {
                this.columnName = columnName;
                this.reference = reference;
            }
        }
    }
}
