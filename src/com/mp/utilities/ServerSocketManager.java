package com.mp.utilities;

/**
 * Matthew Park
 * 7/6/19
 * Socket manager that allows for simple messages to be passed between
 * ChatClient and ChatServer. 
 * 
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ServerSocketManager implements CommsManager, Runnable{

	public static int portNumber = SocketManager.portNumber;
	
	protected Socket socket;
	protected InputStream is;
	protected BufferedReader br;
	
	protected OutputStream os;
	protected PrintWriter pw;
	
	protected CommsReceiver receiver;

	
	/*
	 * Initializes all socket components
	 * @see com.mp.utilities.CommsManager#init()
	 */
	public void init() {
		try {
			ServerSocket serverSocket = new ServerSocket(portNumber);
			socket = serverSocket.accept();
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			
			os = socket.getOutputStream();
			pw = new PrintWriter(os);

			new Thread(this).start();
		}
		catch(IOException e) {
			System.err.println("Socket error. Check port 5151 and if specified ip address exists.");
		}
		
	}

	/*
	 * Sends simple message without encryption.
	 * @see com.mp.utilities.CommsManager#send(byte[], int, java.lang.String)
	 */
	public void send(byte[] info, int type, String meta) {
		try {
			os.write(info);
		}
		catch (IOException e) {
			System.err.println("Error sending last information.");
		}
	}

	/*
	 * Initialize receiver.
	 * @see com.mp.utilities.CommsManager#setReceiver(com.mp.utilities.CommsReceiver)
	 */
	public void setReceiver(CommsReceiver r) {
		receiver = r;
	}
	
	/*
	 * Allows for messages to be received asynchronously. 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		
		String line = null;
		ByteArrayOutputStream baos = null;
		
		byte[] bucket = new byte[1000];
		int read = 0;
		while(true) {
			try {
				line = br.readLine();
			} catch (IOException e) {
				System.err.println("Error reading last line.");
			}
			
			
			if(!line.contains(">>>")) {
				receiver.messageReceived(line.getBytes(), CommsReceiver.STRING_TYPE, null);
			}
			else if(line.contains(">>>file_type")){
				
				baos = new ByteArrayOutputStream();
				while(true) {
					try {
						read = is.read(bucket);
						if(read == -1) {
							break;
						}
						baos.write(bucket);
					}
					catch(IOException e) {
						System.err.println("Error in last file stream.");
					}
				}
				receiver.messageReceived(baos.toByteArray(), CommsReceiver.FILE_TYPE, line.substring(0, line.indexOf(">>>")));
				
			}
			
			
		}
		
	}
	
}
