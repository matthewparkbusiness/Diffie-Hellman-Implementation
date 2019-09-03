package com.mp.utilities;

/**
 * Matthew Park
 * 7/6/19
 * Socket manager that allows for messages to be encrypted and decrypted 
 * between ChatClient and ChatServer. Uses Cipher classes to compute E(x)
 * and D(E(x)). 
 * 
 */

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class EncryptedServerSocketManager extends ServerSocketManager{

	private XORCipher cipher;
	private Thread currentFileTransferThread;
	
	
	/*
	 * Initializes main components of socket and cipher
	 * @see com.mp.utilities.ServerSocketManager#init()
	 */
	public void init() {
		super.init();
		cipher = new XORCipher();
		
		String exchangeMessage = cipher.determineExchange() + SocketManager.ACTION_SEQUENCE + "EXCHANGE_TYPE";
		pw.println(exchangeMessage);
		pw.flush();
	}
	
	/*
	 * Send information
	 * @see com.mp.utilities.ServerSocketManager#send(byte[], int, java.lang.String)
	 */
	public void send(byte[] info, int type, String meta) {
		try {
			if(type == CommsReceiver.STRING_TYPE) {
				os.write(cipher.processByteArray(info));
				os.flush();
			}
			else if(type == CommsReceiver.FILE_TYPE) {
				if(currentFileTransferThread != null) {
					// can't transfer more than one file at a time
					return;
				}
				currentFileTransferThread = new Thread(new Runnable() {
					
					public void run() {
						synchronized (currentFileTransferThread){
							try {
								os.write(cipher.processByteArray((meta + SocketManager.ACTION_SEQUENCE + "FILE_TYPE").getBytes()));
								os.flush();
								
								try {
									currentFileTransferThread.wait();
								} catch (InterruptedException e) {
									System.err.println("File transfer failed");
								}
								
								os.write(cipher.processByteArray(info));
								os.flush();
								
							}
							catch(IOException e) {
								System.err.println("Error transfering last file.");
							}
						}
					}
					
				});
				currentFileTransferThread.start();
				
				
			}
		}
		catch (IOException e) {
			System.err.println("Error sending last information.");
		}
	}

	
	/*
	 * Manages file receiving asynchronously
	 * @see com.mp.utilities.ServerSocketManager#run()
	 */
	public void run() {
		try {
		
			String encryptionInformation = br.readLine();
			if(encryptionInformation.contains(SocketManager.ACTION_SEQUENCE + "EXCHANGE_TYPE")) {
				cipher.determineSecret(Long.parseLong(encryptionInformation.substring(0, encryptionInformation.indexOf(SocketManager.ACTION_SEQUENCE))));
			}
			GUIManager.setHeader(cipher.publicMod, cipher.publicBase, cipher.getPrivateKey(), cipher.getSecret());
		
		}
		catch(IOException e) {
			System.err.println("Error processing encryption data.");
		}
		
		String line = null;
		while(true) {
			byte[] inputData = readData();
			line = new String(inputData);
			
			if(!line.contains(SocketManager.ACTION_SEQUENCE)) {
				receiver.messageReceived(line.getBytes(), CommsReceiver.STRING_TYPE, null);
			}
			
			else if(line.contains(SocketManager.ACTION_SEQUENCE + "VERIFIED")){
				if(currentFileTransferThread != null) {
					synchronized(currentFileTransferThread) {
						currentFileTransferThread.notify();
					}
				}
			}
			
			else if(line.contains(SocketManager.ACTION_SEQUENCE + "FILE_TYPE")){
				
				send((SocketManager.ACTION_SEQUENCE + "VERIFIED").getBytes(), CommsReceiver.STRING_TYPE, null);
				byte[] decoded = readData();
				receiver.messageReceived(decoded, CommsReceiver.FILE_TYPE, line.substring(0, line.indexOf(SocketManager.ACTION_SEQUENCE)));
				
			}
			
			
		}
		
	}
	
	
	/*
	 * Read file information from socket and decrypt.
	 */
	public byte[] readData() {
		ByteArrayOutputStream baos = null;
		byte[] bucket = new byte[100];
		int read = 0;
		
		try {
			while(is.available() != 0 || baos == null) {
				
				if(is.available() != 0) {
					if(baos == null) {
						baos = new ByteArrayOutputStream();
					}
					read = is.read(bucket);
					baos.write(bucket, 0, read);
				}
				
			}
		}
		catch(IOException e) {
			System.err.println("Error in last file stream.");
		}
		return cipher.processByteArray(baos.toByteArray());
	}

}
