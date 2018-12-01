import java.util.*;
import java.sql.*;
import java.time.*;

public class User {
	//user general information
	private String firstName = "", lastName = "", address = "", city = "", state = "", phone = "", email = "", userId = "", pwd = "";
	private int zip;
	//credit card info menu
	private String creditMenu = "";
	private String cardType = "", cardNum = "";
	
	public User(String firstName, String lastName, String address, String city, String state, int zip, String phone, String email, String userId, String pwd, String cardType, String cardNum)
	{
		this.firstName = firstName; this.lastName = lastName;
		this.address = address; this.city = city; this.state = state; this.zip = zip;
		this.phone = phone; this.email = email;
		this.userId = userId; this.pwd = pwd;
		this.cardType= cardType; this.cardNum = cardNum;
	}

	public String toString(){
		return   "Name: " + this.firstName + " " + this.lastName +"\n"
				+"Address: " + this.address +"\n"
				+"City: " + this.city +"\n"
				+"State: " + this.state +"\n"
				+"Zip: " + this.zip + "\n"
				+"Phone: " + this.phone + "\n"
				+"Email: " + this.email + "\n"
				+"UserID: " + this.userId + "\n"
				+"Credit Card Type: " + this.cardType + "\n"
				+"Credit Card Number: " + this.cardNum + "\n";
	}
	
	//checks if a user has a card information 
	public boolean checkIfCard(){
				try{
					Connection con = getConnection();
					PreparedStatement stmt = con.prepareStatement("SELECT CREDITCARDNUMBER FROM MEMBERS WHERE USERID = '" + this.userId + "'");
					ResultSet rs = stmt.executeQuery();
					while(rs.next()){
						String cardNum = rs.getString(1);
						if(cardNum.equals(null))
							     return false;
					}
					return true;
				}
				catch(Exception e){
					System.out.println(e);
					 return false;
				}
	}
	
	//function that creates account and insert user information to the database
	public void registration(){
		try{
			Connection con = getConnection();
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO MEMBERS VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
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
			if(!cardType.equals(null)){
				pstmt.setString(11, this.cardType);
				pstmt.setString(12, this.cardNum);
				pstmt.executeUpdate();
			}
			//no credit card info
			else{
				pstmt.setString(11, null);
				pstmt.setString(12, null);
				this.cardType = "Not Registered";
				this.cardNum = "Not Registered";
				pstmt.executeUpdate();
			}
			con.close();
			System.out.println("You have regiestered successfully\n");
		}
		catch(Exception e){}		
		}
	
	public String getId(){
		return this.userId;
	}
	
