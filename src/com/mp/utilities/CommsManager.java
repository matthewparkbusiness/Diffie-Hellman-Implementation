package com.mp.utilities;

/**
 * Matthew Park
 * 7/6/19
 * Interface for a class that wants to facilitate socket communications.
 * 
 */

public interface CommsManager {

	public void init();
	
	public void send(byte[] info, int type, String meta);
	
	public void setReceiver(CommsReceiver receiver);
	
}
