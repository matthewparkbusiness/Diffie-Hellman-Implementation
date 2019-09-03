package com.mp.utilities;

/**
 * Matthew Park
 * 7/6/19
 * Interface for a class that wants to facilitate socket receiving.
 * 
 */

public interface CommsReceiver {

	public static int FILE_TYPE = 0;
	public static int STRING_TYPE = 1;
	
	public void messageReceived(byte[] information, int type, String meta);
	
}
