package tz.co.fishhappy.app.model;

import com.google.gson.annotations.SerializedName;

public class Photo{

	@SerializedName("url")
	private String url;

	@SerializedName("uri")
	private String uri;

	@SerializedName("name")
	private String name;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
 	public String toString(){
		return 
			"Photo{" + 
			"name = '" + name + '\'' +
			",url = '" + url + '\'' +
			",uri = '" + uri + '\'' +
			"}";
		}
}