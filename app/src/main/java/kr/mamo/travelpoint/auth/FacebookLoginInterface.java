package kr.mamo.travelpoint.auth;

/**
 * Created by mentalmamo on 15. 8. 13..
 */
public interface FacebookLoginInterface {
    public void checkLoggedIn();
    public void asyncLogin(String email, String password);
    public FacebookLoginResultInterface getResultInterface();
    public void setResultInterface(FacebookLoginResultInterface resultInterface);
}
