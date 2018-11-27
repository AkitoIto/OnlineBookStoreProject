import java.sql.*;
import java.util.*;

//basic user interface
public class BookStoreSystem 
{

	public static void main(String[] args)
	{
		//login option for users
		String loginMenu = "z";
		String firstName ="";
		String lastName = "";
		String address = "";
		String city = "";
		String state = "";
		int zip = 0;
		String phone = "";
		String email = "";
		String userId = "";
		String pwd = "";
		String creditMenu = "";
		String cardType = null;
		String cardNum = null;
		Scanner input = new Scanner(System.in);
		int orderNum = 1;
		//make user object
		User user = new User(null,null,null,null,null, 0, null, null, null, null, null, null);
		
		//Login/Registration Menu
		while(!loginMenu.equals("q"))
		{
			System.out.println("**********************************************************************");
			System.out.println("***                                                                ***");
			System.out.println("***              Welcome to the Online Book Store                  ***");
			System.out.println("***                                                                ***");
			System.out.println("**********************************************************************\n");
			System.out.println("                     1. Member Login");
			System.out.println("                     2. New Member Registration");
			System.out.println("                     q. Quit\n");
			System.out.print("Type in your option: ");  loginMenu = input.nextLine();
			
			if(loginMenu.equals("1") || loginMenu.equals("2"))
			{
				//member login
				if(loginMenu.equals("1"))
				{
						boolean isValid = false;
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
								firstName = rs.getString(1);
								lastName = rs.getString(2);
								address = rs.getString(3);
								city = rs.getString(4);
								state = rs.getString(5);
								zip = rs.getInt(6);
								phone = rs.getString(7);
								email = rs.getString(8);
								cardType = rs.getString(11);
								cardNum = rs.getString(12);
							}
						}
						catch(Exception e)
						{
							
						}
						//make user instance
						user = new User(firstName, lastName, address, city, state, zip, phone, email, userId, pwd, cardType, cardNum);
				  }//end nested if
				
				//new member registration
				else if(loginMenu.equals("2"))
				{
					System.out.println("Welcome to the Online Book Store"); 
					System.out.println("         New Member Registration");
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
					
					//if a customer do not want to store credit card information, it was stored null initially
					
					//make user instance
					user = new User(firstName, lastName, address, city, state, zip, phone, email, userId, pwd, cardType, cardNum);
					user.registration();
					String showInfo = user.toString();
					System.out.println(showInfo);
				}//end nested else if
		    
				//entering member menu
				int menu = 0;
				while(menu != 8)
				{
				//if login successful
				//Member Menu
					System.out.println("**********************************************************************");
					System.out.println("***                                                                ***");
					System.out.println("***              Welcome to the Online Book Store                  ***");
					System.out.println("***                 Member Menu                                    ***");
					System.out.println("***                                                                ***");
					System.out.println("**********************************************************************\n");
					System.out.println("                   1. Browse by Subject");
					System.out.println("                   2. Search by Author/Title/Subject");
					System.out.println("                   3. View/Edit Shopping Cart");
					System.out.println("                   4. Check Order Status");
					System.out.println("                   5. Check Out");
					System.out.println("                   6. One Click Check Out");
					System.out.println("                   7. View/Edit Personal Information");
					System.out.println("                   8. Logout");
				    System.out.print("Type in your option: "); 
				    menu = input.nextInt();
				    input.nextLine();
				   
				    switch(menu)
				    {
				      	case 1:
				      		 browse(user.getId());
				    	    break;
				      	case 2:
				      		searchByAuthor();
				      		break;
				      	case 3:
				      		 viewEditShoppingCart(user);
				      		break;
				      	case 4:
				      		break;
				      	case 5: 
				      		 checkOut(user, orderNum);
				      		break;
				      	case 6:
				      		break;
				      	case 7:
				      		break;
				      	case 8:
				      		System.out.println("Thank you for using our online book store!!");
				      		break;
				    }//end switch
				}//end while
			}//end if
			
