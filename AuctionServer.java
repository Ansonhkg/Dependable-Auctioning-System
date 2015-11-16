import java.rmi.*;
import java.rmi.server.*;
import java.net.MalformedURLException;

public class AuctionServer
{
	public static void main(String[] args)
	{
		try{
			//Instantiate remote object
			Auction a = new AuctionImpl();

			//Register with RMIRegistry
			Naming.rebind("AuctionService", a);
			System.out.println("Server reporting for duty.");
			
		}catch(Exception e){
			System.out.println("Server Error: " + e);
		}
	}
}