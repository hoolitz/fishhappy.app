package tz.co.fishhappy.app.model;

/**
 * Created by Simon on 29-Apr-17.
 */

public class LoginModel {

    private String email;
    private String password;

    public LoginModel(){}

    public LoginModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