	public void updateName(String firstName, String lastName)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE MEMBERS SET FNAME = '" + firstName + "', LNAME = '" + lastName + 
														  "' WHERE USERID = '" + this.userId + "'");
			stmt.executeUpdate();
		}
		catch(Exception e){ System.out.println(e); }
	}
	
	public void updateAddress(String address, String city, String state, int zip){
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE MEMBERS SET ADDRESS = '" + address + "', CITY = '" + city + 
															"', STATE = '" + state + "', ZIP = " + zip + " WHERE USERID = '" + this.userId + "'");
			stmt.executeUpdate();
		}
		catch(Exception e){	System.out.println(e);}
	}
	
	public void updatePhone(String phone)
	{
		this.phone = phone;
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE MEMBERS SET PHONE = '" + phone + "' WHERE USERID = '" + this.userId +"'");
			stmt.executeUpdate();
		}
		catch(Exception e){System.out.println(e);}
	}
	
	public void updateEmail(String email){
		this.email = email;
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE MEMBERS SET EMAIL = '" + email + "' WHERE USERID = '" + this.userId + "'");
			stmt.executeUpdate();
		}
		catch(Exception e){System.out.println(e);}
	}
	
	public void updatePwd(String pwd){
		this.pwd = pwd;
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE MEMBERS SET PASSWORD = '" + pwd + "' WHERE USERID = '" + this.userId + "'");
			stmt.executeUpdate();
		}
		catch(Exception e){ System.out.println(e);}
	}
	
	public void updateCard(String cardType, String cardNum){
		this.cardType = cardType;
		this.cardNum = cardNum;
		try	{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE MEMBERS SET CREDITCARDTYPE = '" + cardType + "', CREDITCARDNUMBER = '" + cardNum + "'"
														+ "WHERE USERID = '" + this.userId + "'");
			stmt.executeUpdate();
			con.close();
		}
		catch(Exception e){ System.out.println(e); }
	}
		
	public void setAddress(String address, String city, String state, int zip){
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	public void generateInvoice(int orderNum, String firstName, String lastName, String address, String city, String state, int zip, boolean isNewAddress){
		System.out.println("\n       Invoice for Order no. " + orderNum);
		System.out.printf("%-30s %-30s %n", "Shipping Address", "Billing Adress");
		if(isNewAddress){
			System.out.printf("%-30s %-30s %n", "Name: " + firstName + " " + lastName, "Name: " + this.firstName + this.lastName);
			System.out.printf("%-30s %-30s %n", "Address: " + address, "Address: " + this.address);
			System.out.printf("%-30s %-30s %n", city, this.city);
			System.out.printf("%-30s %-30s %n", state + " " + zip, this.state + " " + this.zip);
		}
		else{
			System.out.printf("%-30s %-30s %n", "Name: " + this.firstName + " " + this.lastName, "Name: " + this.firstName + " " + this.lastName);
			System.out.printf("%-30s %-30s %n", "Address: " + this.address, "Address: " + this.address);
			System.out.printf("%-30s %-30s %n", this.city, this.city);
			System.out.printf("%-30s %-30s %n", this.state + " " + this.zip, this.state + " " + this.zip);
		}
	}
	
	//insert to ORDER table
	public int insertOrder(String address, String city, String state, int zip, boolean isNewAddress){
		try{
			int orderNum = 1;
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO ORDERS VALUES(?,?,?,?,?,?,?,?)");
			PreparedStatement stmt2 = con.prepareStatement("SELECT MAX(ONO) FROM ORDERS");
			ResultSet rs = stmt2.executeQuery();
			if(rs.next()){
				orderNum = rs.getInt(1) + 1;
			}
			stmt.setString(1, this.userId);
			stmt.setInt(2, orderNum);
			stmt.setDate(3, getDate());
			stmt.setDate(4, getDate());
			if(isNewAddress){
				stmt.setString(5, address);
				stmt.setString(6, city);
				stmt.setString(7, state);
				stmt.setInt(8, zip);
			}
			else{
				stmt.setString(5, this.address);
				stmt.setString(6, this.city);
				stmt.setString(7, this.state);
				stmt.setInt(8, this.zip);
			}
			stmt.executeUpdate();
			con.close();
			//inserting to order detail table
			insertOdetails(orderNum);
			//delete items from cart
			return orderNum;
		}
		catch(Exception e){	System.out.println(e); return 0;}
	}
	
	//get current date
	public static java.sql.Date getDate(){
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
	}
	
	//insert to ODETAILS table
	public void insertOdetails(int orderNum){
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT c.ISBN, c.QTY, b.PRICE from CART c, BOOKS b"
														+ " WHERE b.ISBN = c.ISBN AND USERID = '" + this.userId + "'");
			PreparedStatement insert = con.prepareStatement("INSERT INTO ODETAILS VALUES(?,?,?,?)");
			ResultSet rs = stmt.executeQuery();
			double total = 0;
			while(rs.next()){
				insert.setInt(1, orderNum);
				insert.setString(2, rs.getString(1));//isbn
				insert.setInt(3, rs.getInt(2));//quantity
				total = (rs.getInt(2)) * (rs.getDouble(3));
				insert.setDouble(4, total);//price
				insert.executeUpdate();
			}
			con.close();
		}
		catch(Exception e){System.out.println(e);}
	}
	
	public void showOrderStatus(){
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT ONO, RECEIVED, SHIPPED FROM ORDERS WHERE USERID = '" + this.userId + "'");
			ResultSet rs = stmt.executeQuery();
			System.out.println("\nOrder(s) placed by " + this.firstName + " " + this.lastName);
			System.out.println("---------------------------------------------------------------------");
			System.out.printf("%-7s %-15s %-15s %n", "ORDER NO", "RECEIVED DATE", "SHIPPED DATE");
			System.out.println("---------------------------------------------------------------------");
			while(rs.next()){
				System.out.printf("%-10s %-15s %-15s %n", rs.getInt(1), rs.getDate(2), rs.getDate(3));
			}
			System.out.println("---------------------------------------------------------------------");
			con.close();
		}
		catch(Exception e){System.out.println(e);}
	}
	
	public void showOrder(int orderNum){
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT o.ISBN, b.TITLE, o.PRICE, o.QTY FROM ODETAILS o, BOOKS b"
														+ " WHERE ONO = " + orderNum +  " AND o.ISBN = b.ISBN");
			ResultSet rs = stmt.executeQuery();
			double total = 0, allTotal = 0;
			System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------"
					+ "--------------------------------------");
			System.out.printf("%-12s %-165s %-5s %-3s %-7s", "ISBN", "Title", "$", "Qty", "Total");
			System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------"
					+ "--------------------------------------");
			while(rs.next()){
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
			System.out.printf("%182s %1.2f %n", "$", allTotal);
			con.close();
		}
		catch(Exception e){}
	}
	
	//after check out, clear out cart
	public void clearCart(){
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM CART WHERE USERID = '" + this.userId + "'");
			stmt.executeUpdate();
			con.close();
		}
		catch(Exception e){
		}
	}
	
	public void addToCart(String isbn, int quantity){
		try {
			   Connection con = getConnection();
			   PreparedStatement stmt = con.prepareStatement("INSERT INTO CART(USERID, ISBN, QTY) VALUES(?,?,?)");
			   stmt.setString(1, this.userId);
			   stmt.setString(2, isbn);
			   stmt.setInt(3, quantity);
			   stmt.executeUpdate();
			   con.close();
			   System.out.println("You have added an item!");
			  }
		catch(Exception e) {  System.out.println(e); }
	}
	
	public void deleteFromCart(String isbn){
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM CART"
												+ " WHERE USERID ='" + this.userId +"' AND ISBN = '" + isbn + "'");
			stmt.executeUpdate();
			con.close();
			System.out.println("Item was deleted from your cart");
		}
		catch(Exception e){System.out.println(e);}
	}
	
	public void editCart(String isbn, int newQty){
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE CART SET QTY = " + newQty +  
					" WHERE USERID = '" + this.userId + "' AND ISBN = '" + isbn + "'");
			stmt.executeUpdate();
			con.close();
			System.out.println("Edit Item Completed");
		}
		catch(Exception e){System.out.println(e);}
	}
	
	//showing contents of shopping cart
	public void showCartContents(){
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT c.ISBN, b.TITLE, b.PRICE, c.QTY FROM CART c, BOOKS b "
															+ "WHERE c.ISBN = b.ISBN AND USERID = ?");
			stmt.setString(1, this.userId);
			ResultSet rs = stmt.executeQuery();
			
			System.out.printf("%-12s %-165s %-5s %-3s %-7s", "ISBN", "Title", "$", "Qty", "Total");
			System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------------"
					+ "--------------------------------------");
			double total = 0;
			double allTotal = 0;
			while(rs.next()){
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

		}
		catch(Exception e){	System.out.println(e);}
	}
	
	//getting connection to oracle database
	public static Connection getConnection(){
				try{
					Class.forName("oracle.jdbc.driver.OracleDriver");
					Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL", "akito_ito","utdfall2018");
					//Statement stmt = con.createStatement();
					//ResultSet rs = stmt.executeQuery("select * from BOOKS"
					return con;
				}
				catch(Exception e){
					System.out.println(e);
					return null;
				}
			}
}//end of class
