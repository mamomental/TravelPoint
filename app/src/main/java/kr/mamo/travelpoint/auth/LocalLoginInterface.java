package kr.mamo.travelpoint.auth;

/**
 * Created by mentalmamo on 15. 8. 13..
 */
public interface LocalLoginInterface {
    public boolean isLoggedIn();
    public void asyncLogin(String email, String password);
    public LocalLoginResultInterface getResultInterface();
    public void setResultInterface(LocalLoginResultInterface resultInterface);
}
