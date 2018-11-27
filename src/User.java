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
	
	public User(String firstName, String lastName, String address, String city, String state, int zip, String phone, String email, String userId, String pwd, String cardType, String cardNum)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phone = phone;
		this.email = email;
		this.userId = userId;
		this.pwd = pwd;
		this.cardType= cardType;
		this.cardNum = cardNum;
	}

	
	public String toString()
	{
		return   "Name: " + this.firstName + this.lastName +"\n"
				+"Address: " + this.address +"\n"
				+"City: " + this.city +"\n"
				+"Phone: " + this.phone + "\n"
				+"Email: " + this.email + "\n"
				+"UserID: " + this.userId + "\n"
				+"Credit Card Type: " + this.cardType + "\n"
				+"Credit Card Number: " + this.cardNum + "\n";
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
	
	
	//function that creates account and insert user information to the database
	public void registration()
	{
		try{
			Connection con = getConnection();
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO MEMBERS(fname, lname, address, city, state, zip, phone, email, userid, password, creditcardtype, creditcardnumber)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
			
			pstmt.setString(1, this.firstName);
			pstmt.setString(2, this.lastName);
			pstmt.setString(3, this.address);
			pstmt.setString(4, this.city);
			pstmt.setString(5, this.state);
			pstmt.setInt(6, this.zip);
			pstmt.setString(7, this.phone);
			pstmt.setString(8, this.email);
			pstmt.setString(9, this.userId);
			pstmt.setString(10, this.pwd);
			
			//inserting data with credit card info
			if(!cardType.equals(null))
			{
				pstmt.setString(11, this.cardType);
				pstmt.setString(12, this.cardNum);
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
