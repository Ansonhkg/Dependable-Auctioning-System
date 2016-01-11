import java.rmi.*;
import java.rmi.server.*;
import java.net.MalformedURLException;
import java.io.*;
import java.util.*;

import org.jgroups.*;
import org.jgroups.util.*;
import org.jgroups.blocks.*;
import org.jgroups.blocks.atomic.Counter;
import org.jgroups.blocks.atomic.CounterService;

public class AuctionServer extends ReceiverAdapter 
{
	protected static KeyManager keyManager;
	protected static Map<Integer, User> users = new HashMap<Integer, User>();
	protected static Map<Integer, Item> items = new HashMap<Integer, Item>();
	protected static Map<Integer, Bidder> bidders = new HashMap<Integer, Bidder>();
	
	private static final String CONFIG_PATH = "config.xml";
    private static final String CLUSTER_NAME = "Server";

    private static CmdHelper cmdHelper;
    public static JChannel chan;

    public static RpcDispatcher disp;

    //MAIN
	public static void main(String[] args) throws Exception
	{
    	new AuctionServer().start();
	}


	//CONSTRUCTOR
    public void start() throws Exception
    {
    	chan = new JChannel(CONFIG_PATH);

		disp = new RpcDispatcher(chan, this, this, this);

        CounterService counter_service = new CounterService(chan);

        chan.connect(CLUSTER_NAME);

        Counter counter = counter_service.getOrCreateCounter("mycounter", 1);        

        if(counter.get() == 1){

            System.out.println("FIRST SERVER IS ON");
            init();
            counter.incrementAndGet();

        }else{

            System.out.println("Another cluster..");
            System.out.println(counter.get());

        }
        
        chan.getState(null, 10000);

		System.out.println("Server reporting for duty.");
    }
			

	@Override
    public void receive(Message msg){

        Map<Integer, Item> tempItems = (Map<Integer, Item>) msg.getObject();

        synchronized(items){

            items.clear();

            items.putAll(tempItems);

        }

    }

    @Override
    public void viewAccepted(View new_view){

    	System.out.println("\nviewAccepted is called.");

    	try{
    		//Instantiate remote object
			Auction a = new AuctionImpl();

			//Register with RMIRegistry
			Naming.rebind("AuctionService", a);

    	}catch(Exception e){
    		System.out.println(e);
    	}
    	

        // System.out.println("\nView: " + new_view);
        System.out.println("View ID: " + new_view.getViewId());
        System.out.println("Creator: " + new_view.getCreator());
        System.out.println("Members: " + new_view.getMembers());
        System.out.println("Size: " + new_view.size());

    }

    @Override
    public void getState(OutputStream output) throws Exception {

    	System.out.println("\nGETSTATE is called.");
    	
        //Since access to state may be concurrent, 
        //we synchronize it. Then we call Util.objectToStream() which is a JGroups utility method writing an object to an output stream.
        synchronized(keyManager){
            Util.objectToStream(keyManager, new DataOutputStream(output));
        }

        synchronized(users){
            Util.objectToStream(users, new DataOutputStream(output));
        }

        synchronized(items){
            Util.objectToStream(items, new DataOutputStream(output));
        }

        synchronized(bidders){
            Util.objectToStream(bidders, new DataOutputStream(output));
        }

    }

    @Override
    public void setState(InputStream input) throws Exception {

    	System.out.println("\nSETSTATE is called.");

        KeyManager tempKey;

        tempKey = (KeyManager)Util.objectFromStream(new DataInputStream(input));

        keyManager = tempKey;


        Map<Integer, User> tempUsers;

        tempUsers = (Map<Integer, User>)Util.objectFromStream(new DataInputStream(input));

        synchronized(users){

            users.clear();

            users.putAll(tempUsers);

        }

        Map<Integer, Item> tempItems;

        tempItems = (Map<Integer, Item>)Util.objectFromStream(new DataInputStream(input));

        synchronized(items){

            items.clear();

            items.putAll(tempItems);

        }

        Map<Integer, Bidder> tempBidders;

        tempBidders = (Map<Integer, Bidder>)Util.objectFromStream(new DataInputStream(input));

        synchronized(bidders){

            bidders.clear();

            bidders.putAll(tempBidders);

        }

    }

	public void init() throws Exception
	{
		//Initilize RSA key
		keyManager = new KeyManager();

		//Populate users table
		users.put(users.size(), new User(users.size(), "john", "john@doe.com", keyManager.password("password")));
		users.put(users.size(), new User(users.size(), "user", "user@bar.com", keyManager.password("password")));
		users.put(users.size(), new User(users.size(), "user1", "user1@bar.com", keyManager.password("password")));
		users.put(users.size(), new User(users.size(), "user2", "user2@bar.com", keyManager.password("password")));
		users.put(users.size(), new User(users.size(), "user3", "user3@bar.com", keyManager.password("password")));
		users.put(users.size(), new User(users.size(), "user4", "user4@bar.com", keyManager.password("password")));
		users.put(users.size(), new User(users.size(), "user5", "user5@bar.com", keyManager.password("password")));
	}


	//AUCTION CONTROLLER
	public void createAuction(Item item)
	{
		String ownerName = users.get(item.getOwnerId()).getName();

		items.put(items.size(), item);

		System.out.println("> User { " + ownerName + " } created new auction { (" + item.getAuctionId() + ") " + item.getItemName() + " }");
	}

	public void closeAuction(int user_id, int auction_id)
	{
		String ownerName = users.get(user_id).getName();

		Item item = items.get(auction_id);
		
		String itemName = item.getItemName();

		//Set item to inactive
		item.setActive(false);

		System.out.println("> User { " + ownerName + " } closed auction { (" + auction_id + ") " + itemName + " }");
	}

	public void bid(int user_id, int auction_id, int new_bid)
	{
		Item item = items.get(auction_id);
		User user = users.get(user_id);
		
		int oldBid = item.getCurrentBid();

		//Update new higest bid in items table
		item.setWinnerId(user_id);
		item.setCurrentBid(new_bid);

		//Add new bidder to bidders table
		bidders.put(auction_id, new Bidder(auction_id, user));

		System.out.println("> User { " + user.getName() + " } has bid item { " + item.getItemName() + " } from " + oldBid + " to " + item.getCurrentBid());
	}

	public void update_OLD(Object obj)
	{
        try{

            Message msg = new Message(null, null, obj);
            chan.send(msg);

        }catch(Exception e){
            chan.close();
        }
	}

}