import java.rmi.*;
import java.rmi.server.*;
import java.net.MalformedURLException;
import java.util.*;

public class AuctionServer
{
	protected static KeyManager keyManager = null;
	protected static Map<Integer, User> users = new HashMap<Integer, User>();

	public static void main(String[] args)
	{
		try{
			//Instantiate remote object
			Auction a = new AuctionImpl();

			//Initilize RSA key
			keyManager = new KeyManager();

			//Populate users table
			users.put(users.size(), new User(users.size(), "john", "john@doe.com", keyManager.password("password")));
			users.put(users.size(), new User(users.size(), "foo", "foo@bar.com", keyManager.password("password")));
			users.put(users.size(), new User(users.size(), "user", "user@bar.com", keyManager.password("password")));
			users.put(users.size(), new User(users.size(), "user1", "user1@bar.com", keyManager.password("password")));
			users.put(users.size(), new User(users.size(), "user2", "user2@bar.com", keyManager.password("password")));
			users.put(users.size(), new User(users.size(), "user3", "user3@bar.com", keyManager.password("password")));
			users.put(users.size(), new User(users.size(), "user4", "user4@bar.com", keyManager.password("password")));
			users.put(users.size(), new User(users.size(), "user5", "user5@bar.com", keyManager.password("password")));

			//Register with RMIRegistry
			Naming.rebind("AuctionService", a);
			System.out.println("Server reporting for duty.");



		}catch(Exception e){
			System.out.println("Server Error: " + e);
		}
	}

}