import java.rmi.*;
import java.rmi.server.*;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public final class KeyManager
{
	private Key privateKey = null; //This key stays on the server
	private Key publicKey = null; //This key is ready to be distributed to clients

	public KeyManager() throws RemoteException, NoSuchAlgorithmException{

		System.out.println("...Generating RSA key pair");
		KeyPair kp = this.genKeyPair();

		System.out.println("...Generating public key");
		this.publicKey = kp.getPublic();

		System.out.println("...Generating private key");
		this.privateKey = kp.getPrivate();
	};

	public Key getPublicKey()
	{
		return publicKey;
	}

	protected Key getPrivateKey()
	{
		return privateKey;
	}

	public KeyPair genKeyPair() throws RemoteException, NoSuchAlgorithmException
	{
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(1024);
		return (KeyPair) kpg.genKeyPair();
	}

	public String password(String password) throws Exception
	{
		byte[] plaintText = password.getBytes();
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		messageDigest.update(plaintText);
		
		return new String(messageDigest.digest());
	}

}