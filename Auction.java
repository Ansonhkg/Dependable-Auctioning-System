import java.rmi.*;
import java.util.*;

public interface Auction extends Remote{
	

	public int createAuction(String itemName, int startingPrice, int minAcceptablePrice) throws RemoteException;

	// public String closeAuction(int auction_id) throws RemoteException;

	public void bid(int auction_id) throws RemoteException;

	public Map<Integer, Item> getItems() throws RemoteException;
}