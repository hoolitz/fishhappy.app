package tz.co.fishhappy.app.model;

import com.google.gson.annotations.SerializedName;

public class FishModel{

	@SerializedName("size")
	private String size;

	@SerializedName("imageUrl")
	private String imageUrl;

	@SerializedName("category_id")
	private int categoryId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("price")
	private int price;

	@SerializedName("name")
	private String name;

	@SerializedName("weight")
	private int weight;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("photo")
	private Photo photo;

	@SerializedName("id")
	private int id;

	@SerializedName("is_favourite")
	private boolean isFavourite;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void setSize(String size){
		this.size = size;
	}

	public String getSize(){
		return size;
	}

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setPrice(int price){
		this.price = price;
	}

	public int getPrice(){
		return price;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setWeight(int weight){
		this.weight = weight;
	}

	public int getWeight(){
		return weight;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setPhoto(Photo photo){
		this.photo = photo;
	}

	public Photo getPhoto(){
		return photo;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setIsFavourite(boolean isFavourite){
		this.isFavourite = isFavourite;
	}

	public boolean isIsFavourite(){
		return isFavourite;
	}

	@Override
 	public String toString(){
		return 
			"FishModel{" + 
			"size = '" + size + '\'' + 
			",category_id = '" + categoryId + '\'' +
			",imageUrl = '" + imageUrl + '\'' +
			",updated_at = '" + updatedAt + '\'' +
			",price = '" + price + '\'' + 
			",name = '" + name + '\'' + 
			",weight = '" + weight + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",photo = '" + photo + '\'' + 
			",id = '" + id + '\'' + 
			",is_favourite = '" + isFavourite + '\'' + 
			"}";
		}
}