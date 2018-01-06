package action;

import java.io.Serializable;

public class Sale implements Serializable {
	
	private String ID;
	private String Description;
	private String Discount;
	private String StoreID;
	
	public Sale()
	{
		this.ID=null;
		this.Description=null;
		this.Discount=null;
		this.StoreID=null;
	}
	
	public Sale(String store, String des, String dis)
	{
		this.StoreID=store;
		this.Description=des;
		this.Discount=dis;
	}
	
	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}
	
	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		Description = description;
	}
	
	/**
	 * @return the discount
	 */
	public String getDiscount() {
		return Discount;
	}
	
	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(String discount) {
		Discount = discount;
	}
	
	public String toString()
	{
		return ("ID: "+this.getID()+", Description: "+this.getDescription()+", Discount: "+this.getDiscount());
	}

	public String getStoreID() {
		return StoreID;
	}

	public void setStoreID(String storeID) {
		StoreID = storeID;
	}

}
