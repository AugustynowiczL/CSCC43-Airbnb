public class Listing {
	
	private final int uuid;
	private final String username;
	private final int type;
	private final double longitude;
	private final double latitude;
	private final String address;
	private final String country;
	private final String postal_code;
	private final String city;
	
	public Listing(int uuid, String username, int type, double longitude, double latitude, String country, String postal_code, String city, String address)
	{
		this.uuid = uuid;
		this.username = username;
		this.type =type;
		this.longitude = longitude;
		this.latitude = latitude;
		this.country = country;
		this.city = city;
		this.postal_code = postal_code;
		this.address = address;
	}

	public int getUuid() {
		return uuid;
	}

	public String getUsername() {
		return username;
	}

	public int getType() {
		return type;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public String getCountry() {
		return country;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public String getCity() {
		return city;
	}

	public String getAddress() {
		return address;
	}	
}
