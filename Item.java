import java.io.*;

public class Item implements Serializable
{
	private int auction_id;
	private String itemName;
	private int startingPrice;
	private int reservePrice;

	public Item (int auction_id, String itemName, int startingPrice, int reservePrice)
	{
		this.auction_id = auction_id;
		this.itemName = itemName;
		this.startingPrice = startingPrice;
		this.reservePrice = reservePrice;
	}

	public int getAuctionId()
	{
		return auction_id;
	}

	public String getItemName()
	{
		return itemName;
	}

	public int getStartingPrice()
	{
		return startingPrice;
	}

	public int getReservePrice()
	{
		return reservePrice;
	}
}