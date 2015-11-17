import java.rmi.*;
import java.rmi.server.*;
import java.net.MalformedURLException;
import java.util.*;
import java.io.*;

public class AuctionImpl extends UnicastRemoteObject implements Auction
{

	public Map<Integer, Item> items = new HashMap<Integer, Item>();
	public Map<Integer, Bidder> bidders = new HashMap<Integer, Bidder>();

	public AuctionImpl() throws RemoteException{};

	//Enable a seller to create a new auction.
	//@param {int} startingPrice - Aka. current bid or highest bid.
	//@param {int} reservePrice - Aka. Minimum acceptable price.
	//@return - A unique auction identifier for the newly created auction.
	public int createAuction(String itemName, int startingPrice, int reservePrice) throws RemoteException{

		System.out.println("createAuction Called");
		
		int auction_id = items.size();

		items.put(auction_id, new Item(auction_id, itemName, startingPrice, reservePrice));

		return auction_id;
	};

	//Close auction and return winner's details.
	// @param {int} auction_id - auction that you want to close.
	// @return - Winner's details.
	public Bidder closeAuction(int auction_id) throws RemoteException{
		
		//Get the current item according to the given ID
		Item item = items.get(auction_id);

		//Set item to inactive
		item.setActive(false);

		Bidder bidder = bidders.get(auction_id);

		return bidder;
	};

	
	//----- Potential Buyers -----//
	//Enable potential buyers to bid for auctioned items.
	public void bid(String name, String email, int auction_id, int placeBid) throws RemoteException{
		
		System.out.println("Name: " + name);
		System.out.println("Email: " + email);

		//Get the current item according to the given ID
		Item item = items.get(auction_id);
		
		//If item is active
		if(item.getActive()){
			//Update new higest bid in items table
			items.put(auction_id, new Item(auction_id, item.getItemName(), placeBid, item.getReservePrice()));

			//Add new bidder to bidders table
			bidders.put(auction_id, new Bidder(auction_id, name, email));
		}
		
	};

	//browse a list of currently active auctions with their current highest bid (But not the reserved price, which is secret.)
	public Map<Integer, Item> getItems() throws RemoteException{
		return items;
	};

	//List bidders of a specific auction 
	public Map<Integer, Bidder> getBidders() throws RemoteException{
		return bidders;
	};
}