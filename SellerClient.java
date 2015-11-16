import java.rmi.*;
import java.net.MalformedURLException;
public class SellerClient
{
	public static void main(String[] args)
	{
		try{
			Auction a = (Auction) Naming.lookup("AuctionService");
			int auction_id = a.createAuction("First Item", 123, 456);
			System.out.println("Return auction id is " + auction_id);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}