import java.rmi.*;
import java.rmi.server.*;
import java.net.MalformedURLException;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

import java.util.*;
import java.io.*;
import java.nio.*;
import org.jgroups.blocks.*;

public class AuctionImpl extends UnicastRemoteObject implements Auction
{

	public int randomNumber = 0;

	public AuctionImpl() throws RemoteException{};

	//Enable a seller to create a new auction.
	//@param {int} startingPrice - Aka. current bid or highest bid.
	//@param {int} reservePrice - Aka. Minimum acceptable price.
	//@return - A unique auction identifier for the newly created auction.
	public synchronized int createAuction(int owner_id, String itemName, int startingPrice, int reservePrice) throws RemoteException, Exception{

		Map<Integer, User> usersTable = AuctionServer.users;
		
		User user = usersTable.get(owner_id);
		
		int auction_id = AuctionServer.items.size();

		Item item = new Item(auction_id, owner_id, itemName, startingPrice, reservePrice);
		
		AuctionServer.disp.callRemoteMethods(null, "createAuction", new Object[]{item}, new Class[]{Item.class}, new RequestOptions(ResponseMode.GET_ALL, 5000));

		return auction_id;

	};

	// Close an auction
	// @param {int} auction_id - auction that you want to close.
	// @return - Winner's details.
	public Boolean closeAuction(int user_id, int auction_id) throws Exception{
		
		Map<Integer, User> usersTable = AuctionServer.users;
		User user = usersTable.get(user_id);

		//Get the current item according to the given ID
		Item item = AuctionServer.items.get(auction_id);

		//Get the ownerId of the item
		int ownerId = item.getOwnerId();

		//Check if the request is from the item owner
		if(ownerId == user_id){
			
			AuctionServer.disp.callRemoteMethods(null, "closeAuction", new Object[]{user_id, auction_id}, new Class[]{int.class, int.class}, new RequestOptions(ResponseMode.GET_ALL, 5000));
						
			return true;
		}

		return false;

	};

	public Bidder getWinner(int auction_id) throws RemoteException{
		return AuctionServer.bidders.get(auction_id);
	}

	
	//----- Potential Buyers -----//
	//Enable potential buyers to bid for auctioned items.
	public synchronized void bid(int user_id, int auction_id, int new_bid) throws Exception{
		
		Map<Integer, User> usersTable = AuctionServer.users;
		User user = usersTable.get(user_id);

		//Get the current item according to the given ID
		Item item = AuctionServer.items.get(auction_id);

		//If item is active
		if(item.getActive()){
			
			//Update new higest bid in items table
			//Add new bidder to bidders table			
			AuctionServer.disp.callRemoteMethods(null, "bid", new Object[]{user_id, auction_id, new_bid}, new Class[]{int.class, int.class, int.class}, new RequestOptions(ResponseMode.GET_ALL, 5000));
			
		}
		
	};

	//browse a list of currently active auctions with their current highest bid (But not the reserved price, which is secret.)
	public Map<Integer, Item> getItems() throws RemoteException{
		return AuctionServer.items;
	};

	//List bidders of a specific auction 
	public Map<Integer, Bidder> getBidders() throws RemoteException{
		return AuctionServer.bidders;
	};


	//CRYPTO CLIENT AUTHENTICATION
	public Key getPublicKey() throws RemoteException{
		return AuctionServer.keyManager.getPublicKey();
	};

	public synchronized byte[] getChallenge(byte[] cipherSharedKey) throws RemoteException, Exception{
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, AuctionServer.keyManager.getPrivateKey());
		byte[] sKeyBytes = cipher.doFinal(cipherSharedKey);

		//Turn 'bytes' key back to 'SecretKey' object
		SecretKey sKey = new SecretKeySpec(sKeyBytes, 0, sKeyBytes.length, "AES");

		//Use the shared key to createChallenge
		Cipher cipherAES = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipherAES.init(Cipher.ENCRYPT_MODE, sKey);
		
		//Generate random number
		randomNumber = (int) Math.floor(Math.random() * 101);
		byte[] randomNumberBytes = ByteBuffer.allocate(4).putInt(randomNumber).array();
		
		byte[] cipherChallenge = cipherAES.doFinal(randomNumberBytes);

		return cipherChallenge;
		
	}

	//Check if the server's random number is the same as the received number from the client
	public boolean verify(int challengeNum) throws RemoteException{
		return randomNumber == challengeNum;
	}

	//ACCESS CONTROL
	public User login(User checkUser) throws RemoteException{
		Map<Integer, User> usersTable = AuctionServer.users;

		for(int i=0; i < usersTable.size(); i++){
			User user = usersTable.get(i);

			//Return if user exists in the database
			Boolean userExist = (checkUser.getName()).equals(user.getName());
			Boolean correctPassword = (checkUser.getPassword()).equals(user.getPassword());

			if(userExist && correctPassword){
				System.out.println("> User { " + user.getName() + " } is logged in.");
				return user;
			}
		}

		return null;
	}

}