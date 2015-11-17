import java.rmi.*;
import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

public class BuyersClient implements Serializable
{
	public static void main(String[] args)
	{
		try{
			Auction a = (Auction) Naming.lookup("AuctionService");
			
			listItems(a.getItems());

			//Scan for user inputs
			Scanner reader = new Scanner(System.in);
			
			System.out.println("Enter name: ");
			String name = reader.nextLine();
			
			System.out.println("Enter email: ");
			String email = reader.nextLine();
		
			System.out.println("Hello, " + name + ".");
			
			System.out.println("Enter the auction id of the item you want to bid on: ");
			int auction_id = reader.nextInt();

			//Get the details from the given auction id
			Map<Integer, Item> items = a.getItems();
			Item item = items.get(auction_id);

			int placeBid;

			do{
				if(item.getActive()){
					System.out.println("Enter your bid more than " + item.getStartingPrice());
					placeBid = reader.nextInt();

					a.bid(name, email, auction_id, placeBid);
					listItems(a.getItems());	
										
				}else{
					System.out.println("Sorry. This auction is closed.");
					return;
				}
			}while(placeBid <= item.getStartingPrice() && item.getActive());


		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public static void listItems(Map<Integer, Item> itemsMap)
	{
		for(Map.Entry<Integer, Item> entry : itemsMap.entrySet()){
			Item item = entry.getValue();

			//Only show active items
			if(item.getActive()){
				System.out.println("/========== " + item.getItemName() + "==========/");
				System.out.println("Auction ID:       |  " + item.getAuctionId());
				System.out.println("Item Name:        |  " + item.getItemName());
				System.out.println("Highest Bid:      |  " + item.getStartingPrice());
				System.out.println("Reserve Price:    |  " + item.getReservePrice());
			}else{
				System.out.println("Sorry. This auction is closed.");
				return;
			}
		}
	}
}