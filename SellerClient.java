import java.rmi.*;
import java.net.MalformedURLException;
import java.util.*;
import java.io.*;

public class SellerClient
{
	public static Auction a = null;
	public static User user = null;
	public static Scanner reader = new Scanner(System.in);

	public static void main(String[] args)
	{
		try{
			a = (Auction) Naming.lookup("AuctionService");
			AuthClient auth = new AuthClient(a);
			Boolean authed = auth.authenticated();

			//Check if it's a legit client
			if(authed){

				System.out.print("Enter username: ");
				String name = reader.nextLine();
				
				System.out.print("Enter password: ");
				Console console = System.console();
			    char[] passString = console.readPassword();
			    String password = new String(passString);

				user = new User(name, auth.password(password));
				
				//User access control
				User u = a.login(user);
				if(u instanceof User){
					user = u;
					System.out.println("\n> Hi, " + user.getName() + ". Welcome back!");
					run();
				}else{
					System.out.println("> Invalid username/password. Please try again.");
				}

			}else{
				System.out.println("SellerClient does not have access right.");
			}

		}catch(Exception e){
			reconnect();
			// e.printStackTrace();
		}

	}

	//Passive 
	public static void reconnect()
	{
		try{
			a = (Auction) Naming.lookup("AuctionService");
			System.out.println("# System was down. Reconnected.");
			System.out.println("\n> Hi, " + user.getName() + ". Welcome back!");
			run();
		}catch(Exception e){
			System.out.println("Reconnection failed.");
			e.printStackTrace();
		}
		
	}

	public static void run() throws Exception
	{
		try{
			String option = null;

			while(!"exit".equals(option)){
				
				if(option==null){
					System.out.println("---------------------------------");
					System.out.println("Enter 'exit' to exit program");
					System.out.println("Enter '1'    to create an auction");
					System.out.println("Enter '2'    to close an auction");
					System.out.println("---------------------------------");
				}

				option = reader.nextLine();

				if("1".equals(option)){
					System.out.print("Enter item name: ");
					String itemName = reader.nextLine();

					System.out.print("Enter Starting Price: ");
					int startingPrice = reader.nextInt();

					System.out.print("Enter Minimum Acceptable Price: ");
					int reservePrice = reader.nextInt();

					int auction_id = a.createAuction(user.getId(), itemName, startingPrice, reservePrice);

					
					System.out.println("******************************");
					System.out.println("Your Auction ID     : " + auction_id);
					System.out.println("Your Item Name      : " + itemName);
					System.out.println("Your Starting Price : " + startingPrice);
					System.out.println("Your Reserve Price  : " + reservePrice);
					System.out.println("******************************");

					option = null;
				}

				if("2".equals(option)){
					System.out.print("Enter auction ID: ");
					int auction_id = reader.nextInt();
					
					//Get the details from the given auction id
					Map<Integer, Item> items = a.getItems();
					Item item = items.get(auction_id);

					Boolean auctionClosed = a.closeAuction(user.getId(), auction_id);
					
					if(auctionClosed){

						System.out.println("Item { " + item.getItemName() + " } is closed.");
						
						//If reserve price has not been reached
						if(item.getReservePrice() > item.getCurrentBid()){
							System.out.println("the reserve price has not been reached");
						}else{
							Bidder winner = a.getWinner(auction_id);

							//Show winner's details
							System.out.println("Highest Bid: " + item.getCurrentBid());
							System.out.println("Winner's Name: " + winner.getUser().getName());
							System.out.println("Winner's Email: " + winner.getUser().getEmail());
						}

					}else{
						System.out.println("Only the owner of this item can close the auction.");
					}


					option = null;
				}
			}
			reader.close();
			
		}catch(Exception e){
			reconnect();
		}
	}
}