import java.sql.*;
import java.util.Iterator;
import java.util.List;

public class QueryRepository {
	
	private final Connection conn;
	
	public QueryRepository(Connection conn)
	{
		this.conn = conn;
	}
	
	public User getUser(String username)
	{
		try 
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM Users WHERE id = '" + username + "';"; 
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				String uname = rs.getString("id");
				String upass = rs.getString("password");
				String name = rs.getString("name");
				String address = rs.getString("address");
				Date dob = rs.getDate("dob");
				int sin = rs.getInt("sin");
				return new User(uname, upass, name, address, dob, sin);
				
			}
			else 
			{
				return null;
			}	
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return null;
	}
	
	public Listing getListing(String userID, double latitude, double longitude)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM Listings WHERE uid = '" + userID + "' AND ROUND(latitude,3) = ROUND(" + latitude + ",3) AND ROUND(longitude,3) = ROUND(" + longitude + ", 3);";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				int uuid = rs.getInt("id");
				String username = rs.getString("uid");
				int type = rs.getInt("type");
				String country = rs.getString("country");
				String postal_code = rs.getString("postal_code");
				String city = rs.getString("city");
				String address = rs.getString("address");
				return new Listing(uuid, username, type, longitude, latitude, country, postal_code, city, address);
			}
			return null;
		}
		catch (SQLException e)
		{
			return null;
		}
	}
	
	public PaymentInformation getUserPaymentInformation(String username)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM PaymentInformation WHERE id = '" + username + "';";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				int creditcardnum = rs.getInt("creditcard");
				return new PaymentInformation(username, creditcardnum);
			}
			return null;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return null;
		}
	}
	
	public ResultSet getListingAvailabilities(int lid, String start_date, String end_date)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM Availability WHERE id = " + lid + " AND isAvailable = 0 AND date BETWEEN '" + start_date + "' AND '" + end_date + "';";
			return stmt.executeQuery(sql);
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return null;
		}
	}
	
	public Booking getBooking(String username, int lid, String start_date, String end_date)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM Bookings WHERE uid = '" + username + "' AND lid = " + lid + " AND start_date = '" + start_date + "' AND end_date = '" + end_date + "';";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				int bookingid = rs.getInt("id");
				return new Booking(bookingid, username, lid, start_date, end_date);
			}
			return null;	
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return null;
		}
	}
	
	public ResultSet getAllBookings(int lid)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM Bookings WHERE lid = " + lid + ";";
			return stmt.executeQuery(sql);
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return null;
		}
	}
	
	public ResultSet getAllAmenities(int lid)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM ListingAmenities WHERE lid = " + lid + ";";
			return stmt.executeQuery(sql);
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return null;
		}
	}
	
	public ResultSet getAllRenter(String username, String renter)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM listings JOIN bookings ON listings.id = bookings.lid WHERE listings.uid = '" + username + "' AND bookings.uid = '" + renter + "';";
			return stmt.executeQuery(sql);
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return null;
		}
	}
	
	public ResultSet getAllUserBookings(String username)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM Bookings where uid = '" + username + "';";
			return stmt.executeQuery(sql);
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return null;
		}
	}
	
	public ResultSet getAllUserListings(String username)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM Listings where uid = '" + username + "';";
			return stmt.executeQuery(sql);
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return null;
		}
	}
	
	public ResultSet getAllListingBookings(int lid)
	{
		try 
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM Bookings WHERE lid = " + lid + ";";
			return stmt.executeQuery(sql);
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return null;
		}
	}
	
	public void queryListingByCoordinates(float longitude, float latitude, float distance)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT *, 6371 * 2 * ASIN ( SQRT ( POWER ( SIN ( RADIANS (( " + latitude + " - latitude)/2)), 2) + COS (RADIANS (" + latitude + ")) * COS (RADIANS(latitude)) * POWER (SIN ( RADIANS ((" + longitude + " - longitude)/2)),2))) AS distance FROM Listings WHERE (6371 * 2 * ASIN (SQRT ( POWER ( SIN ( RADIANS (( " + latitude + " - latitude)/2)),2) + "
					+ "	COS ( RADIANS (" + latitude + ")) * COS ( RADIANS (latitude)) * "
							+ "POWER ( SIN (RADIANS ((" + longitude + " - longitude) /2)), 2)))) <= " + distance + " ORDER BY distance;";
			ResultSet rs = stmt.executeQuery(sql);
			printListingHelper(rs);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public void queryListingByPostalCode(String postal_code)
	{
		try
		{
			String forwarding_code = postal_code.substring(0,3).toLowerCase();
			Statement stmt = conn.createStatement();
			String sql  = "SELECT * FROM Listings WHERE LOWER(postal_code) LIKE '" + forwarding_code + "___';";
			ResultSet rs = stmt.executeQuery(sql);
			printListingHelper(rs);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public void queryListingByAddress(String address)
	{
		try
		{
			String addr = address.toLowerCase();
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM Listings WHERE LOWER(address) = '" + addr + "';";
			ResultSet rs = stmt.executeQuery(sql);
			printListingHelper(rs);
		}
		catch (SQLException e)
		{
			System.out.print(e);
		}
	}
	
	public void queryListingByAvailability(String start_date, String end_date)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "select * from listings where exists (select * from availability where listings.id = availability.id and availability.date between '" + start_date + "' and '" + end_date + "' and isavailable = 0 group by availability.id having count(*) = datediff('" + end_date + "', '" + start_date + "') + 1);";
			ResultSet rs = stmt.executeQuery(sql);
			printListingHelper(rs);
		}
		catch (SQLException e)
		{
			System.out.print(e);
		}
	}
	
	public void queryListingByFullSearch(String postal_code, String country, String address, String start_date, String end_date, List<String> listOfAmenities, float min_price, float max_price)
	{
		try
		{
			String sql = "select * from listings ";
			Statement stmt = conn.createStatement();
			if (!postal_code.equals(""))
			{
				sql = sql + " intersect ";
				sql = sql + "select * from listings where postal_code = '" + postal_code + "'";
			}
			if (!country.equals(""))
			{
				sql = sql + " intersect ";
				sql = sql + " select * from listings where country = '" + country + "'";
			}
			if (!address.equals(""))
			{
				sql = sql + " intersect ";
				sql = sql + " select * from listings where address = '" + address + "'";
			}
			if (!start_date.equals(""))
			{
				sql = sql + " intersect ";
				sql = sql + " select * from listings where exists (select * from availability where listings.id = availability.id and availability.date between '" + start_date + "' and '" + end_date + "' and isavailable = 0 group by availability.id having count(*) = datediff('" + end_date + "', '" + start_date + "') + 1) ";
			}
			if (!listOfAmenities.isEmpty())
			{
				sql = sql + " intersect ";
				sql = sql + " select * from listings where exists (select * from listingamenities where listings.id = listingamenities.lid and listingamenities.name IN (";
				Iterator<String> iterator = listOfAmenities.iterator();
				while (iterator.hasNext())
				{
					sql = sql + "'" + iterator.next() + "'";
					if (iterator.hasNext())
					{
						sql = sql + ", ";
					}
					else 
					{
						sql = sql + ")";
					}
				}
				sql = sql + " GROUP BY listingamenities.lid having count(*) = " + listOfAmenities.size() + ")";
			}
			if (min_price != -1)
			{
				sql = sql + " intersect ";
				sql = sql + " select * from listings natural join (select distinct id from availability where price > " + min_price + " and price < " + max_price + ") as price_table";
			}
			sql = sql + ";";
			ResultSet rs = stmt.executeQuery(sql);
			printListingHelper(rs);
		}
		catch (SQLException e)
		{
			System.out.print(e);
		}
	}
	
	public void reportBookingsByCity(String start_date, String end_date)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT city, count(*) as count FROM Bookings JOIN Listings ON Bookings.lid = Listings.id WHERE Bookings.start_date BETWEEN '" + start_date + "' AND '" + end_date + "' AND Bookings.end_date BETWEEN '" + start_date + "' AND '" + end_date + "' GROUP BY city";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				System.out.println("City: " + rs.getString("city") + ", Count: " + rs.getInt("count"));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public void reportBookingsByZip(String start_date, String end_date)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT city, postal_code, count(*) as count FROM Bookings JOIN Listings ON Bookings.lid = Listings.id WHERE Bookings.start_date BETWEEN '" + start_date + "' AND '" + end_date + "' AND Bookings.end_date BETWEEN '" + start_date + "' AND '" + end_date + "' GROUP BY city, postal_code";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				System.out.println("City: " + rs.getString("city") + ", Postal Code: " + rs.getString("postal_code") + ", Count: " + rs.getInt("count"));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public void reportByCountry()
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT country, count(*) as count FROM Listings GROUP BY country";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				System.out.println("Country: " + rs.getString("country") + ", Count: " + rs.getInt("count"));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}

	public void reportByCountryCity()
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT country, city, count(*) as count FROM Listings GROUP BY country, city";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				System.out.println("Country: " + rs.getString("country") + ", City: " + rs.getString("city") + ", Count: " + rs.getInt("count"));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public void reportByCountryCityZip()
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT country, city, postal_code, count(*) as count FROM Listings GROUP BY country, city, postal_code";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				System.out.println("Country: " + rs.getString("country") + ", City: " + rs.getString("city") + ", Postal Code: " + rs.getString("postal_code") + ", Count: " + rs.getInt("count"));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public void reportRankHostByCountry()
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT uid, country, count(*) as count FROM Listings GROUP BY uid, country ORDER BY country, count DESC";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				System.out.println("Hosts: " + rs.getString("uid") + ", Country: " + rs.getString("country") + ", Count: " + rs.getInt("count"));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public void reportRankHostByCity()
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT uid, city, count(*) as count FROM Listings GROUP BY uid, city ORDER BY city, count DESC";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				System.out.println("Hosts: " + rs.getString("uid") + ", City: " + rs.getString("city") + ", Count: " + rs.getInt("count"));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public void reportIdenify10PercentHost()
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT uid, country, city, COUNT(*) FROM Listings GROUP BY uid, country, city HAVING COUNT(*) > 0.1 * (SELECT COUNT(*) FROM Listings)";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				System.out.println("Host: " + rs.getString("uid"));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public void reportRankRentersByBooking(String start_date, String end_date)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT Bookings.uid as renter, COUNT(*) as count FROM Bookings JOIN Listings ON Bookings.lid = Listings.id WHERE start_date BETWEEN '" + start_date + "' AND '" + end_date + "' GROUP BY renter ORDER BY count DESC ";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				System.out.println("Host: " + rs.getString("renter") + " Count: " + rs.getInt("count"));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	
	public void reportRankRentersByBookingCity(String start_date, String end_date)
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT Bookings.uid as renter, city, COUNT(*) as count FROM Bookings JOIN Listings ON Bookings.lid = Listings.id WHERE EXISTS (SELECT 1 FROM Bookings b WHERE b.uid = Bookings.uid GROUP BY b.uid HAVING COUNT(*) >= 2) AND start_date BETWEEN '" + start_date + "' AND '" + end_date + "' GROUP BY renter,city ORDER BY count DESC ";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				System.out.println("Host: " + rs.getString("renter") + " Count: " + rs.getInt("count"));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public void reportRankCancellations()
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "(SELECT username, 'Renter' as type, COUNT(*) as count FROM RenterCancellations GROUP BY username ORDER BY count DESC) UNION (SELECT username, 'Host' as type, COUNT(*) as count FROM HostCancellations GROUP BY username ORDER BY count DESC) ORDER BY count DESC";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) 
			{
				System.out.println("Username: " + rs.getString("username") + ", Type: " + rs.getString("type") + ", Count: " + rs.getInt("count"));
			}
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public ResultSet reportNounPhrases()
	{
		try
		{
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM Listings JOIN (Bookings JOIN CommentHosts ON Bookings.id = CommentHosts.id) ON Listings.id = Bookings.lid";
			return stmt.executeQuery(sql);
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return null;
		}
	}
	
	public void printListingHelper (ResultSet rs)
	{
		try
		{
			while (rs.next())
			{
				String poster = rs.getString("uid");
				System.out.println("-------");
				System.out.println("Poster: " + poster);
				float lat = Float.parseFloat(rs.getString("latitude"));
				float lon = Float.parseFloat(rs.getString("longitude"));
				String country = rs.getString("country");
				String postal_code = rs.getString("postal_code");
				String city = rs.getString("city");
				System.out.println("Latitude: " + lat);
				System.out.println("Longitude: " + lon);
				System.out.println("Country: " + country);
				System.out.println("Postal code: " + postal_code);
				System.out.println("City: " + city);
				System.out.println("-------");
			}
			System.out.println("No more postings found.");
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
}