			else if(loginMenu.equals("q"))
			{
				//end of the program
			}
			else
			{
				System.out.println("Please select the valid option");	
			}
		}//end while*/
    }//end main

	
	
	//checks if login was successful or not
	public static boolean loginCheck(String userId, String pwd)
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
	
	//checks if entered card information is valid of not
	 public static boolean isCardValid(String cardType, String cardNum)
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
			
			
	
	//browse by subject menu
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
	
	//search by author or title menu
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
	
	//view or edit shopping cart menu
	public static void viewEditShoppingCart(User user)
	{
		String menu = "";
	 do{
		//showing contens in shopping cart
		user.showCartContents();
		
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
					PreparedStatement stmt = con.prepareStatement("DELETE FROM CART WHERE USERID ='" + user.getId() +"' AND ISBN = '" + isbn + "'");
					stmt.executeUpdate();
					con.close();
					System.out.println("Item was deleted from your cart");
				}
				else if(menu.equals("e"))
				{
					System.out.print("Enter new quantitiy: "); int newQty = input.nextInt(); 
					PreparedStatement stmt = con.prepareStatement("UPDATE CART SET QTY = " + newQty +  " WHERE USERID = '" + user.getId() + "' AND ISBN = '" + isbn + "'");
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
	
	//check order status menu
	
	//check out menu
	public static void checkOut(User user, int orderNum)
	{
		//variable for new shipping address
		String firstName = "";
		String lastName = "";
		String street ="";
		String city = "";
		String state = "";
		int zip = 0;
		boolean isNewAddress = false;
		
		//showing cart contents
		user.showCartContents();
		Scanner input = new Scanner(System.in);
		
		System.out.print("Proceed to check out?(Y/N): "); String choice = input.nextLine();
		if(choice.equals("y") || choice.equals("Y"))
		{
			System.out.print("Do you want to enter new shipping address?(Y/N): "); String choice2 = input.nextLine();
			if(choice2.equals("y") || choice2.equals("Y"))
			{
				System.out.print("Enter first name: "); firstName = input.nextLine();
				System.out.print("Enter last name: "); lastName = input.nextLine();
				System.out.print("Enter street: "); street = input.nextLine();
				System.out.print("Enter city: ");   city = input.nextLine();	  
				System.out.print("Enter state: "); state = input.nextLine();   
				System.out.print("Enter zip: "); zip = input.nextInt(); input.nextLine(); 
				isNewAddress = true;
			}
			
			boolean isCard = user.checkIfCard();
			//user has a card information
			if(isCard)
			{
				System.out.print("Do you want to enter new Credit Card Number?(Y/N): "); String choice3 = input.nextLine();
				//update card information
				if(choice3.equals("y") || choice3.equals("Y"))
				{
					boolean isValid = false;
					while(!isValid)
					{
						System.out.print("Enter new card type (amex/visa): "); String cardType = input.nextLine();
						System.out.print("Enter new credit card number: "); String cardNum = input.nextLine();
						//isValid = user.isCardValid(cardType, cardNum);
					}
				}//end nested if
			}//end if
			
			//user does not have a card information
			else
			{
				boolean isValid = false;
				String cardType = "";
				String cardNum = "";
				while(!isValid)
				{
					System.out.println("You currently do not have any card information");
					System.out.print("Enter credit card type (amex/visa): "); cardType = input.nextLine();
					System.out.print("Enter credit card number: "); cardNum = input.nextLine();
					//isValid = user.isCardValid(cardType, cardNum);
				}
				//update card information
				user.setCard(cardType, cardNum);
				user.updateCard();
			}//end else
			
			//generating invoice
			user.generateInvoice(orderNum, firstName, lastName, street, city, state, zip, isNewAddress);
			orderNum++;
			
		}//end if
	}
	
	//one click order menu
	
	//view/edit personal information menu
	
	
	
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

