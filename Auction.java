import java.rmi.*;
import java.util.*;

public interface Auction extends Remote{
	

	public int createAuction(String itemName, int startingPrice, int reservePrice) throws RemoteException;

	// public String closeAuction(int auction_id) throws RemoteException;

	public void bid(String name, String email, int auction_id, int placeBid) throws RemoteException;

	public Map<Integer, Item> getItems() throws RemoteException;
}