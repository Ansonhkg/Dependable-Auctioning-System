import java.rmi.*;
import java.rmi.server.*;
import java.net.MalformedURLException;
import java.util.*;
import java.io.*;

public class AuctionImpl extends UnicastRemoteObject implements Auction
{

	public Map<Integer, Item> items = new HashMap<Integer, Item>();

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
	//@param {int} auction_id - auction that you want to close.
	//@return - Winner's details.
	// public String closeAuction(int auction_id) throws RemoteException{

	// };

	
	//----- Potential Buyers -----//
	//Enable potential buyers to bid for auctioned items.
	public void bid(String name, String email, int auction_id, int placeBid) throws RemoteException{
		System.out.println("Name: " + name);
		System.out.println("Email: " + email);

		//Get the current item according to the given ID
		Item item = items.get(auction_id);
		
		//Update bid
		items.put(auction_id, new Item(auction_id, item.getItemName(), placeBid, item.getReservePrice()));
	};

	//browse a list of currently active auctions with their current highest bid (But not the reserved price, which is secret.)
	public Map<Integer, Item> getItems() throws RemoteException{
		return items;
	};
}