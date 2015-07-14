package kr.mamo.travelpoint.constant;

/**
 * Created by alucard on 2015-07-13.
 */
public interface ConstantsDB {
    public String DATABASE_NAME = "TravelPoint.db";
    public int DATABASE_VERSION = 1;

    public interface ConstantsTableUser {
        public String TABLE_NAME = "User";

        public enum COLUMN {
            ID ("_id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
            EMAIL("email", "TEXT", ""),
            PASSWORD("password", "TEXT", "");
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

    public interface ConstantsTableTravel {
        public String TABLE_NAME = "Travel";

        public enum COLUMN {
            ID ("_id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
            TYPE ("type", "INTEGER", ""),
            NAME("name", "TEXT", ""),
            DESCRIPTION("description", "TEXT", "")
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

    public interface ConstantsTableTravelPoint {
        public String TABLE_NAME = "TravelPoint";

        public enum COLUMN {
            ID ("_id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
            TRAVEL_ID("travelId", "INTEGER", ""),
            NAME("name", "TEXT", ""),
            LATITUDE("latitude", "DOUBLE", ""),
            LONGITUDE("longitude", "DOUBLE", "")
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

        public enum INDEX {
            FK_TRAVEL_ID (ConstantsTableTravelPoint.COLUMN.TRAVEL_ID.getName(), ConstantsTableTravel.TABLE_NAME, ConstantsTableUser.COLUMN.ID.getName())
            ;
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

    public interface ConstantsTableTravelHistory {
        public String TABLE_NAME = "TravelHistory";

        public enum COLUMN {
            ID ("_id", "INTEGER", "PRIMARY KEY AUTOINCREMENT"),
            USER_ID("userId", "INTEGER", ""),
            TRAVEL_ID("travelId", "INTEGER", "")
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

        public enum INDEX {
            FK_USER_ID (ConstantsTableTravelHistory.COLUMN.USER_ID.getName(), ConstantsTableUser.TABLE_NAME, ConstantsTableUser.COLUMN.ID.getName()),
            FK_TRAVEL_ID (ConstantsTableTravelHistory.COLUMN.TRAVEL_ID.getName(), ConstantsTableTravel.TABLE_NAME, ConstantsTableUser.COLUMN.ID.getName())
            ;
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
