import java.io.*;
import java.util.*;

public class Item implements Serializable
{
	private int auction_id;
	private int owner_id;
	private int winner_id;
	
	private String itemName;
	private int currentBid;
	private int reservePrice;
	private boolean active;

	public Item (int auction_id, int owner_id, String itemName, int currentBid, int reservePrice)
	{
		this.auction_id = auction_id;
		this.owner_id = owner_id;
		this.itemName = itemName;
		this.currentBid = currentBid;
		this.reservePrice = reservePrice;
		this.active = true;
	}

	public int getAuctionId()
	{
		return auction_id;
	}

	public int getOwnerId()
	{
		return owner_id;
	}

	public int getWinnerId()
	{
		return winner_id;
	}

	public String getItemName()
	{
		return itemName;
	}

	public int getCurrentBid()
	{
		return currentBid;
	}

	public int getReservePrice()
	{
		return reservePrice;
	}

	public boolean getActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public void setWinnerId(int user_id){
		this.winner_id = user_id;
	}

	public void setCurrentBid(int new_bid){
		this.currentBid = new_bid;
	}
}
