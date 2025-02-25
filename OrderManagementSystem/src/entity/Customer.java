package entity;

import constants.CustomerType;

public class Customer {
	
	private final String name;
	private final String type; 
	private double discount;

    public Customer(String name, String type) {
        this.name = name;
        this.type = type;
    }  

    public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	private void setDiscount() {
        if (type.equals(CustomerType.REGULAR.getValue())) {
            discount = 0.05;
        } else if (type.equals(CustomerType.PREMIUM.getValue())) {
            discount = 0.1;
        } else if (type.equals(CustomerType.VIP.getValue())) {
            discount = 0.2;
        }
    }
}
