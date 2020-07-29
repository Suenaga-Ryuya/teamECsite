package jp.co.internous.ocean.model.form;

import java.io.Serializable;

public class CartForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int userId;
	private int productId;
	private int productCount;
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public int getProductId() {
		return productId;
	}
	
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}
	
	public int getProductCount() {
		return productCount;
	}

}