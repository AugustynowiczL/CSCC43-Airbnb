import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInteractiveTerminal {

	private final String username;
	private final BusinessLogic business;
	
	public UserInteractiveTerminal(String username, Connection conn)
	{
		this.business = new BusinessLogic(conn);
		this.username = username;
	}
	
	/* Main interactive console loop for user to do actions */
	public void Entry()
	{
		Scanner scanner = new Scanner(System.in);
		/* Main loop */
		System.out.print("1. Operation,  2. Query,  3. Report, 4. Exit: ");
		String option = scanner.nextLine();
		while (!option.equals("4"))
		{
			System.out.println(option);
			/* List possible operations for user to do */
			if (option.equals("1"))
			{
				UserOperation(scanner);
			}
			/* List possible queries for user to do */
			else if (option.equals("2"))
			{
				UserQuery(scanner);
			}
			else
			{
				Report(scanner);
			}
			System.out.print("1. Operation,  2. Query,  3. Report, 4. Exit: ");
			option = scanner.nextLine();
		}
		scanner.close();
	}
	
	@SuppressWarnings("unused")
	private void UserOperation(Scanner scanner)
	{
		System.out.print("What operation do you want to do: ");
		String operation = scanner.nextLine();
		
		switch (operation)
		{
			//delete user profile
		
			case "0":
				if (business.deleteProfile(username))
				{
					System.out.println("Successfully deleted account");
					System.exit(0);
				}
				else
				{
					System.out.println("Failed to delete account");
				}
				break;
			/* Create user listing */
			case "1":
				System.out.print("Enter type of listing: ");
				int type = Integer.parseInt(scanner.nextLine());
				System.out.print("Enter longitude: ");
				double longitude = Float.parseFloat(scanner.nextLine());
				System.out.print("Enter latitude: ");
				double latitude = Float.parseFloat(scanner.nextLine());
				System.out.print("Enter country: ");
				String country = scanner.nextLine();
				System.out.print("Enter city: ");
				String city = scanner.nextLine();
				System.out.print("Enter postalcode: ");
				String postal_code = scanner.nextLine();
				System.out.print("Enter the address: ");
				String address = scanner.nextLine();
				System.out.print("Would you like to add some extra amenities?(y/n): ");
				List<String> amenities = new ArrayList<String>();
				Boolean check = scanner.nextLine().equals("y");
				while (check)
				{
					System.out.print("Enter name of amenity: ");
					String amenity = scanner.nextLine();
					amenities.add(amenity);
					System.out.print("Would you like to add another amenity?(y/n): ");
					check = scanner.nextLine().equals("y");
				}
				System.out.println("We suggest you add the following amenities : " + business.hostToolTipAmenities(amenities));
				System.out.println("Based on this listing we suggest the price of the listing to be: " + business.hostToolTipPrice(amenities));
				System.out.print("Enter price for you listing: ");
				double price = Float.parseFloat(scanner.nextLine());
				boolean success = business.createListing(username, type, latitude, longitude, country, postal_code, city, address, price);
				if (success)
				{
					System.out.println("Successfully created listing!");
					for (String item : amenities)
					{
						business.addListingAmenity(username, longitude, latitude, item);
					}
				}
				else 
				{
					System.out.println("Failed to create listing");
				}
				break;
			/* Add amenity to an existing listing */
			case "2": 
				System.out.print("Enter longitude of the listing: ");
				longitude = Float.parseFloat(scanner.nextLine());
				System.out.print("Enter latitude of the listing: ");
				latitude = Float.parseFloat(scanner.nextLine());
				System.out.print("Name of amenity: ");
				String amenity = scanner.nextLine();
				success = business.addListingAmenity(username, longitude, latitude, amenity);
				if (success)
				{
					System.out.println("Succesfully added amenity");
				}
				else 
				{
					System.out.println("Failed to add amenity");
				}
				break;
			/* Book a listing */
			case "3":
				/* Check if the user has a payment method already in the system */
				boolean payment = business.verifyUserPayment(username);
				success = true;
				if (!payment)
				{
					System.out.println("Seems like you do not have a payment method in the application. You are required to add one!");
					System.out.print("Enter your credit card number: ");
					int creditcardnum = Integer.parseInt(scanner.nextLine());
					success = business.addUserPaymentInformation(username, creditcardnum);
					if (success)
					{
						System.out.println("Your payment has been added to the system");
					}
					else
					{
						System.out.println("Failed to add payment method");
					}
				}
				if (success)
				{
					System.out.print("Enter the name of the user who posted the listing: ");
					String posterUser = scanner.nextLine();
					System.out.print("Enter longitude of the listing: ");
					longitude = Float.parseFloat(scanner.nextLine());
					System.out.print("Enter latitude of the listing: ");
					latitude = Float.parseFloat(scanner.nextLine());
					System.out.print("Enter start date (yyyy-mm-dd): ");
					String start_date = scanner.nextLine();
					System.out.print("Enter end date (yyyy-mm-dd):");
					String end_date = scanner.nextLine();
					Listing listing = business.getUserListing(posterUser, latitude, longitude);
					success = business.addBooking(username, listing.getUuid(), start_date, end_date);
					if (success)
					{
						System.out.println("You have successfully booked");
					}
					else
					{
						System.out.println("Booking failed");
					}
				}
				break;
			/* Host change availability */
			case "4":
				System.out.print("Enter longitude of the listing: ");
				longitude = Float.parseFloat(scanner.nextLine());
				System.out.print("Enter latitude of the listing: ");
				latitude = Float.parseFloat(scanner.nextLine());
				System.out.print("Enter date you want to make unavailable (yyyy-mm-dd): ");
				String start_date = scanner.nextLine();
				Listing listing = business.getUserListing(username, latitude, longitude);
				// Ensure date is not booked for this listing
				if (listing == null)
				{
					System.out.println("You do not have access to this listing");
				}
				else 
				{
					boolean avail = business.isListingAvailable(listing.getUuid(), start_date, start_date);
					if (avail)
					{
						if (business.makeUnavailable(listing.getUuid(), start_date))
						{
							System.out.println("Successfully made unavailable");
						}
						else
						{
							System.out.println("Failed to make unavailable");
						}
					}
					else
					{
						System.out.println("Cannot change availability since the listing is not available");
					}
				}
				break;
			/* Host change price of date */
			case "5":
				System.out.print("Enter longitude of the listing: ");
				longitude = Float.parseFloat(scanner.nextLine());
				System.out.print("Enter latitude of the listing: ");
				latitude = Float.parseFloat(scanner.nextLine());
				System.out.print("Enter date you want to change price (yyyy-mm-dd): ");
				String start_date1 = scanner.nextLine();
				System.out.print("Enter the price: ");
				float price1 = Float.parseFloat(scanner.nextLine());
				Listing listing1 = business.getUserListing(username, latitude, longitude);
				// Ensure date is not booked for this listing
				if (listing1 == null)
				{
					System.out.println("You do not have access to this listing");
				}
				else 
				{
					boolean avail = business.isListingAvailable(listing1.getUuid(), start_date1, start_date1);
					if (avail)
					{
						success = business.changePrice(listing1.getUuid(), start_date1, price1);
						if (success)
						{
							System.out.println("Successfully changed price");
						}
						else
						{
							System.out.println("Failed to change price");
						}
					}
					else
					{
						System.out.println("Cannot change availability since the listing is not available");
					}
				}
				break;
			/* User cancel booking */
			case "6":
				System.out.print("Enter the name of the user who posted the listing: ");
				String posterUser = scanner.nextLine();
				System.out.print("Enter longitude of the listing: ");
				longitude = Float.parseFloat(scanner.nextLine());
				System.out.print("Enter latitude of the listing: ");
				latitude = Float.parseFloat(scanner.nextLine());
				System.out.print("Enter start date (yyyy-mm-dd): ");
				start_date = scanner.nextLine();
				System.out.print("Enter end date (yyyy-mm-dd):");
				String end_date = scanner.nextLine();
				listing = business.getUserListing(posterUser, latitude, longitude);
				if (listing == null)
				{
					System.out.println("This listing does not exist");
				}
				else
				{
					// Find the users bookings
					success = business.deleteBooking(username, listing.getUuid(), start_date, end_date);
					if (success)
					{
						business.addRenterCancellation(username);
						System.out.println("Successfully deleted booking");
					}
					else 
					{
						System.out.println("Failed to delete booking");
					}
				}
				break;
			//host cancel booking
			case "7":
				System.out.print("Enter the name of the booking user: ");
				String bookingUser = scanner.nextLine();
				System.out.print("Enter longitude of the listing: ");
				longitude = Float.parseFloat(scanner.nextLine());
				System.out.print("Enter latitude of the listing: ");
				latitude = Float.parseFloat(scanner.nextLine());
				System.out.print("Enter start date (yyyy-mm-dd): ");
				start_date = scanner.nextLine();
				System.out.print("Enter end date (yyyy-mm-dd):");
				end_date = scanner.nextLine();
				listing = business.getUserListing(username, latitude, longitude);
				if (listing == null)
				{
					System.out.println("You do not have access to this listing");
				}
				else
				{
					if (business.deleteBooking(bookingUser, listing.getUuid(), start_date, end_date))
					{
						business.addHostCancellation(username);
						System.out.println("Successfully cancelled booking");
					}
					else
					{
						System.out.println("Failed to cancel booking");
					}
				}
				break;
			/* remove a listing */
			case "8":
				System.out.print("Enter longitude of the listing: ");
				longitude = Float.parseFloat(scanner.nextLine());
				System.out.print("Enter latitude of the listing: ");
				latitude = Float.parseFloat(scanner.nextLine());
				if (business.deleteListing(username, latitude, longitude))
				{
					System.out.println("Successfully deleted listing");
				}
				else
				{
					System.out.println("Failed to delete listing");
				}
				break;
			//Host rates a renter
			case "9":
				System.out.print("Enter the username of the renter: ");
				bookingUser = scanner.nextLine();
				System.out.print("Enter the current date(yy-mm-dd): ");
				String current_date = scanner.nextLine();
				System.out.print("Enter the rating you want to give: ");
				int rating = Integer.parseInt(scanner.nextLine());
				if (business.ratingRenter(username, bookingUser, rating, current_date))
				{
					System.out.println("Successfully rated the renter");
				}
				else
				{
					System.out.println("Failed to rate the renter");
				}
				break;
			//Host comments a renter
			case "10":
				System.out.print("Enter the username of the renter: ");
				bookingUser = scanner.nextLine();
				System.out.print("Enter the current date(yy-mm-dd): ");
				current_date = scanner.nextLine();
				System.out.print("Enter the comment you want to give: ");
				String comment = scanner.nextLine();
				if (business.commentRenter(username, bookingUser, comment, current_date))
				{
					System.out.println("Successfully commented the renter");
				}
				else
				{
					System.out.println("Failed to rate the renter");
				}
				break;
			//Renter rates host
			case "11":
				System.out.print("Enter username of the host: ");
				String hostUser = scanner.nextLine();
				System.out.print("Enter the current date(yy-mm-dd): ");
				current_date = scanner.nextLine();
				System.out.print("Enter the rating you want to give: ");
				rating = Integer.parseInt(scanner.nextLine());
				if (business.ratingHost(username, hostUser, rating, current_date))
				{
					System.out.println("Successfully rated the host");
				}
				else
				{
					System.out.println("Failed to rate the host");
				}
				break;
				//Renter rates host
			case "12":
				System.out.print("Enter username of the host: ");
				hostUser = scanner.nextLine();
				System.out.println("Enter the current date(yy-mm-dd): ");
				current_date = scanner.nextLine();
				System.out.print("Enter the comment you want to give: ");
				comment = scanner.nextLine();
				if (business.commentHost(username, hostUser, comment, current_date))
				{
					System.out.println("Successfully commented the host");
				}
				else
				{
					System.out.println("Failed to comment the host");
				}
				break;
			default:
				return;
		}
		
	}
	
	private void UserQuery(Scanner scanner)
	{
		System.out.print("What operation do you want to do: ");
		String operation = scanner.nextLine();
		
		switch (operation)
		{
		//search by longitude latitude
		case "0":
			System.out.print("Enter longitude: ");
			float longitude = Float.parseFloat(scanner.nextLine());
			System.out.print("Enter latitude: ");
			float latitude = Float.parseFloat(scanner.nextLine());
			System.out.print("Specify distance(-1 for default): ");
			float distance = Float.parseFloat(scanner.nextLine());
			business.queryListingByCoordinates(longitude, latitude, distance);
			break;
		// search by adjacent postal code
		case "1":
			System.out.print("Enter postal code (A0A0A0): ");
			String postal_code = scanner.nextLine();
			business.queryListingByPostalCode(postal_code);
			break;
		// search by exact address
		case "2":
			System.out.print("Enter the address: ");
			String address = scanner.nextLine();
			business.queryListingByAddress(address);
			break;
		//search by date range
		case "3":
			System.out.print("Enter starting date(yyyy-mm-dd): ");
			String start_date = scanner.nextLine();
			System.out.print("Enter the end date(yyyy-mm-dd): ");
			String end_date = scanner.nextLine();
			business.queryListingByAvailability(start_date, end_date);
			break;
		//search fully
		case "4":
			 System.out.print("Do you want to filter by postal code?(y/n): ");
			 Boolean filter = scanner.nextLine().toLowerCase().equals("y");
			 postal_code = "";
			 if (filter)
			 {
				 System.out.print("Enter the postal code: ");
				 postal_code = scanner.nextLine();
			 }
			 String country = "";
			 System.out.print("Do you want to filter by country? (y/n): ");
			 filter = scanner.nextLine().toLowerCase().equals("y");
			 if (filter)
			 {
				 System.out.print("Enter the name of the country: ");
				 country = scanner.nextLine();
			 }
			 address = "";
			 System.out.print("Do you want to filter by address?(y/n): ");
			 filter = scanner.nextLine().toLowerCase().equals("y");
			 if (filter)
			 {
				 System.out.print("Enter the address: ");
				 address = scanner.nextLine();
			 }
			 System.out.print("Do you want to filter by date?(y/n): ");
			 filter = scanner.nextLine().toLowerCase().equals("y");
			 start_date = "";
			 end_date = "";
			 if (filter)
			 {
					System.out.print("Enter starting date(yyyy-mm-dd): ");
					start_date = scanner.nextLine();
					System.out.print("Enter the end date(yyyy-mm-dd): ");
					end_date = scanner.nextLine();			 
			 }
			 System.out.print("Do you want to filter by amenities?(y/n): ");
			 filter = scanner.nextLine().toLowerCase().equals("y");
			 List<String> listOfAmenities = new ArrayList<>();
			 if (filter)
			 {
				 while (true)
				 {
					 System.out.print("Enter the name of the amenity: ");
					 listOfAmenities.add(scanner.nextLine());
					 System.out.print("Do you want to enter another amenity?(y/n): ");
					 filter = scanner.nextLine().toLowerCase().equals("y");
					 if (!filter)
					 {
						 break;
					 }
				 }
			 }
			 float min_price = -1;
			 float max_price = -1;
			 System.out.print("Do you want to filter by price?(y/n): ");
			 filter = scanner.nextLine().toLowerCase().equals("y");
			 if (filter) 
			 {
				 System.out.print("Enter minimum price: ");
				 min_price = Float.parseFloat(scanner.nextLine());
				 System.out.print("Enter your max price: ");
				 max_price = Float.parseFloat(scanner.nextLine());
			 }
			 business.queryListingByFullSearch(postal_code, country, address, start_date, end_date, listOfAmenities, min_price, max_price);
			 break;
		}		
	}
	
	private void Report(Scanner scanner)
	{
		System.out.print("What report do you want to do: ");
		String operation = scanner.nextLine();
		
		switch (operation)
		{
			case "0":
				//total bookings in date range by city
				System.out.print("Enter starting date(yyyy-mm-dd): ");
				String start_date = scanner.nextLine();
				System.out.print("Enter the end date(yyyy-mm-dd): ");
				String end_date = scanner.nextLine();
				business.reportBookingsByCity(start_date, end_date);
				break;
			//total bookings in date range by zip by city
			case "1":
				System.out.print("Enter starting date(yyyy-mm-dd): ");
				start_date = scanner.nextLine();
				System.out.print("Enter the end date(yyyy-mm-dd): ");
				end_date = scanner.nextLine();
				business.reportBookingsByZip(start_date, end_date);
				break;
			//total listings per country
			case "2":
				business.reportByCountry();
				break;
			//total listings by country and city
			case "3":
				business.reportByCountryCity();
				break;
			//total listings by country city zip
			case "4":
				business.reportByCountryCityZip();
				break;
			//rank hosts by total num lisitng per country
			case "5":
				business.reportRankHostByCountry();
				break;
			//rank hosts by total num listing per city
			case "6":
				business.reportRankHostByCity();
				break;
			//identify hosts with more than 10%
			case "7":
				business.reportIdenify10PercentHost();
				break;
			//rank renters by num bookings by date range
			case "8":
				System.out.print("Enter starting date(yyyy-mm-dd): ");
				start_date = scanner.nextLine();
				System.out.print("Enter the end date(yyyy-mm-dd): ");
				end_date = scanner.nextLine();
				business.reportRankRentersByBooking(start_date, end_date);
				break;
			//rank renters by num bookings by city by date range > 2
			case "9":
				System.out.print("Enter starting date(yyyy-mm-dd): ");
				start_date = scanner.nextLine();
				System.out.print("Enter the end date(yyyy-mm-dd): ");
				end_date = scanner.nextLine();
				business.reportRankRentersByBookingCity(start_date, end_date);
				break;
			//rank renters and hosts by largest num of cancellations
			case "10":
				business.reportRankCancellations();
				break;
			//report noun phrases per listing
			case "11":
				business.reportNounPhrases();
				break;
		}
	}
}
