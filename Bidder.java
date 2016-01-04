import java.io.*;
import java.util.*;

public class Bidder implements Serializable
{
	private int auction_id;
	private User user;

	public Bidder (int auction_id, User user)
	{
		this.auction_id = auction_id;
		this.user = user;
	}

	public int getAuctionId()
	{
		return auction_id;
	}

	public User getUser()
	{
		return user;
	}

}
