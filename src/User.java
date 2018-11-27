import java.util.*;
import java.sql.*;


public class User {
	//user general information
	private String firstName = "";
	private String lastName = "";
	private String address = "";
	private String city = "";
	private String state = "";
	private int zip;
	private String phone = "";
	private String email = "";
	private String userId = "";
	private String pwd = ""; //password
	
	//credit card info menu
	private String creditMenu = "";
	private String cardType = "";
	private String cardNum = "";
	

	//creates account 
	public void accountCreation()
	{
		System.out.println("Welcome to the Online Book Store"); 
		System.out.println("         New Member Registration");
		
		Scanner input = new Scanner(System.in);
		System.out.print("Enter first name: "); 	firstName = input.nextLine(); 
		System.out.print("Enter last name: ");  	lastName = input.nextLine(); 
		System.out.print("Enter street address: "); address = input.nextLine(); 
		System.out.print("Enter city: "); 			city = input.nextLine(); 
		System.out.print("Enter state: ");			state = input.nextLine();
		System.out.print("Enter zip code: ");		zip = input.nextInt(); input.nextLine();
		System.out.print("Enter phone number: "); 	phone = input.nextLine(); 
		System.out.print("Enter email address: ");	email = input.nextLine(); 
		System.out.print("Enter User ID: ");		userId = input.nextLine(); 
		System.out.print("Enter password: ");		pwd = input.nextLine(); 
		System.out.println("Do you wish to store credit card information? (Y/N): "); creditMenu = input.nextLine();
		
		
		//if a customer has a card information or not
		boolean isCard = false;
		
		//if a customer wish to store credit card information
		if(creditMenu.equals("Y") || creditMenu.equals("y"))
		{
			boolean isValid = false;
			while(!isValid)
			{
				System.out.print("Enter type of Credit Card (amex/visa): "); cardType = input.nextLine(); 
				System.out.print("Enter Credit Card Number: "); cardNum = input.nextLine();
				//check if entered card information is valid
				isValid = isCardValid(cardType, cardNum);
			}
			
		}
		
		//if a customer do not want to store credit card information
		else if(creditMenu.equals("n"))
		{
			isCard = false;
			cardType = null;
			cardNum = null;
		}
		
		//inserting data
		userCreation(firstName, lastName, address, city, state, zip, phone, email, userId, pwd, cardType, cardNum, isCard);
		
		System.out.println("Name:    		    " + firstName + " " + lastName);
		System.out.println("Address: 		    " + address);
		System.out.println("City:   	        " + city);
		System.out.println("Phone    		    " + phone);
		System.out.println("Email:              " + email);
		System.out.println("UserID:             " + userId);
		System.out.println("Credit CardType:    " + cardType);
		System.out.println("Credit Card Number: " + cardNum);
		
	}
	
