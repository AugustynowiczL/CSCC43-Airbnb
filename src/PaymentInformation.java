public class PaymentInformation {

	private String user;
	private int creditCard;
	
	public PaymentInformation(String username, int num)
	{
		this.user = username;
		this.creditCard = num;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(int creditCard) {
		this.creditCard = creditCard;
	}	
}
