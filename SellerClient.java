import java.rmi.*;
import java.net.MalformedURLException;
import java.util.*;
import java.io.*;

public class SellerClient
{
	public static void main(String[] args)
	{
		try{
			Auction a = (Auction) Naming.lookup("AuctionService");

			//Scan for user inputs	
			Scanner reader = new Scanner(System.in);

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
					System.out.println("Enter item name: ");
					String itemName = reader.nextLine();

					System.out.println("Enter Starting Price: ");
					int startingPrice = reader.nextInt();

					System.out.println("Enter Minimum Acceptable Price: ");
					int reservePrice = reader.nextInt();

					int auction_id = a.createAuction(itemName, startingPrice, reservePrice);

					
					System.out.println("******************************");
					System.out.println("Your Auction ID     : " + auction_id);
					System.out.println("Your Item Name      : " + itemName);
					System.out.println("Your Starting Price : " + startingPrice);
					System.out.println("Your Reserve Price  : " + reservePrice);
					System.out.println("******************************");

					option = null;
				}

				if("2".equals(option)){
					System.out.println("Enter auction ID: ");
					int auction_id = reader.nextInt();
					//Get the details from the given auction id
					Map<Integer, Item> items = a.getItems();
					Item item = items.get(auction_id);

					Bidder bidder = a.closeAuction(auction_id);

					//If no one has bid on the item
					if(bidder==null){
						System.out.println("No one has bid on this item.");

					//If reserve price has not been reached
					}else if(item.getReservePrice() > item.getStartingPrice()){
						System.out.println("the reserve price has not been reached");

					//Show winner's details
					}else{
						System.out.println("Higest Bid: " + item.getStartingPrice());
						System.out.println("Winner's Name: " + bidder.getName());
						System.out.println("Winner's email: " + bidder.getEmail());
					}

					option = null;
				}
			}
			reader.close();

		}catch(Exception e){
			e.printStackTrace();
		}

	}
}