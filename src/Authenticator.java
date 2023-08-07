import java.sql.Connection;
import java.util.Scanner;

public class Authenticator {

	private final BusinessLogic business;
	private final String username;
	private final String password;
	private final String type;
	private final Scanner scanner;
	
	public Authenticator(Scanner scanner, Connection conn, String type)
	{
		this.scanner = scanner;
		this.business = new BusinessLogic(conn);
		this.type = type;
		System.out.print("Please enter your username: ");
		username = scanner.nextLine();
		System.out.print("Please enter your password: ");
		password = scanner.nextLine();
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public boolean Authenicate()
	{
		/* Sign in Logic */
		if (type.equals("1"))
		{
			if (business.verifyUserProfile(username, password))
			{
				System.out.println("Signed in!");
				return true;
			}
			else
			{
				System.out.println("Wrong password");
				return false;
			}
		}
		/* Sign up logic */
		else if (type.equals("2"))
		{
			System.out.print("What is your name: ");
			String name = scanner.nextLine();
			System.out.print("What is your address: ");
			String address = scanner.nextLine();
			System.out.print("What is your date of birth(yyyy-mm-dd): ");
			String dob = scanner.nextLine();
			System.out.print("What is your occupation: ");
			String occupation = scanner.nextLine();
			System.out.print("What is your sin number: ");
			int sin = Integer.parseInt(scanner.nextLine());
			/* Ensure that username doesn't already exists*/
			if (business.createUserProfile(name, password, name, address, dob, occupation, sin))
			{
				System.out.println("You have created your account!");
				return true;
			}
			else
			{
				System.out.println("Something went wrong!");
				return false;
			}
			
		}
		else 
		{
			System.out.println("Not a valid option");
			return false;
		}
	}
}
