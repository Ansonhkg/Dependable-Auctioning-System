import java.rmi.*;
import java.util.*;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

import java.io.*;

public interface Auction extends Remote{
	
	public int createAuction(int userId, String itemName, int startingPrice, int reservePrice) throws RemoteException;

	public Boolean closeAuction(int user_id, int auction_id) throws RemoteException;

	public Bidder getWinner(int auction_id) throws RemoteException;

	public void bid(int userId, int auction_id, int new_bid) throws RemoteException;

	public Map<Integer, Item> getItems() throws RemoteException;

	public Map<Integer, Bidder> getBidders() throws RemoteException;

	//Cryptography Authentication
	public Object getPublicKey() throws RemoteException;

	public byte[] getChallenge(byte[] cipherSharedKey) throws RemoteException, Exception;

	public boolean verify(int challengeNum) throws RemoteException;

	//Access Control
	public User login(User checkUser) throws RemoteException;

}