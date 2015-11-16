import java.rmi.*;
import java.rmi.server.*;
import java.net.MalformedURLException;
import java.util.*;

public class AuctionImpl extends UnicastRemoteObject implements Auction
{

	public Map<Integer, Item> items = new HashMap<Integer, Item>();

	public AuctionImpl() throws RemoteException{};

	//Enable a seller to create a new auction.
	//@param {int} startingPrice.
	//@param {int} minAcceptablePrice.
	//@return - A unique auction identifier for the newly created auction.
	public int createAuction(String itemName, int startingPrice, int minAcceptablePrice) throws RemoteException{

		System.out.println("createAuction Called");
		
		int auction_id = items.size();

		items.put(auction_id, new Item(auction_id, itemName, startingPrice, minAcceptablePrice));

		return auction_id;
	};

	//Close auction and return winner's details.
	//@param {int} auction_id - auction that you want to close.
	//@return - Winner's details.
	// public String closeAuction(int auction_id) throws RemoteException{

	// };

	
	//----- Potential Buyers -----//
	//Enable potential buyers to bid for auctioned items.
	public void bid(int auction_id) throws RemoteException{

	};

	//browse a list of currently active auctions with their current highest bid (But not the reserved price, which is secret.)
	public Map<Integer, Item> getItems() throws RemoteException{
		return items;
	};
}