	//logins account
	public void accountLogin()
	{
		boolean isValid = false;
		Scanner input = new Scanner(System.in);
		while(!isValid)
		{
			System.out.println("Welcome to the Online Book Store"); 
			System.out.println("         Login page");
			System.out.print("Enter the User ID: ");
			userId = input.nextLine(); 
			System.out.print("Enter password: ");
			pwd = input.nextLine(); 
			
			isValid = loginCheck(userId,pwd);		
		}
		
		System.out.println("You have logined successfully");
		//if login successful get all attributes for this user object
		try
		{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM MEMBERS WHERE USERID = '" + userId + "'");
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			{
				this.firstName = rs.getString(1);
				this.lastName = rs.getString(2);
				this.address = rs.getString(3);
				this.city = rs.getString(4);
				this.state = rs.getString(5);
				this.zip = rs.getInt(6);
				this.phone = rs.getString(7);
				this.email = rs.getString(8);
				this.cardType = rs.getString(11);
				this.cardNum = rs.getString(12);
			}
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	//this method checks if login was successful or not
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
	

	//This method check if a user has a card information 
	public boolean checkIfCard()
	{
				try
				{
					Connection con = getConnection();
					PreparedStatement stmt = con.prepareStatement("SELECT CREDITCARDNUMBER FROM MEMBERS WHERE USERID = '" + userId + "'");
					ResultSet rs = stmt.executeQuery();
					while(rs.next())
					{
						String cardNum = rs.getString(1);
						if(cardNum.equals(null))
							     return false;
					}
					return true;
				}
				catch(Exception e)
				{
					System.out.println(e);
					 return false;
				}
	}
	
	//this method check if entered card information is valid of not
	public boolean isCardValid(String cardType, String cardNum)
	{
		  if(cardType.equals("amex") || cardType.equals("visa"))
		  {
				if(cardNum.length() != 14)
				{
					System.out.println("Card number length has to be 14");
					return false;
				}
				else
					return true;
		  }
		  else
		  {
			  if(cardNum.length() != 14)
				{
				   System.out.println("Credit card type must be amex or visa");
				   System.out.println("Card number length has to be 14");
				   return false;
				}
			  else
			  {
				  System.out.println("Credit card type must be amex or visa");
				  return false;
			  }
		  }
	}
	
	//function that creates account and insert user information to the database
	public static void userCreation(String firstName, String lastName, String address, String city, String state, int zip, String phone, String email,
			                 String userId, String pwd, String cardType, String cardNum, boolean isCard)
	{
		try{
			Connection con = getConnection();
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO MEMBERS(fname, lname, address, city, state, zip, phone, email, userid, password, creditcardtype, creditcardnumber)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
			
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			pstmt.setString(3, address);
			pstmt.setString(4, city);
			pstmt.setString(5, state);
			pstmt.setInt(6, zip);
			pstmt.setString(7, phone);
			pstmt.setString(8, email);
			pstmt.setString(9, userId);
			pstmt.setString(10, pwd);
			
			//inserting data with credit card info
			if(isCard)
			{
				pstmt.setString(11, cardType);
				pstmt.setString(12, cardNum);
				pstmt.executeUpdate();
			}
			//no credit card info
			else
			{
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
	
	public void updateCard()
	{
		try
		{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE MEMBERS SET CREDITCARDTYPE = '" + this.cardType + "', CREDITCARDNUMBER = '" + this.cardNum +"'");
			stmt.executeUpdate();
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}
	public void updateAddress()
	{
		try
		{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE MEMBERS SET ADDRESS = '" + this.address +"', CITY = '" + this.city +"', STATE = '" + this.state +"', ZIP = " + this.zip 
															+ "WHERE USERID = '" + this.userId +"'");
			stmt.executeUpdate();
			con.close();
		}
		catch(Exception e)
		{
		}
	}
	
	public void setCard(String cardType, String cardNum)
	{
		this.cardType = cardType;
		this.cardNum = cardNum;
	}
	
	public void setAddress(String address, String city, String state, int zip)
	{
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	public void generateInvoice(int orderNum, String firstName, String lastName, String address, String city, String state, int zip, boolean isNewAddress)
	{
		System.out.println("       Invoice for Order no. " + orderNum);
		System.out.printf("%-30s %-30s", "Shipping Address", "Billing Adress");
		if(isNewAddress)
		{
			System.out.printf("%-30s %-30s", "Name: " + firstName + " " + lastName, "Name: " + this.firstName + this.lastName);
			System.out.printf("%-30s %-30s", "Address: " + address, "Address: " + this.address);
			System.out.printf("%-30s %-30s", city, this.city);
			System.out.printf("%-30s %-30s", state + " " + zip, this.state + " " + this.zip);
			
		}
		else
		{
			System.out.printf("%-30s %-30s", "Name: " + this.firstName + " " + this.lastName, "Name: " + this.firstName + this.lastName);
			System.out.printf("%-30s %-30s", "Address: " + this.address, "Address: " + this.address);
			System.out.printf("%-30s %-30s", this.city, this.city);
			System.out.printf("%-30s %-30s", this.state + " " + this.zip, this.state + " " + this.zip);
		}
		showCartContents();
	}
	

	public void showCartContents()
	{
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT c.ISBN, b.TITLE, b.PRICE, c.QTY FROM CART c, BOOKS b WHERE c.ISBN = b.ISBN AND USERID = ?");
			stmt.setString(1, this.userId);
			ResultSet rs = stmt.executeQuery();
			System.out.println("Current Cart Contents:");
			System.out.printf("%-12s %-165s %-5s %-3s %-7s", "ISBN", "Title", "$", "Qty", "Total");
			System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------"
					+ "--------------------------------------");
			double total = 0;
			double allTotal = 0;
			while(rs.next())
			{
				System.out.printf("%-12s",  rs.getString(1)); //ISBN
				System.out.printf("%-165s", rs.getString(2) ); //TITLE
				System.out.printf("%5.2f",   rs.getDouble(3)); //PRICE
				System.out.printf("%5d",   rs.getInt(4)); //QTY
				total = (rs.getDouble(3)) * (rs.getInt(4));
				System.out.printf("%8.2f", total); //Total
				System.out.println(); //new line
				allTotal = allTotal + total;
			}
			System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------"
					+ "--------------------------------------");
			System.out.print("Total =");
			System.out.printf("%182s %1.2f", "$", allTotal);
			System.out.println();
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}
}
