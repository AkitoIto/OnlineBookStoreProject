import java.sql.*;
import java.util.*;

//basic user interface
public class BookStoreSystem {

	public static void main(String[] args){
		//login option for users
		String login_menu = "z";
		Scanner input = new Scanner(System.in);
		User user = new User();
		
		//Login/Registration Menu
		do{
			System.out.println("**********************************************************************");
			System.out.println("***                                                                ***");
			System.out.println("***              Welcome to the Online Book Store                  ***");
			System.out.println("***                                                                ***");
			System.out.println("**********************************************************************\n");
			System.out.println("                     1. Member Login");
			System.out.println("                     2. New Member Registration");
			System.out.println("                     q. Quit\n");
			System.out.print("Type in your option: ");  login_menu = input.nextLine();
			
			if(login_menu.equals("1") || login_menu.equals("2") || login_menu.equals("q"))
			{
				
				switch (login_menu){
					//member login
					case "1":
						user.accountLogin();
						login_menu = "q";
						break;
						
					//new member registration
					case "2":
						user.accountCreation();
						login_menu = "q";
						break;
						
					//quit
					case "q":
						break;
						
				}//end of switch	
			}//end of if
			else
				System.out.println("Please select the valid option");
			
		}while(!login_menu.equals("q"));
		
		
		int menu = 100;
		
		//if login successful
		//Member Menu
		if(!login_menu.equals("q"))
		{
			System.out.println("**********************************************************************");
			System.out.println("***                                                                ***");
			System.out.println("***              Welcome to the Online Book Store                  ***");
			System.out.println("***                 Member Menu                                    ***");
			System.out.println("***                                                                ***");
			System.out.println("**********************************************************************\n");
			System.out.println("                   1. Browse by Subject");
			System.out.println("                   2. Search by Author/Title/Subject");
			System.out.println("                   3. View/Edit Shopping Cart");
			System.out.println("                   4, Check Order Status");
			System.out.println("                   5, Check Out");
			System.out.println("                   6. One Click Check Out");
			System.out.println("                   7. View/Edit Personal Information");
			System.out.println("                   8. Logout");
		    System.out.print("Type in your option: "); menu = input.nextInt();
		    input.nextLine();
		    
		    //getting current user's id
		    String id = user.getId();
		    switch(menu)
		    {
		      	case 1:
		      		browse(id);
		    	    break;
		      	case 2:
		      		searchByAuthor();
		      		break;
		      	case 3:
		      		viewEditShoppingCart(id);
		      		break;
		      	case 4:
		      		break;
		      	case 5: 
		      		break;
		      	case 6:
		      		break;
		      	case 7:
		      		break;
		    }
			
		}
		input.close();
	}
	
	
	public static void browse(String userId)
	{
		
		Scanner input = new Scanner(System.in);
		String option = "3";
		
	  while(!option.equals("2"))
	  {
		 String[] subject = new String[100];
		
	   //listing subjects
	   try
	   {
		   Connection con = getConnection();
		   String query = "SELECT DISTINCT SUBJECT FROM BOOKS ORDER BY SUBJECT";
		   PreparedStatement stmt = con.prepareStatement(query);
		   ResultSet rs = stmt.executeQuery();
		   int num = 1;
		   while(rs.next())
		   {
			   System.out.println(num + " "+ rs.getString(1));
			   subject[num] = rs.getString(1);
			   num++;
		   }
		   con.close();
	   }
	   catch(Exception e)
	   {
		   System.out.println(e);
	   }
	   
	   System.out.print("Enter your choice: ");  int choice = input.nextInt();
	   input.nextLine();
	   //getting book lists for the subject user chose
	   try
	   {
		   Connection con = getConnection();
		   String query = "SELECT * FROM BOOKS WHERE SUBJECT = ?";
		   String numRows = "SELECT COUNT(*) FROM BOOKS WHERE SUBJECT = ?"; 
		   PreparedStatement stmt = con.prepareStatement(query);
		   PreparedStatement stmt2 = con.prepareStatement(numRows);
		   stmt.setString(1, subject[choice]);
		   ResultSet rs = stmt.executeQuery();
		   stmt2.setString(1, subject[choice]);
		   ResultSet rs2 = stmt2.executeQuery();
		   //getting number of books in a subject
		   while(rs2.next())
		   {
			   int rows = rs2.getInt(1);
			   System.out.println(rows + " books available on this subject\n\n");
		   }
		   //listing all books from a subject
		   while(rs.next())
		   {
			   System.out.println("Author:   " + rs.getString(2)); //AUTHOR
			   System.out.println("TITLE:    " + rs.getString(3)); //TITLE
			   System.out.println("ISBN:     " + rs.getString(1)); //ISBN
			   System.out.println("PRICE:    " + rs.getString(4)); //PRICE
			   System.out.println("SUBJECT:  " + rs.getString(5) +"\n"); //SUBJECT
		   }
		   con.close();
	   }
	   catch(Exception e)
	   {
		   System.out.println(e);
	   }
	   
	   
	   System.out.println("1. Enter ISBN to put in cart");
	   System.out.println("2. Return to main menu");
	   System.out.println("3. Continue browsing"); 
	   System.out.print("Type in your option: ");  option = input.nextLine();
	   
	   //shopping cart
	   if(option.equals("1"))
	   {
		   String isbn = "";
		   int quantity = 0;
		   System.out.print("Enter ISBN to add to shopping cart: "); isbn = input.nextLine();
		   System.out.print("Enter quantity: "); quantity = input.nextInt();
		   try
		   {
		   Connection con = getConnection();
		   PreparedStatement stmt = con.prepareStatement("INSERT INTO CART(USERID, ISBN, QTY) VALUES(?,?,?)");
		   stmt.setString(1, userId);
		   stmt.setString(2, isbn);
		   stmt.setInt(3, quantity);
		   stmt.executeUpdate();
		   con.close();
		   System.out.println("You have added an item!");
		   }
		   catch(Exception e)
		   {
			   System.out.println(e);
		   }
	   }
	   
	  }
	}
	
