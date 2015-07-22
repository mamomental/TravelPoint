package kr.mamo.travelpoint.db.table;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by alucard on 2015-07-14.
 */
public class TravelHistory extends AbstractTable {
    public static final String TABLE_NAME = "TravelHistory";

    public TravelHistory(Context context) {
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

    @Override
    protected void doCreateTableIndex(SQLiteDatabase db) {
        for (Schema.INDEX idx : Schema.INDEX.values()) {
            db.execSQL(idx.getValue());
        }
    }

    public interface Schema {
        public enum COLUMN {
            NO ("no", "INTEGER PRIMARY KEY AUTOINCREMENT"),
            USER_NO("userNo", "INTEGER"),
            TRAVEL_NO("travelNo", "INTEGER"),
            TRAVEL_POINT_NO("travelPointNo", "INTEGER"),
            LATITUDE("latitude", "DOUBLE"),
            LONGITUDE("longitude", "DOUBLE"),
            DIARY("diary", "TEXT"),
            CREATE_DATE("createDate", "DATETIME DEFAULT CURRENT_TIMESTAMP")
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
            FK_USER_ID (Schema.COLUMN.USER_NO.getName(), User.TABLE_NAME + "(" + User.Schema.COLUMN.NO.getName() + ")"),
            FK_TRAVEL_ID (Schema.COLUMN.TRAVEL_NO.getName(), Travel.TABLE_NAME+ "(" + Travel.Schema.COLUMN.NO.getName() + ")"),
            FK_TRAVEL_POINT_ID (COLUMN.TRAVEL_POINT_NO.getName(), TravelPoint.TABLE_NAME+ "(" + TravelPoint.Schema.COLUMN.NO.getName() + ")");

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

        public enum INDEX {
            IDX_EMAIL ("CREATE UNIQUE INDEX idx_travel_history_userNotravelPointNo ON TravelHistory (userNo, travelPointNo);");

            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            INDEX(String value) {
                this.value = value;
            }
        }
    }
}
