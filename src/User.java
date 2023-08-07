import java.sql.Date;

public class User {
	
	private final String username;
	private final String password;
	private final String name;
	private final String address;
	private final Date dob;
	private final int sin;
	
	public User(String username, String password, String name, String address, Date dob, int sin)
	{
		this.username = username;
		this.password = password;
		this.name = name;
		this.address = address;
		this.dob = dob;
		this.sin = sin;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public Date getDob() {
		return dob;
	}

	public int getSin() {
		return sin;
	}
	
	
}
