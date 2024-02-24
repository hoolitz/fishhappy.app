package tz.co.fishhappy.app.model;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponseModel{

	@SerializedName("message")
	private String message;

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"ChangePasswordResponseModel{" + 
			"message = '" + message + '\'' + 
			"}";
		}
}