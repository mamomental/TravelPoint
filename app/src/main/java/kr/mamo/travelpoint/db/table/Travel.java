package kr.mamo.travelpoint.db.table;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by alucard on 2015-07-14.
 */
public class Travel extends AbstractTable {
    public static final String TABLE_NAME = "Travel";

    public Travel(Context context) {
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

        builder.append(");");
        db.execSQL(builder.toString());
    }

    public interface Schema {
        public enum COLUMN {
            NO ("no", "INTEGER PRIMARY KEY AUTOINCREMENT"),
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
    }
}
