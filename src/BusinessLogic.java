import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

public class BusinessLogic {

	private final OperationRepository repository;
	private final QueryRepository qrepository;
	
	public BusinessLogic(Connection conn)
	{
		this.qrepository = new QueryRepository(conn);
		this.repository = new OperationRepository(conn);
	}
	
	/* All possible user actions with business logic to stop
	 * invalid requests from occurring.
	 */
	public boolean verifyUserProfile(String username, String password)
	{
		User user = qrepository.getUser(username);
		if (user != null)
		{
			return user.getPassword().equals(password);
		}
		return false;
	}
	
	public boolean createUserProfile(String username, String password, String name, String addr, String dob, String occupation, int sin)
	{
		/* Check that username doesn't already exists */
		if (qrepository.getUser(username) == null && ChronoUnit.YEARS.between(LocalDate.parse(dob), LocalDate.now()) > 18)
		{
			//check that the user is at least 18 using dob
			return repository.createUser(username, password, name, addr, dob, occupation, sin);
		}
		else 
		{
			return false;
		}
	}
	
	public boolean deleteProfile(String username)
	{
		try
		{
			ResultSet rs = qrepository.getAllUserBookings(username);
			while (rs.next())
			{
				int bid = rs.getInt("id");
				int lid = rs.getInt("lid");
				String start_date = rs.getString("start_date");
				String end_date = rs.getString("end_date");
				repository.deleteBooking(bid, lid, start_date, end_date);
			}
			rs = qrepository.getAllUserListings(username);
			while (rs.next())
			{
				int id = rs.getInt("id");
				ResultSet rs2 = qrepository.getAllListingBookings(id);
				while (rs2.next()) 
				{
					int bid = rs2.getInt("id");
					String start_date = rs2.getString("start_date");
					String end_date = rs2.getString("end_date");
					
					repository.deleteBooking(bid, id, start_date, end_date);
				}
				// **** delete all bookings associated with this listing first 
				repository.deleteListing(id);
			}
			repository.deletePaymentInfo(username);
			repository.deleteUser(username);
			return true;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public boolean createListing(String userID, int type, double latitude, double longitude, String country, String postal_code, String city, String address, double pricing)
	{
		return repository.createListing(userID, type, latitude, longitude, country, postal_code, city, address, pricing);
	}
	public boolean deleteListing(String userID, double latitude, double longitude)
	{
		/* We are assuming that no user has multiple listings at the same coordinates */
		/* Ensure that the user that is deleting the listing is the owner */
		Listing l = qrepository.getListing(userID, latitude, longitude);
		if (l != null)
		{
			return repository.deleteListing(l.getUuid());
		}
		else 
		{
			System.out.println("You do not have access to delete this listing");
			return false;
		}
	}
	
	
	
	public boolean verifyUserListing(String username, double longitude, double latitude)
	{
		Listing listing = qrepository.getListing(username, latitude, longitude);
		if (listing != null)
		{
			return true;
		}
		return false;
	}
	
	public Listing getUserListing(String username, double latitude, double longitude)
	{
		return qrepository.getListing(username, latitude, longitude);
	}
	
	public boolean addListingAmenity(String username, double longitude, double latitude, String amenity_name)
	{
		Listing listing = qrepository.getListing(username, latitude, longitude);
		if (listing != null)
		{
			boolean success = repository.createAmenity(listing.getUuid(), amenity_name);
			return success;
		}
		return false;
	}
	
	public boolean verifyUserPayment(String username)
	{
		PaymentInformation payment = qrepository.getUserPaymentInformation(username);
		if (payment != null)
		{
			return true;
		}
		return false;
	}
	
	public boolean addUserPaymentInformation(String username, int creditCard)
	{
		return repository.createPaymentInformation(username, creditCard);
	}
	
	public boolean addBooking(String username, int listingID, String start_date, String end_date)
	{
		/* Check if the booking is available for this time period */
		if (isListingAvailable(listingID, start_date, end_date))
		{
			return repository.createBooking(listingID, username, start_date, end_date);
		}
		else 
		{
			System.out.println("The listing is booked during those days");
			return false;
		}
	}
	
	public boolean isListingAvailable(int listingID, String start_date, String end_date)
	{
		ResultSet rs = qrepository.getListingAvailabilities(listingID, start_date, end_date);
		int count = 0;
		try
		{
			while (rs.next())
			{
				count++;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}

		/* Get number of days in between start_date and end_date and compare to 
		 * the number of rows returned. They must match for the booking to be available.
		 */
		try 
		{
			LocalDate start = LocalDate.parse(start_date);
			LocalDate end = LocalDate.parse(end_date);
			long daysBetween = ChronoUnit.DAYS.between(start, end) + 1;
			return count == daysBetween;
		}
		catch (DateTimeParseException e)
		{
			System.out.println("Invalid date given");
			return false;
		}
	}
	
	public boolean makeUnavailable(int id, String date)
	{
		return repository.makeUnavailable(id, date);
	}
	
	public boolean changePrice(int id, String date, float price)
	{
		return repository.changePrice(id, date, price);
	}
	
	public boolean deleteBooking(String username, int lid, String start_date, String end_date)
	{
		Booking booking = qrepository.getBooking(username, lid, start_date, end_date);
		if (booking == null)
		{
			System.out.println("This booking does not exists");
			return false;
		}
		else 
		{
			return repository.deleteBooking(booking.getId(), lid, start_date, end_date);
		}
	}
	
	public boolean ratingRenter(String username, String renter, int rating, String date)
	{
		//Ensure that the renter has recently booked a listing from the user
		ResultSet rs = qrepository.getAllRenter(username, renter);
		try
		{
			while (rs.next())
			{
				LocalDate bookingDate = rs.getDate("bookings.end_date").toLocalDate();
				//If they have booked within 100 days
				if (ChronoUnit.DAYS.between(LocalDate.parse(date), bookingDate) + 1 < 90)
				{
					int BookingID = rs.getInt("bookings.id");
					if (repository.addRenterRating(BookingID, rating))
					{
						return true;
					}
					else
					{
						System.out.println("Failed to insert rating");
						return false;
					}
				}
			}
			return false;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public boolean commentRenter(String username, String renter, String comment, String date)
	{
		//Ensure that the renter has recently booked a listing from the user
		ResultSet rs = qrepository.getAllRenter(username, renter);
		try
		{
			while (rs.next())
			{
				LocalDate bookingDate = rs.getDate("bookings.end_date").toLocalDate();
				//If they have booked within 90 days
				if (ChronoUnit.DAYS.between(bookingDate, LocalDate.parse(date)) + 1 < 90)
				{
					int BookingID = rs.getInt("bookings.id");
					if (repository.addRenterComment(BookingID, comment))
					{
						return true;
					}
					else
					{
						System.out.println("Failed to insert rating");
						return false;
					}
				}
			}
			return false;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public boolean ratingHost(String renter, String username, int rating, String date)
	{
		//Ensure that the renter has recently booked a listing from the user
		ResultSet rs = qrepository.getAllRenter(username, renter);
		try
		{
			while (rs.next())
			{
				LocalDate bookingDate = rs.getDate("bookings.end_date").toLocalDate();
				//If they have booked within 100 days
				if (ChronoUnit.DAYS.between(LocalDate.parse(date), bookingDate) + 1 < 90)
				{
					int BookingID = rs.getInt("bookings.id");
					if (repository.addHostRating(BookingID, rating))
					{
						return true;
					}
					else
					{
						System.out.println("Failed to insert rating");
						return false;
					}
				}
			}
			return false;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public boolean commentHost(String renter, String username, String comment, String date)
	{
		//Ensure that the renter has recently booked a listing from the user
		ResultSet rs = qrepository.getAllRenter(username, renter);
		try
		{
			while (rs.next())
			{
				LocalDate bookingDate = rs.getDate("bookings.end_date").toLocalDate();
				//If they have booked within 100 days
				if (ChronoUnit.DAYS.between(LocalDate.parse(date), bookingDate) + 1 < 90)
				{
					int BookingID = rs.getInt("bookings.id");
					if (repository.addHostComment(BookingID, comment))
					{
						return true;
					}
					else
					{
						System.out.println("Failed to insert rating");
						return false;
					}
				}
			}
			return false;
		}
		catch (SQLException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public void queryListingByCoordinates(float longitude, float latitude, float distance)
	{
		qrepository.queryListingByCoordinates(longitude, latitude, distance);
	}
	
	public void queryListingByPostalCode(String postal_code)
	{
		qrepository.queryListingByPostalCode(postal_code);
	}
	
	public void queryListingByAddress(String address)
	{
		qrepository.queryListingByAddress(address);
	}
	
	public void queryListingByAvailability(String start_date, String end_date)
	{
		qrepository.queryListingByAvailability(start_date, end_date);
	}
	
	public void queryListingByFullSearch(String postal_code, String country, String address, String start_date, String end_date, List<String> listOfAmenities, float min_price, float max_price)
	{
		qrepository.queryListingByFullSearch(postal_code, country, address, start_date, end_date, listOfAmenities, min_price, max_price);
	}
	
	public void addHostCancellation(String username)
	{
		repository.addHostCancellation(username);
	}
	
	public void addRenterCancellation(String username)
	{
		repository.addRenterCancellation(username);
	}
	
	public void reportBookingsByCity(String start_date, String end_date)
	{
		qrepository.reportBookingsByCity(start_date, end_date);
	}
	
	public void reportBookingsByZip(String start_date, String end_date)
	{
		qrepository.reportBookingsByZip(start_date, end_date);
	}
	
	public void reportByCountry()
	{
		qrepository.reportByCountry();
	}
	
	public void reportByCountryCity()
	{
		qrepository.reportByCountryCity();
	}
	
	public void reportByCountryCityZip()
	{
		qrepository.reportByCountryCityZip();
	}
	
	public void reportRankHostByCountry()
	{
		qrepository.reportRankHostByCountry();
	}
	
	public void reportRankHostByCity()
	{
		qrepository.reportRankHostByCity();
	}
	
	public void reportIdenify10PercentHost()
	{
		qrepository.reportIdenify10PercentHost();
	}
	
	public void reportRankRentersByBooking(String start_date, String end_date)
	{
		qrepository.reportRankRentersByBooking(start_date, end_date);
	}
	
	public void reportRankRentersByBookingCity(String start_date, String end_date)
	{
		qrepository.reportRankRentersByBookingCity(start_date, end_date);
	}
	
	public void reportRankCancellations()
	{
		qrepository.reportRankCancellations();
	}
	
	public void reportNounPhrases()
	{
		ResultSet rs = qrepository.reportNounPhrases();
		//Store set of words used and keep count for each listing.
		Map<Integer, Map<String, Integer>> listings = new HashMap<>();
		try
		{
			//Iterate over each entry
			while (rs.next())
			{
				int listingID = Integer.parseInt(rs.getString("Listings.id"));
				if (!listings.containsKey(listingID))
				{
					listings.put(listingID, new HashMap<>());
				}
				Map<String, Integer> nouns = listings.get(listingID);
				//Iterate over the words in the comment
				StringTokenizer comment = new StringTokenizer(rs.getString("CommentHosts.comment"));
				while (comment.hasMoreTokens())
				{
					String word = comment.nextToken();
					//Either increment or set to 1
					nouns.put(word.toLowerCase(), nouns.getOrDefault(word, 0) + 1);
				}
			}
			//Display for each listing the words used
			for (Entry<Integer, Map<String, Integer>> entry : listings.entrySet())
			{
				int lid = entry.getKey();
				System.out.println("-------------");
				System.out.println("Listing ID: " + lid);
				System.out.println("-------------");
				Map<String, Integer> nouns = entry.getValue();
				List<Map.Entry<String, Integer>> entryList = new ArrayList<>(nouns.entrySet());
				entryList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
				int count = 0;
				//get only top 10
				for (Map.Entry<String, Integer> innerEntry : entryList)
				{
					if (count > 10)
					{
						break;
					}
					System.out.println(innerEntry.getKey() + ": " + innerEntry.getValue());
					count++;
				}
			}
			
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	public String hostToolTipAmenities(List<String> amenities)
	{
		String suggested = "";
		if (!amenities.contains("wifi"))
		{
			suggested = suggested + "wifi, ";
		}
		if (!amenities.contains("jacuzzi"))
		{
			suggested = suggested + "jacuzzi, ";
		}
		if (!amenities.contains("parking"))
		{
			suggested = suggested + "parking, ";
		}
		if (!amenities.contains("ac"))
		{
			suggested = suggested + "ac, ";
		}
		if (!amenities.contains("heating"))
		{
			suggested = suggested + "heating, ";
		}
		if (!amenities.contains("washer"))
		{
			suggested = suggested + "washer, ";
		}
		if (!amenities.contains("dryer"))
		{
			suggested = suggested + "dryer, ";
		}
		return suggested.substring(0, suggested.length()-2);
	}
	
	public float hostToolTipPrice(List<String> amenities) 
	{
		if (amenities.size() <= 2)
		{
			return 50;
		}
		if (amenities.size() <= 4)
		{
			return 75;
		}
		if (amenities.size() <= 6)
		{
			return 100;
		}
		if (amenities.size() <= 8)
		{
			return 150;
		}
		return 200;
	}
}
