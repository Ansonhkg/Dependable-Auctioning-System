import java.io.*;

import java.security.*;
import javax.crypto.*;

public class User implements Serializable
{
	private int id;
	private String name = null;
	private String email = null;
	private String password = null;

	//Registration 
	public User(int id, String name, String email, String password)
	{
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	//Login
	public User(String name, String password)
	{
		this.name = name;
		this.password = password;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getEmail()
	{
		return email;
	}

	public String getPassword()
	{
		return password;
	}

}