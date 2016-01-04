import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.*;

public class AuthClient{

	private Auction auth = null;

	public AuthClient(Auction auth){
		this.auth = auth;
	};

	public Boolean authenticated() throws Exception{
		
		//1. Get public key from Auth interface (Asymmetric)
		System.out.println("...Getting public key from server");
		Key publicKey = (Key) auth.getPublicKey();

		//2. Generate AES shared key (Symmetric)
		System.out.println("...Generating shared key");
		SecretKey sharedKey = genSharedKey();

		//3. Encrypt AES shared key with RSA public key (Symmetric)
		System.out.println("...Encrypting shared key with public key");
		byte[] cipherSharedKey = getCipherRSA(publicKey, sharedKey);

		//4a. Send the encrypted shared key to server			
		//4b. Server decrypts the encrypted shared key with its RSA private key
		//4c. Server encrypts the challenge with the shared key
		//4d. Server returns cipher challenge		
		System.out.println("...Requesting challenge");
		byte[] cipherChallenge = auth.getChallenge(cipherSharedKey);

		//5. Convert the received cipher challenge into bytes
		byte[] challengeBytes = getChallengeBytes(cipherChallenge, sharedKey);

		//6. Convert challenge from bytes to Integer
		int challengeNum = byteToInt(challengeBytes);

		//7a. Sends the challengeNum to server
		//7b. Server verifies if the received number == number generated before 
		System.out.println("...Verifying response");
		Boolean authenticated = auth.verify(challengeNum);

		System.out.println("Authenticated: " + authenticated);
		System.out.println("-------------------");
		return authenticated;
	}

	private SecretKey genSharedKey() throws NoSuchAlgorithmException{
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);
		return keyGen.generateKey();
	}

	private byte[] getCipherRSA(Key publicKey, SecretKey sharedKey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal((byte[])sharedKey.getEncoded());
	}

	private byte[] getChallengeBytes(byte[] cipherChallenge, SecretKey sharedKey) throws Exception{
		Cipher cipherAES = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipherAES.init(Cipher.DECRYPT_MODE, sharedKey);
		return cipherAES.doFinal(cipherChallenge);
	}

	private int byteToInt(byte[] challengeBytes){
		ByteBuffer wrapped = ByteBuffer.wrap(challengeBytes);
		return wrapped.getInt();
	}

	//Access Control
	public String password(String password) throws Exception
	{
		byte[] plaintText = password.getBytes();
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
		messageDigest.update(plaintText);
		
		return new String(messageDigest.digest());
	}
}