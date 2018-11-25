import java.util.*;
import java.sql.*;


public class User {
	private String first_name = "";
	private String last_name = "";
	private String address = "";
	private String city = "";
	private String state = "";
	private String zip;
	private String phone = "";
	private String email = "";
	private String userId = "";
	private String pwd = ""; //password
	
	//creadit card info menu
	private String credit_menu = "";
	private String card_type = "";
	private String card_number = "";
	
	

	
	public void accountCreation()
	{
		System.out.println("Welcome to the Online Book Store"); 
		System.out.println("         New Member Registration");
		
		Scanner input = new Scanner(System.in);
		System.out.print("Enter first name: "); 	first_name = input.nextLine(); 
		System.out.print("Enter last name: ");  	last_name = input.nextLine(); 
		System.out.print("Enter street address: "); address = input.nextLine(); 
		System.out.print("Enter city: "); 			city = input.nextLine(); 
		System.out.print("Enter state: ");			state = input.nextLine();
		System.out.print("Enter zip code: ");		zip = input.nextLine();
		System.out.print("Enter phone number: "); 	phone = input.nextLine(); 
		System.out.print("Enter email address: ");	email = input.nextLine(); 
		System.out.print("Enter User ID: ");		userId = input.nextLine(); 
		System.out.print("Enter password: ");		pwd = input.nextLine(); 
		System.out.println("Do you wish to store credit card information? (y/n): "); credit_menu = input.nextLine();
		
		
		//if a customer has a card information or not
		boolean isCard = false;
		
		//if a customer wish to store credit card information
		if(credit_menu.equals('y'))
		{
			//check if entered card information is valid
			boolean isValid;
			isCard = true;
			
			do{
			    System.out.print("Enter type of Credit Card (amex/visa): ");
			    card_type = input.nextLine(); System.out.println();
			    
			    if(card_type.equals("amex") || card_type.equals("visa"))
			    		isValid = true;
			    else
			    		isValid = false;
			    		
			}while(!isValid);
			
			do{
				System.out.print("Enter Credit Card Number: ");
				card_number = input.nextLine(); System.out.println();
				
				int card_length = card_number.length();
				
				if(card_length != 14)
					isValid = false;
				else
					isValid = true;
				
			  }while(!isValid);
		}
		
		//if a customer do not want to store credit card information
		else if(credit_menu.equals('n'))
		{
			isCard = false;
			card_type = null;
			card_number = null;
		}

		input.close();
		
		//inserting data
		userCreation(first_name, last_name, address, city, state, zip, phone, email, userId, pwd, card_type, card_number, isCard);
		
		System.out.println("Name:    		    " + first_name + last_name);
		System.out.println("Address: 		    " + address);
		System.out.println("City:   	        " + city);
		System.out.println("Phone    		    " + phone);
		System.out.println("Email:              " + email);
		System.out.println("UserID:             " + userId);
		System.out.println("Credit CardType:    " + card_type);
		System.out.println("Credit Card Number: " + card_number);
		
	}
	
	public void accountLogin()
	{
		boolean isValid = false;
		Scanner input = new Scanner(System.in);
		while(!isValid)
		{
			System.out.println("Welcome to the Online Book Store"); 
			System.out.println("         Login page");
			System.out.println("Enter the User ID: ");
			userId = input.nextLine(); 
			System.out.print("Enter password: ");
			pwd = input.nextLine(); 
			
			isValid = loginCheck(userId,pwd);		
		}
		input.close();
		System.out.println("You have logined successfully");
		
	}
		
	public boolean loginCheck(String userId, String pwd)
	{
		try
		{
		
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("select password from MEMBERS where userId = ?");
			
			stmt.setString(1, userId);
			
			ResultSet rs = stmt.executeQuery();
			
			//check if a user ID exists in database
			if(rs.next()){
		  	
				while(rs.next())
				{
					String password = rs.getString(1);
			 
					if(password.equals(pwd))
						return true;
					else
					 return false;
				 }
				
				 return true;
			}
			else
			{
				System.out.println("This user id does not exist, please type again");
				return false;
			}
			
	   
	   }
		catch(Exception e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	
	
	//function that creates account and insert user information to the database
	public static void userCreation(String first_name, String last_name, String address, String city, String state, String zip, String phone, String email,
			                 String userId, String pwd, String card_type, String card_number, boolean isCard)
	{
		try{
			Connection con = getConnection();
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO MEMBERS(fname, lname, address, city, state, zip, phone, email, userid, password, creditcardtype, creditcardnumber)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
			
			//inserting data with credit card info
			if(isCard){
				pstmt.setString(1, first_name);
				pstmt.setString(2, last_name);
				pstmt.setString(3, address);
				pstmt.setString(4, city);
				pstmt.setString(5, state);
				pstmt.setString(6, zip);
				pstmt.setString(7, phone);
				pstmt.setString(8, email);
				pstmt.setString(9, userId);
				pstmt.setString(10, pwd);
				pstmt.setString(11, card_type);
				pstmt.setString(12, card_number);
				pstmt.executeUpdate();
			}
			//no credit card info
			else{
				pstmt.setString(1, first_name);
				pstmt.setString(2, last_name);
				pstmt.setString(3, address);
				pstmt.setString(4, city);
				pstmt.setString(5, state);
				pstmt.setString(6, zip);
				pstmt.setString(7, phone);
				pstmt.setString(8, email);
				pstmt.setString(9, userId);
				pstmt.setString(10, pwd);
				pstmt.setString(11, null);
				pstmt.setString(12, null);
				pstmt.executeUpdate();
			}
			con.close();
			System.out.println("You have regiestered successfully\n");
		}
		catch(Exception e){
			System.out.println(e);
		}		
		}
	
	//getting connection to oracle database
	public static Connection getConnection()
	{
			try
			{
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL", "akito_ito","utdfall2018");
				//Statement stmt = con.createStatement();
				//ResultSet rs = stmt.executeQuery("select * from BOOKS"
				return con;
			}
			catch(Exception e)
			{
				System.out.println(e);
				return null;
			}
		}
	
	public String getId()
	{
		return this.userId;
	}

}
