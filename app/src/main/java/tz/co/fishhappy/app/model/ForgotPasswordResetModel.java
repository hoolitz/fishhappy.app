package tz.co.fishhappy.app.model;

/**
 * Created by Simon on 02-May-17.
 */

public class ForgotPasswordResetModel {

    private String code;
    private String password;

    public ForgotPasswordResetModel(){}

    public ForgotPasswordResetModel(String code, String password) {
        this.code = code;
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
