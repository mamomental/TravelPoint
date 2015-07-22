package kr.mamo.travelpoint.constant;

/**
 * Created by alucard on 2015-07-13.
 */
public interface Constants {
    public String APP_PACKAGE_NAME = "kr.mamo.travelpoint";
    public String LOGCAT_TAGNAME = "TP";

    public interface DB {
        public String DATABASE_NAME = "TravelPoint.db";
        public int DATABASE_VERSION = 1;
    }

    public interface Preference {
        public interface Account {
            public String EMAIL = "account_email";
            public String LOGOUT = "account_logout";
        }
    }

    public interface ACTIVITY_RESULT {
        public int SETTINGS = 1001;
    }
}
