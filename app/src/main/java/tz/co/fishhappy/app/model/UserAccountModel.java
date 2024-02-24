package tz.co.fishhappy.app.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserAccountModel{

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("favourites")
	private List<FavouritesItem> favourites;

	@SerializedName("phone")
	private String phone;

	@SerializedName("name")
	private String name;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("orders")
	private List<OrdersItem> orders;

	@SerializedName("id")
	private int id;

	@SerializedName("email")
	private String email;

    public UserAccountModel(){}

    public UserAccountModel(String updatedAt, List<FavouritesItem> favourites, String phone,
                            String name, String createdAt, List<OrdersItem> orders,
                            int id, String email) {
        this.updatedAt = updatedAt;
        this.favourites = favourites;
        this.phone = phone;
        this.name = name;
        this.createdAt = createdAt;
        this.orders = orders;
        this.id = id;
        this.email = email;
    }

    public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setFavourites(List<FavouritesItem> favourites){
		this.favourites = favourites;
	}

	public List<FavouritesItem> getFavourites(){
		return favourites;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setOrders(List<OrdersItem> orders){
		this.orders = orders;
	}

	public List<OrdersItem> getOrders(){
		return orders;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"UserAccountModel{" + 
			"updated_at = '" + updatedAt + '\'' + 
			",favourites = '" + favourites + '\'' + 
			",phone = '" + phone + '\'' + 
			",name = '" + name + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",orders = '" + orders + '\'' + 
			",id = '" + id + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}