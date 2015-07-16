package kr.mamo.travelpoint.db.domain;

/**
 * Created by alucard on 2015-07-16.
 */
public class User {
    private int no;
    private String email;
    private String password;
    private boolean signIn;

    public User(int no, String email, String password, boolean signIn) {
        this.no = no;
        this.email = email;
        this.password = password;
        this.signIn = signIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSignIn() {
        return signIn;
    }

    public void setSignIn(boolean signIn) {
        this.signIn = signIn;
    }
}
