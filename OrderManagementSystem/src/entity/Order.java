package entity;

import java.util.ArrayList;
import java.util.List;

public class Order {
	private final Customer customer;
	private final List<String> items;
	private final List<Double> prices;
	private double totalPrice;
	private double discountedPrice;

	    public Order(Customer customer) {
	        this.customer = customer;
	        this.items = new ArrayList<>();
	        this.prices = new ArrayList<>();
	    }

	    private void addItem(String item, double price) {
	        items.add(item);
	        prices.add(price);
	        calculateTotal();
	    }

	    private void calculateTotal() {
	        totalPrice = 0;
	        for (double price : prices) {
	            totalPrice += price;
	        }
	        applyDiscount();
	    }

	    private void applyDiscount() {
	        discountedPrice = totalPrice - (totalPrice * customer.getDiscount());
	    }

	    private void printOrder() {
	        System.out.println("Customer: " + customer.getName());
	        System.out.println("Items: " + items);
	        System.out.println("Total Price: " + totalPrice);
	        System.out.println("Discounted Price: " + discountedPrice);
	    }


}