	public static void searchByAuthor()
	{
	   Scanner input = new Scanner(System.in);
	   int option = 100;
	   
	   while(option != 3)
		{
			System.out.println("1. Author Search");
			System.out.println("2. Title Search");
			System.out.println("3. Return to Member Menu");
			System.out.print("Type in your option: "); 
			option = input.nextInt(); 
			input.nextLine();
			
			//Author Search
			if(option == 1)
			{
				System.out.print("Enter Author name or part of author name: ");
				String author = input.nextLine();
				
				try
				{
					Connection con = getConnection();
					PreparedStatement stmt = con.prepareStatement("SELECT * FROM BOOKS WHERE AUTHOR LIKE '%" + author + "%'");
					ResultSet rs = stmt.executeQuery();
					while(rs.next())
					{
						 System.out.println("Author:   " + rs.getString(2)); //AUTHOR
						 System.out.println("TITLE:    " + rs.getString(3)); //TITLE
						 System.out.println("ISBN:     " + rs.getString(1)); //ISBN
						 System.out.println("PRICE:    " + rs.getString(4)); //PRICE
						 System.out.println("SUBJECT:  " + rs.getString(5) +"\n"); //SUBJECT
					}
					con.close();
				}
				catch(Exception e)
				{
					System.out.println("There are no books with Author name '" + author + "'");
				}
			}//end if
			//Title Search
			else if(option == 2)
			{
				System.out.print("Enter title or part of title: ");
				String title = input.nextLine();
				try
				{
					Connection con = getConnection();
					PreparedStatement stmt = con.prepareStatement("SELECT * FROM BOOKS WHERE TITLE LIKE '%" + title +"%'");
					ResultSet rs = stmt.executeQuery();
					while(rs.next())
					{
						 System.out.println("Author:   " + rs.getString(2)); //AUTHOR
						 System.out.println("TITLE:    " + rs.getString(3)); //TITLE
						 System.out.println("ISBN:     " + rs.getString(1)); //ISBN
						 System.out.println("PRICE:    " + rs.getString(4)); //PRICE
						 System.out.println("SUBJECT:  " + rs.getString(5) +"\n"); //SUBJECT
					}
					con.close();
				}
				catch(Exception e)
				{
					System.out.println("There are no books containg title '" + title + "'");
				}
				
			}//end else if	
		}//end while
	}
	
	public static void viewEditShoppingCart(String userId)
	{
		String menu = "";
	 do{
		//showing contens in shopping cart
		showCartContents(userId);
		
		Scanner input = new Scanner(System.in);
		System.out.println("Enter d to delete item");
		System.out.println("e to edit cart or ");
		System.out.print("q to go back to Menu: "); menu = input.nextLine();
		
		
		//delete or edit cart
		if(!menu.equals("q"))
		{
			try
			{
				System.out.print("Enter ISBN of item: "); String isbn = input.nextLine();
				Connection con = getConnection();
				if(menu.equals("d"))
				{
					PreparedStatement stmt = con.prepareStatement("DELETE FROM CART WHERE USERID ='" + userId +"' AND ISBN = '" + isbn + "'");
					stmt.executeUpdate();
					con.close();
					System.out.println("Item was deleted from your cart");
				}
				else if(menu.equals("e"))
				{
					System.out.print("Enter new quantitiy: "); int newQty = input.nextInt(); 
					PreparedStatement stmt = con.prepareStatement("UPDATE CART SET QTY = " + newQty +  " WHERE USERID = '" + userId + "' AND ISBN = '" + isbn + "'");
					stmt.executeUpdate();
					con.close();
					System.out.println("Edit Item Completed");
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
		
	  }while(!menu.equals("q"));
	}
	
	public static void checkOut(String userId)
	{
		//showing cart contents
		showCartContents(userId);
	}
	
	public static void showCartContents(String userId)
	{
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT c.ISBN, b.TITLE, b.PRICE, c.QTY FROM CART c, BOOKS b WHERE c.ISBN = b.ISBN AND USERID = ?");
			stmt.setString(1, userId);
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
			System.out.printf("%188.2f", allTotal);
			System.out.println();
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}
	public static Connection getConnection()
	{
			try
			{
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL", "akito_ito","utdfall2018");
				return con;
			}
			catch(Exception e)
			{
				System.out.println(e);
				return null;
			}
		}
}

