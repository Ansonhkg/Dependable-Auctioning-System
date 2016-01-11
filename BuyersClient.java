import java.rmi.*;
import java.net.MalformedURLException;
import java.util.*;
import java.io.*;
import java.io.Console;

public class BuyersClient
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
				System.out.println("BuyersClient does not have access right.");
			}

		}catch(Exception e){
			reconnect();
			// e.printStackTrace();
		}

	}

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
					System.out.println("Enter '1'    to browse items");
					System.out.println("Enter '2'    to bid an auction");
					System.out.println("---------------------------------");
				}

				option = reader.nextLine();

				if("1".equals(option)){
					
					listItems(a.getItems());

					option = null;
				}

				if("2".equals(option)){
					
					System.out.println("Enter the auction id of the item you want to bid on: ");
					int auction_id = reader.nextInt();

					//Get the details from the given auction id
					Map<Integer, Item> items = a.getItems();
					Item item = items.get(auction_id);

					int placeBid;
					if(item.getActive()){
						do{
							
							System.out.println("Enter your bid more than " + item.getCurrentBid());
							placeBid = reader.nextInt();

							a.bid(user.getId(), auction_id, placeBid);
							// a.bid(name, email, auction_id, placeBid);
													
						}while(placeBid <= item.getCurrentBid() && item.getActive());
					}else{
						System.out.println("Sorry. This auction is closed.");
						return;
					}
					option = null;
				}
			}
			reader.close();

		}catch(Exception e){
			reconnect();
		}
		
	}

	public static void listItems(Map<Integer, Item> itemsMap)
	{
		for(Map.Entry<Integer, Item> entry : itemsMap.entrySet()){
			Item item = entry.getValue();

			//Only show active items
			if(item.getActive()){
				System.out.println("/========== " + item.getItemName() + "(" + item.getAuctionId() + ") ==========/");
				System.out.println("Auction ID:       |  " + item.getAuctionId());
				System.out.println("Item Name:        |  " + item.getItemName());
				System.out.println("Highest Bid:      |  " + item.getCurrentBid());
				// System.out.println("Reserve Price:    |  " + item.getReservePrice());
			}
		}
	}

}