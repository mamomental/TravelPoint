package kr.mamo.travelpoint.constant;

/**
 * Created by alucard on 2015-07-13.
 */
public interface ConstantsDB {
    public String DATABASE_NAME = "TravelPoint.db";
    public int DATABASE_VERSION = 2;

    public interface ConstantsTableUser {
        public String TABLE_NAME = "user";

        public enum COLUMN {
            ID ("_id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
            EMAIL("email", "TEXT", "")
            ;

            private String name;
            private String type;
            private String extension;

            public String getExtension() {
                return extension;
            }

            public void setExtension(String extension) {
                this.extension = extension;
            }

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

            COLUMN(String name, String type, String extension) {
                this.name = name;
                this.type = type;
                this.extension = extension;
            }
        }
    }
}
