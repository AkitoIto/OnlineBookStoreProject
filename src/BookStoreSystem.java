import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

//basic user interface
public class BookStoreSystem {

	public static void main(String[] args){
		//login option for users
		String login_menu = "z";
		Scanner input = new Scanner(System.in);
		
		
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
						User user = new User();
						user.accountLogin();
						login_menu = "q";
						break;
						
					//new member registration
					case "2":
						User new_user = new User();
						new_user.accountCreation();
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
		
		
		//if login successful
		//Member Menu
		if(!login_menu.equals("q")){
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
			
			
		}
		input.close();
	}
	
	
	public void browse(String userId)
	{
	   System.out.println("Type in your option");
	   try
	   {
		   Connection con = this.getConnection();
		   
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
}

