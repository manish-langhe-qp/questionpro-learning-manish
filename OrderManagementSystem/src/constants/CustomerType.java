package constants;

public enum CustomerType {
	 REGULAR("Regular"),
	 PREMIUM("Premium"),
	 VIP("Vip");
	
	private final String value;

	private CustomerType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
