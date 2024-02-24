package tz.co.fishhappy.app.model;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordModel{

	@SerializedName("old_password")
	private String oldPassword;

	@SerializedName("new_password")
	private String newPassword;

    public ChangePasswordModel(){
        //Empty Constructor
    }

    public ChangePasswordModel(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public void setOldPassword(String oldPassword){
		this.oldPassword = oldPassword;
	}

	public String getOldPassword(){
		return oldPassword;
	}

	public void setNewPassword(String newPassword){
		this.newPassword = newPassword;
	}

	public String getNewPassword(){
		return newPassword;
	}

	@Override
 	public String toString(){
		return 
			"ChangePasswordModel{" + 
			"old_password = '" + oldPassword + '\'' + 
			",new_password = '" + newPassword + '\'' + 
			"}";
		}
}