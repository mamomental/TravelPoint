package kr.mamo.travelpoint.auth;

/**
 * Created by mentalmamo on 15. 8. 13..
 */
public interface FacebookLoginResultInterface {
    public void isLoggedIn(boolean loggedIn, String email, String id);
}
