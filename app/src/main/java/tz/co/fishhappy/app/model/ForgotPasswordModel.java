package tz.co.fishhappy.app.model;

/**
 * Created by Simon on 01-May-17.
 */

public class ForgotPasswordModel {

    private String username;

    public ForgotPasswordModel(){

    }

    public ForgotPasswordModel(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
