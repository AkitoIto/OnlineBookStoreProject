import java.sql.*;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class BookStoreSystem {
	public static void main(String[] args){
		//login option for users
		String loginMenu = "";
		String firstName = "", lastName = "", address = "", city = "", state = "", phone = "", email = "", userId = "", pwd = "", pwd2 = "";
		int zip = 0;
		String creditMenu = "", cardType = null, cardNum = null;
		Scanner input = new Scanner(System.in);
		boolean isValid = false;
		//make user object
		User user = new User(null,null,null,null,null, 0, null, null, null, null, null, null);
		
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
			System.out.print("Type in your option: ");  loginMenu = input.nextLine();
			
			if(loginMenu.equals("1") || loginMenu.equals("2")){
				//member login
				if(loginMenu.equals("1")){
						
						do{
							System.out.println("Welcome to the Online Book Store"); 
							System.out.println("         Login page");
							System.out.print("Enter User ID: ");
							userId = input.nextLine(); 
							System.out.print("Enter password: ");
							pwd = input.nextLine(); 
							isValid = loginCheck(userId,pwd);		
						}while(!isValid);
						
						System.out.println("You have logined successfully");
						//if login successful get all attributes for this user object
						try{
							Connection con = getConnection();
							PreparedStatement stmt = con.prepareStatement("SELECT * FROM MEMBERS WHERE USERID = '" + userId + "'");
							ResultSet rs = stmt.executeQuery();
							while(rs.next()){
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
						catch(Exception e){}
						//make user instance
						user = new User(firstName, lastName, address, city, state, zip, phone, email, userId, pwd, cardType, cardNum);
				  }//end nested if
				
				//new member registration
				else if(loginMenu.equals("2")){
					System.out.println("Welcome to the Online Book Store"); 
					System.out.println("         New Member Registration");
					System.out.print("Enter First Name: "); firstName = input.nextLine(); 
					System.out.print("Enter Last Name: ");  lastName = input.nextLine(); 
					System.out.print("Enter Street: "); address = input.nextLine(); 
					System.out.print("Enter City: "); 	city = input.nextLine(); 
					System.out.print("Enter State: ");	state = input.nextLine();
					System.out.print("Enter Zip code: "); zip = input.nextInt(); input.nextLine();
					System.out.print("Enter Phone Number: "); phone = input.nextLine(); 
					System.out.print("Enter Email: ");	email = input.nextLine(); 
					do{
						System.out.print("Enter User ID: "); userId = input.nextLine(); 
						isValid = isIdUnique(userId);
					}while(!isValid);
					do{
						System.out.print("Enter Password: ");	pwd = input.nextLine(); 
						System.out.print("Confirm Password" ); pwd2 = input.nextLine();
						isValid = confirmPass(pwd, pwd2);
					}while(!isValid);
					System.out.println("Do you wish to store credit card information? (Y/N): "); creditMenu = input.nextLine();
				
					//if a customer wish to store credit card information
					if(creditMenu.equals("Y") || creditMenu.equals("y"))	{
						   do{
							System.out.print("Enter type of Credit Card (amex/visa): "); cardType = input.nextLine(); 
							System.out.print("Enter Credit Card Number: "); cardNum = input.nextLine();
							//check if entered card information is valid
							isValid = isCardValid(cardType, cardNum);
						}while(!isValid);
					}//end nested if
					
					//make user instance
					user = new User(firstName, lastName, address, city, state, zip, phone, email, userId, pwd, cardType, cardNum);
					user.registration();
					String showInfo = user.toString();
					System.out.println(showInfo);
				}//end nested else if
		    
				//entering member menu
				int menu = 0;
			    do
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
					System.out.println("                   2. Search by Author/Title");
					System.out.println("                   3. View/Edit Shopping Cart");
					System.out.println("                   4. Check Order Status");
					System.out.println("                   5. Check Out");
					System.out.println("                   6. One Click Check Out");
					System.out.println("                   7. View/Edit Personal Information");
					System.out.println("                   8. Logout");
				    System.out.print("Type in your option: "); 
				    menu = input.nextInt();
				    input.nextLine();
				   
				    switch(menu) {
				      	case 1:
				      		 browse(user.getId(), user);
				    	    break;
				      	case 2:
				      		searchByAuthor(user);
				      		break;
				      	case 3:
				      		 viewEditShoppingCart(user);
				      		break;
				      	case 4:
				      		break;
				      	case 5: 
				      		 checkOut(user);
				      		break;
				      	case 6:
				      		break;
				      	case 7:
				      		editInfo(user);
				      		break;
				      	case 8:
				      		System.out.println("Thank you for using our online book store!!");
				      		break;
				    }//end switch
				}while(menu != 8);
			}//end if
			
			else if(loginMenu.equals("q")){ //end of the program
			}
			else{
				System.out.println("Please select the valid option.");	
			}
		}while(!loginMenu.equals("q"));
    }//end main

	public static boolean isIdUnique(String userId){
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT COUNT (*) FROM MEMBERS WHERE USERID = '" + userId + "'");
			ResultSet rs = stmt.executeQuery();
			int count = 0;
			while(rs.next()){
				count = rs.getInt(1);
			}
			if(count == 1){
				System.out.println("Entered User ID already exists, please try another ID");
				return false;
			}
			else
				return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	//checks if login was successful or not
	public static boolean loginCheck(String userId, String pwd)
	{
		try{
			Connection con = getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT PASSWORD FROM MEMBERS WHERE USERID = '" + userId + "'");
			ResultSet rs = stmt.executeQuery();
			String password = "";
			//check if a user ID exists in database
			if(rs.next()){
					password = rs.getString(1);
				if(password.equals(pwd))
					return true;
				else{
					System.out.println("Password does not match your User ID, please try again");
				    return false;
				}
			}
			else{
				System.out.println("User ID does not exist, please try again");
				return false;
			}
	   }
		catch(Exception e){
			System.out.println(e);
			return false;
		}
	}
	
	public static boolean confirmPass(String pwd1, String pwd2){
		if(pwd1.equals(pwd2)) 
			return true;
		else{
			System.out.println("Confirmation password does not match.");
			return false;
		}
	}
	
	//checks if entered card information is valid of not
	 public static boolean isCardValid(String cardType, String cardNum) {
			if(cardType.equals("amex") || cardType.equals("visa")){
				if(cardNum.length() != 14){
					System.out.println("Card number length has to be 14");
					return false;
				}
				else
					return true;
			}
		    else{
				 if(cardNum.length() != 14){
				   System.out.println("Credit card type must be amex or visa");
				   System.out.println("Card number length has to be 14");
					return false;
				}
				else {
					System.out.println("Credit card type must be amex or visa");
					return false;
				 }
		       } 
	  }
			
	//browse by subject menu
	public static void browse(String userId, User user){
		Scanner input = new Scanner(System.in);
		String option = "3";
		String[] subject = new String[30];
		
	  do{
	   //listing subjects
	   try{
		   Connection con = getConnection();
		   String query = "SELECT DISTINCT SUBJECT FROM BOOKS ORDER BY SUBJECT";
		   PreparedStatement stmt = con.prepareStatement(query);
		   ResultSet rs = stmt.executeQuery();
		   int num = 1;
		   while(rs.next()) {
			   System.out.println(num + " "+ rs.getString(1));
			   subject[num] = rs.getString(1);
			   num++;
		   }
		   con.close();
	    }
	   catch(Exception e) {
	   }
	   
	   System.out.print("Enter your choice: ");  int choice = input.nextInt();
	   input.nextLine();
	   //getting book lists for the subject user chose
	   try {
		   Connection con = getConnection();
		   PreparedStatement stmt = con.prepareStatement("SELECT * FROM BOOKS WHERE SUBJECT = ?");
		   PreparedStatement stmt2 = con.prepareStatement("SELECT COUNT(*) FROM BOOKS WHERE SUBJECT = ?");
		   stmt.setString(1, subject[choice]);
		   ResultSet rs = stmt.executeQuery();
		   stmt2.setString(1, subject[choice]);
		   ResultSet rs2 = stmt2.executeQuery();
		   //getting number of books in a subject
		   while(rs2.next())
		   {
			   int rows = rs2.getInt(1);
			   System.out.println(rows + " books available on this subject.\n\n");
		   }
		   //listing all books from a subject
		   while(rs.next()){
			   System.out.println("Author:   " + rs.getString(2)); //AUTHOR
			   System.out.println("TITLE:    " + rs.getString(3)); //TITLE
			   System.out.println("ISBN:     " + rs.getString(1)); //ISBN
			   System.out.println("PRICE:    " + rs.getString(4)); //PRICE
			   System.out.println("SUBJECT:  " + rs.getString(5) +"\n"); //SUBJECT
		   }
		   con.close();
	   }
	   catch(Exception e) {}
	
	   do{
	   System.out.println("1. Enter ISBN to put in shopping cart");
	   System.out.println("2. Return to main menu");
	   System.out.println("3. Continue browsing"); 
	   System.out.print("Type in your option: ");  option = input.nextLine();
	   
	   //add to shopping cart
	   if(option.equals("1")) {
		   String isbn = "";
		   int quantity = 0;
		   System.out.print("Enter ISBN: "); isbn = input.nextLine();
		   System.out.print("Enter quantity: "); quantity = input.nextInt(); input.nextLine();
		   user.addToCart(isbn,quantity);
	     }//end if
	   }while(option.equals("1"));
	   
	  }while(!option.equals("2"));
	}
	
	//search by author or title menu
	public static void searchByAuthor(User user){
	   Scanner input = new Scanner(System.in);
	   String option = "", isContinue = "";
	   
	   do{
			System.out.println("1. Author Search");
			System.out.println("2. Title Search");
			System.out.println("3. Return to Member Menu");
			System.out.print("Type in your option: "); 
			option = input.nextLine();
			//Author Search
			if(option.equals("1")){
				System.out.print("Enter Author name or part of author name: ");
				String author = input.nextLine();
				
				try{
					Connection con = getConnection();
					PreparedStatement stmt = con.prepareStatement("SELECT * FROM BOOKS WHERE AUTHOR LIKE '%" + author + "%'");
					ResultSet rs = stmt.executeQuery();
					if(!rs.next())
					        System.out.println("There are no books with Author name '" + author + "'");
					else{
					while(rs.next()){
						 System.out.println("Author:   " + rs.getString(2)); //AUTHOR
						 System.out.println("TITLE:    " + rs.getString(3)); //TITLE
						 System.out.println("ISBN:     " + rs.getString(1)); //ISBN
						 System.out.println("PRICE:    " + rs.getString(4)); //PRICE
						 System.out.println("SUBJECT:  " + rs.getString(5) +"\n"); //SUBJECT
					}
					}
					con.close();
				}
				catch(Exception e){
				}
			}//end if
			//Title Search
			else if(option.equals("2")){
				System.out.print("Enter title or part of title: ");
				String title = input.nextLine();
				try{
					Connection con = getConnection();
					PreparedStatement stmt = con.prepareStatement("SELECT * FROM BOOKS WHERE TITLE LIKE '%" + title +"%'");
					ResultSet rs = stmt.executeQuery();
					if(!rs.next())
						System.out.println("There are no books containg title '" + title + "'");
					else{
						while(rs.next()){
							 System.out.println("Author:   " + rs.getString(2)); //AUTHOR
							 System.out.println("TITLE:    " + rs.getString(3)); //TITLE
							 System.out.println("ISBN:     " + rs.getString(1)); //ISBN
							 System.out.println("PRICE:    " + rs.getString(4)); //PRICE
							 System.out.println("SUBJECT:  " + rs.getString(5) +"\n"); //SUBJECT
						}
					}
					con.close();
				}
				catch(Exception e){
					
				}
			}//end else if	
			if(!option.equals("3")){
				System.out.println("1. Enter ISBN to add to shopping cart");
				System.out.println("2: Continue browsking");
				System.out.println("3: Return to main menu");
				System.out.print("Enter your option: "); option = input.nextLine();
				if(option.equals("1")){
				System.out.print("Enter ISBN: "); String isbn = input.nextLine();
				System.out.print("Enter Quantity: "); int quantity = input.nextInt(); 
				input.nextLine();
				user.addToCart(isbn, quantity);
			}
			}
		}while(!option.equals("3"));
	}
	
	//view or edit shopping cart menu
	public static void viewEditShoppingCart(User user){
		String menu = "";
	 do{
		//showing contents in shopping cart
		System.out.println("Current cart contents: ");
		user.showCartContents();
		
		Scanner input = new Scanner(System.in);
		System.out.println("Enter d to delete item");
		System.out.println("e to edit cart or ");
		System.out.print("q to go back to Menu: "); menu = input.nextLine();
		
		
		//delete or edit cart
		if(!menu.equals("q")){
				System.out.print("Enter ISBN of item: "); String isbn = input.nextLine();
				if(menu.equals("d")){
					user.deleteFromCart(isbn);
				}
				else if(menu.equals("e")){
					System.out.print("Enter new quantitiy: "); int newQty = input.nextInt(); 
					user.editCart(isbn, newQty);
				}
			}
	  }while(!menu.equals("q"));
	}
	
	//check order status menu
	public static void checkOrderStatus(User user){
		
	}
	
	//check out menu
	public static void checkOut(User user){
		//variable for new shipping address
		String firstName = "", lastName = "", street ="", city = "", state = "";
		int zip = 0;
		boolean isNewAddress = false;
		boolean isValid = false;
		//showing cart contents
		user.showCartContents();
		Scanner input = new Scanner(System.in);
		
		System.out.print("Proceed to check out?(Y/N): "); String choice = input.nextLine();
		if(choice.equals("y") || choice.equals("Y")){
			System.out.print("Do you want to enter new shipping address?(Y/N): "); String choice2 = input.nextLine();
			if(choice2.equals("y") || choice2.equals("Y")){
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
			if(isCard){
				System.out.print("Do you want to enter new Credit Card Number?(Y/N): "); String choice3 = input.nextLine();
				//update card information
				if(choice3.equals("y") || choice3.equals("Y")){
					do{
						System.out.print("Enter new card type (amex/visa): "); String cardType = input.nextLine();
						System.out.print("Enter new credit card number: "); String cardNum = input.nextLine();
						isValid = isCardValid(cardType, cardNum);
					}while(!isValid);
				}//end nested if
			}//end if
			
			//user does not have a card information
			else{
				String cardType = "";
				String cardNum = "";
				 do{
					System.out.println("You currently do not have any card information.");
					System.out.print("Enter credit card type (amex/visa): "); cardType = input.nextLine();
					System.out.print("Enter credit card number: "); cardNum = input.nextLine();
					isValid = isCardValid(cardType, cardNum);
				}while(!isValid);
				//update card information
				user.updateCard(cardType, cardNum);
			}//end else
			
	
			int orderNum = user.insertOrder(street, city, state, zip, isNewAddress);
			user.generateInvoice(orderNum, firstName, lastName, street, city, state, zip, isNewAddress);
			user.showOrder(orderNum);
			System.out.println("Please enter to go back to menu");
			input.nextLine();
		}//end if
	}
	
	//one click order menu
	public static void oneClick(User user){
		
	}
	
	//view/edit personal information menu
	public static void editInfo(User user){
		Scanner input = new Scanner(System.in);
		int menu = 0;
		boolean isValid = false;
		while(menu != 2){
		System.out.print(user.toString());
		System.out.println("1. Edit information");
		System.out.println("2. Quit");
		System.out.print("Choose your option: "); menu = input.nextInt(); input.nextLine();
		
		if(menu == 1){
			System.out.println("Which information would you like to change? (User ID cannot be changed)");
			System.out.println("1. Name");
			System.out.println("2. Address");
			System.out.println("3. Phone number");
			System.out.println("4. Email");
			System.out.println("5. Password");
			System.out.println("6. Credit Card Information"); 
			System.out.println("0. Exit");
			System.out.println("Type in your option: "); int whichInfo = input.nextInt(); input.nextLine();
			
			if(whichInfo == 1){
				System.out.print("Enter first name: "); String firstName = input.nextLine();
				System.out.print("Enter last name: "); String lastName = input.nextLine();
				user.updateName(firstName, lastName);
			}
			else if(whichInfo == 2){
				System.out.print("Enter new street address: "); String address = input.nextLine();
				System.out.print("Enter new city: "); String city = input.nextLine();
				System.out.print("Enter new state: "); String state = input.nextLine();
				System.out.print("Enter new zip: "); int zip = input.nextInt(); input.nextLine();
				user.updateAddress(address, city, state, zip);
			}
			else if(whichInfo == 3){
				System.out.print("Enter new phone number: "); String phone = input.nextLine();
				user.updatePhone(phone);
			}
			else if(whichInfo == 4){
				System.out.print("Enter new email: "); String email = input.nextLine();
				user.updateEmail(email);
			}
			else if(whichInfo == 5){
				String pwd = "";
				do{
					System.out.print("Enter new password: "); pwd = input.nextLine();
					System.out.print("Confirm password: "); String pwd2 = input.nextLine();
					isValid = confirmPass(pwd, pwd2);
				}while(!isValid);
				user.updatePwd(pwd);
			}
			else if(whichInfo == 6){
				String cardType = "", cardNum = "";
				 do{
					System.out.print("Enter new card type: ");  cardType = input.nextLine();
					System.out.print("Enter new card number: "); cardNum = input.nextLine();
					isValid = isCardValid(cardType, cardNum);
				  }while(!isValid);
				user.updateCard(cardType, cardNum);
			}
		  }//end outer if
		}//end while
	}//end of method
	
	public static Connection getConnection(){
			try{
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL", "akito_ito","utdfall2018");
				return con;
			}
			catch(Exception e){
				System.out.println(e);
				return null;
			}
		}
}//end of class

