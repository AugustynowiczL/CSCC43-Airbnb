import java.sql.*;
import java.time.LocalDate;

public class OperationRepository 
{
	private final Connection conn;
	
	public OperationRepository(Connection conn) 
	{	
		this.conn = conn;
	}
	
	/* Create a new user given the details required for the user */
	public boolean createUser(String username, String password, String name, String addr,
								String dob, String occupation, int sin)
	{
		try 
		{
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO Users(id, password, name, address, dob, occupation, sin) VALUES ('" + username + "', '" + password + "', '" + name + "', '" + addr + "', '" + dob + "', '" + occupation + "', " + Integer.toString(sin) + ");";
			int rs = stmt.executeUpdate(sql);
			System.out.println("Rows affected: " + rs);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}	
	}
	
	/* Create Listing for the given UserID */
	public boolean createListing(String userID, int type, double latitude, double longitude, String country, String postal_code, String city, String address, double pricing)
	{
		try
		{
			/*Insert into listing table */
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO Listings(uid, type, latitude, longitude, address, country, postal_code, city) VALUES ('" + userID + "', " + type + ", " + latitude + ", " + longitude + ", '" + address + "', '" + country + "', '" + postal_code + "', '" + city + "');";
			int rs = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println("Rows affected: " + rs);
			ResultSet generatedKey = stmt.getGeneratedKeys();
			if (generatedKey.next())
			{
				int genID = generatedKey.getInt(1);
				/* Given successful listing posted, create calendar for it */
				boolean success = createCalendar(genID, pricing);
				if (!success)
				{
					System.out.println("Failed to create calendar");
					return false;
				}
			}
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	/* Adjust listing price for the given listing for the date interval */
	public void adjustListingPrice(int lid, double price, String start_date, String end_date)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "UPDATE Availability SET price = " + price + " WHERE date BETWEEN '" + start_date + "' AND '" + end_date + "';";
			stmt.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	/* Create Calendar for listing with unique ID */
	private boolean createCalendar(int id, double pricing)
	{
		try 
		{
			LocalDate startDate = LocalDate.of(2023, 1, 1);
			LocalDate endDate = LocalDate.of(2023, 12, 31);
			
			LocalDate currentDate = startDate;
			
			Statement stmt = conn.createStatement();			
			while (!currentDate.isAfter(endDate))
			{
				String sql = "INSERT INTO Availability(id, date, isAvailable, price) VALUES (" + id + ", '" + currentDate.toString() + "', 0, " + pricing + ");";
				stmt.executeUpdate(sql);
				currentDate = currentDate.plusDays(1);
			}
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	/* Add Amenities to Listing */
	public boolean createAmenity(int id, String name)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO ListingAmenities(lid, name) VALUES (" + id + ", '" + name + "');";
			int rs = stmt.executeUpdate(sql);
			System.out.println("Rows affected: " + rs);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public void deleteAmenity(int id, String name)
	{
		try 
		{
			Statement stmt = conn.createStatement();
			String sql = "DELETE FROM ListingAmenities WHERE lid = " + id + " AND name = '" + name + "';";
			int rs = stmt.executeUpdate(sql);
			System.out.println("Rows deleted: " + rs);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	/* Delete Listing given the listingID */
	public boolean deleteListing(int listingID)
	{
		try 
		{
			Statement stmt = conn.createStatement();
			/* Delete all availabilities and bookings*/
			String sql = "DELETE FROM Bookings WHERE lid = " + listingID + ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM Availability WHERE id = " + listingID + ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM ListingAmenities where lid = " + listingID + ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM Listings WHERE id = " + listingID + ";";
			int rs = stmt.executeUpdate(sql);
			System.out.println("Rows deleted: " + rs);
			return true;
		}
		catch (SQLException e) 
		{
			System.out.println(e);
			return false;
		}
	}
	
	/* Book listing for the following interval of days */
	public boolean createBooking(int lid, String uid, String start_date, String end_date)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO Bookings(uid, lid, start_date, end_date) VALUES ('" + uid + "', " + lid + ", '" + start_date + "', '" + end_date + "');";
			int rs = stmt.executeUpdate(sql);
			System.out.println("Rows affected: " + rs);
			/* Adjust availability of the listing after the booking has process */
			sql = "UPDATE Availability SET isAvailable = 1 WHERE id = " + lid + " AND date BETWEEN '" + start_date + "' AND '" + end_date + "';";
			rs = stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	
	public boolean createPaymentInformation(String username, int num)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO PaymentInformation(id, creditcard) VALUES ('" + username + "', " + num + ");";
			int rs = stmt.executeUpdate(sql);
			System.out.println("Affected rows : " + rs);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public boolean makeUnavailable(int id, String date)
	{
		try 
		{
			Statement stmt = conn.createStatement();
			String sql = "UPDATE Availability SET isAvailable = 1 WHERE id = " + id + " AND date = '" + date + "';";
			int rs = stmt.executeUpdate(sql);
			System.out.println("Affected rows: " + rs);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public boolean changePrice(int id, String date, float price)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "Update Availability Set price = " + price + " WHERE id = " + id + " AND date = '" + date + "';";
			int rs = stmt.executeUpdate(sql);
			System.out.println("Affected rows: " + rs);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public boolean deleteBooking(int bid, int lid, String start_date, String end_date)
	{
		try
		{
			// First delete from booking table
			Statement stmt = conn.createStatement();
			String sql = "DELETE FROM RatingRenters WHERE id = " + bid + ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM RatingHosts WHERE id = " + bid + ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM CommentRenters WHERE id = " + bid + ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM CommentHosts WHERE id = " + bid + ";";
			stmt.executeUpdate(sql);
			sql = "DELETE FROM Bookings WHERE id = " + bid + " AND lid = " + lid + " AND start_date = '" + start_date + "' AND end_date = '" + end_date + "';";
			stmt.executeUpdate(sql);
			//Change availability to open
			sql = "UPDATE Availability SET isAvailable = 0 WHERE id = " + lid + " AND date BETWEEN '" + start_date + "' AND '" + end_date + "';";
			stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public boolean addRenterRating(int bookingID, int rating)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO RatingRenters(id, rating) VALUES (" + bookingID + ", " + rating + ");";
			stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public boolean addRenterComment(int bookingID, String comment)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO CommentRenters(id, comment) VALUES (" + bookingID + ", '" + comment + "');";
			stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public boolean addHostRating(int bookingID, int rating)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO RatingHosts(id, rating) VALUES (" + bookingID + ", " + rating + ");";
			stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public boolean addHostComment(int bookingID, String comment)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO CommentHosts(id, comment) VALUES (" + bookingID + ", '" + comment + "');";
			stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public boolean deletePaymentInfo(String username)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "DELETE FROM PaymentInformation WHERE id = '" + username + "';";
			stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public boolean deleteUser(String username)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "DELETE FROM Users WHERE id = '" + username + "';";
			stmt.executeUpdate(sql);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public void addRenterCancellation(String username)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO RenterCancellations(username) VALUE ('" + username + "');";
			stmt.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public void addHostCancellation(String username)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO HostCancellations(username) VALUE ('" + username + "');";
			stmt.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
}
