import java.rmi.*;
import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

public class BuyersClient implements Serializable
{
	public static void main(String[] args)
	{
		try{
			Auction a = (Auction) Naming.lookup("AuctionService");
			System.out.println("----------Item List----------");
			
			for(Map.Entry<Integer, Item> entry : a.getItems().entrySet()){
				System.out.println(entry.getKey());
				System.out.println(entry.getValue().getItemName());
			} 

		}catch(Exception e){
			e.printStackTrace();
		}

	}
}