package kr.mamo.travelpoint.db.table;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import kr.mamo.travelpoint.constant.Constants;

/**
 * Created by alucard on 2015-07-13.
 */
public class User extends AbstractTable {
    public static final String TABLE_NAME = "User";

    public User(Context context) {
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
            if (!column.getName().equals(Schema.COLUMN.NO.getName())) {
                builder.append(", ");
            }
            builder.append(column.getName());
            builder.append(" ");
            builder.append(column.getType());
        }
        builder.append(");");

        Log.d(Constants.LOGCAT_TAGNAME, builder.toString());

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
            EMAIL("email", "TEXT"),
            PASSWORD("password", "TEXT"),
            SIGN_IN("signIn", "INTEGER DEFAULT 0");

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
            IDX_EMAIL ("CREATE UNIQUE INDEX idx_user_email ON User(email);");

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
