package kr.mamo.travelpoint.db;

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

        for (Schema.INDEX index : Schema.INDEX.values()) {
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
        db.execSQL(builder.toString());
    }

    public interface Schema {

        public enum COLUMN {
            NO ("no", "INTEGER PRIMARY KEY AUTOINCREMENT"),
            USER_NO("userNo", "INTEGER"),
            TRAVEL_NO("travelNo", "INTEGER")
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

        public enum INDEX {
            FK_USER_ID (Schema.COLUMN.USER_NO.getName(), User.TABLE_NAME, User.Schema.COLUMN.NO.getName()),
            FK_TRAVEL_ID (Schema.COLUMN.TRAVEL_NO.getName(), Travel.TABLE_NAME, Travel.Schema.COLUMN.NO.getName());
            private String columnName;
            private String referenceTable;
            private String referenceColumnName;

            public String getColumnName() {
                return columnName;
            }

            public void setColumnName(String columnName) {
                this.columnName = columnName;
            }

            public String getReferenceColumnName() {
                return referenceColumnName;
            }

            public void setReferenceColumnName(String referenceColumnName) {
                this.referenceColumnName = referenceColumnName;
            }

            public String getReferenceTable() {
                return referenceTable;
            }

            public void setReferenceTable(String referenceTable) {
                this.referenceTable = referenceTable;
            }

            INDEX(String columnName, String referenceTable, String referenceColumnName) {
                this.columnName = columnName;
                this.referenceTable = referenceTable;
                this.referenceColumnName = referenceColumnName;
            }
        }
    }
}
