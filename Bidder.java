import java.io.*;
import java.util.*;

public class Bidder implements Serializable
{
	private int auction_id;
	private String name;
	private String email;

	public Bidder (int auction_id, String name, String email)
	{
		this.auction_id = auction_id;
		this.name = name;
		this.email = email;
	}

	public int getAuctionId()
	{
		return auction_id;
	}

	public String getName()
	{
		return name;
	}

	public String getEmail()
	{
		return email;
	}

}
