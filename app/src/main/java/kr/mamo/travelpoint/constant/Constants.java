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
            public String AUTO_LOGIN = "account_auto_login";
        }
        public interface License {
            public String LICENSE = "license_view";
        }
    }

    public interface ACTIVITY_RESULT {
        public int SETTINGS = 1001;
        public int CAMERA = 2001;
    }

    public interface Fragment {
        public interface MainActivity  {
            public int F1 = 1;
            public int F2 = 2;
            public int F3 = 3;
            public int F4 = 4;
        }
    }
}
