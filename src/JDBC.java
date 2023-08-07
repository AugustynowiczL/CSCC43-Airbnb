import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.Scanner;

public class JDBC {

	private static final String dbClassName = "com.mysql.cj.jdbc.Driver";
	private static final String CONNECTION = "jdbc:mysql://127.0.0.1/mysql?sessionVariables=sql_mode=''";
	
	public static void main(String[] args) throws ClassNotFoundException {
		//Register JDBC driver
		Class.forName(dbClassName);
		//Database credentials
		final String USER = "root";
		final String PASS = "password";
		System.out.println("Connecting to database...");
		
		try {
			//Establish connection
			Connection conn = DriverManager.getConnection(CONNECTION,USER,PASS);
			System.out.println("Successfully connected to MySQL!");
			
			//This drops all tables
			executeSqlScript("src\\scripts\\DBSchemaScript.sql", conn);
			//This creates all tables
			executeSqlScript("src\\scripts\\DBSchemaScriptNoDrop.sql", conn);
			//This inserts data
			executeSqlScript("src\\scripts\\DataInsert.sql", conn);
			
			
			/* Prompt user for signup/signin */
			Scanner scanner = new Scanner(System.in);
			System.out.print("1. SignIn,  2.Signup,  3.Exit : ");
			String type = scanner.nextLine();
			if (type.equals("3"))
			{
				System.out.println("Goodbye!");
				scanner.close();
				conn.close();
				return;
			}
			Authenticator authenticator = new Authenticator(scanner, conn, type);
			while (!authenticator.Authenicate())
			{
				System.out.print("1. SignIn 2. Signup 3. Exit: ");
				type = scanner.nextLine();
				if (type.equals("3"))
				{
					System.out.println("Goodbye!");
					System.out.println("Closing connection...");
					conn.close();
					System.out.println("Success!");
					return;
				}
				authenticator = new Authenticator(scanner, conn, type);
			}
			//User must now be authenticated;
			UserInteractiveTerminal terminal = new UserInteractiveTerminal(authenticator.getUsername(), conn);
			terminal.Entry();
			
			System.out.println("Closing connection...");
			conn.close();
			System.out.println("Success!");
		} catch (SQLException e) {
			System.err.println("Connection error occured!");
		}
	}
	public static void executeSqlScript(String filePath, Connection conn) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			StringBuilder script = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				script.append(line);
				script.append("\n");
			}
			reader.close();
			
			String[] statements = script.toString().split(";");
			
			Statement statement = conn.createStatement();
			for (String sql: statements)
			{
				if (!sql.trim().isEmpty())
				{
					statement.executeUpdate(sql);
				}
			}			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
