package com.mp.diffiehellmanclient;

/**
 * Matthew Park
 * 7/5/19
 * Client Application for the Diffie-Hellman encryption chat server. Coordinates
 * GUI and socket connection for the client side. 
 * 
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.mp.utilities.CommsManager;
import com.mp.utilities.CommsReceiver;
import com.mp.utilities.XORCipher;
import com.mp.utilities.EncryptedSocketManager;
import com.mp.utilities.GUIManager;
import com.mp.utilities.SocketManager;

public class ChatClient implements CommsReceiver, ActionListener{
	
	
	private CommsManager manager;
	
	/*
	 * Initializes main components of client side.
	 */
	public ChatClient() {
		manager = new EncryptedSocketManager();
		manager.setReceiver(this);
		
		GUIManager.input.addActionListener(this);
		GUIManager.fileButton.addActionListener(this);
		
		manager.init();
		
	}

	/*
	 * Listener for messages received
	 * @see com.mp.utilities.CommsReceiver#messageReceived(byte[], int, java.lang.String)
	 * 
	 */
	public void messageReceived(byte[] information, int type, String meta) {
		if(type == CommsReceiver.STRING_TYPE) {
			GUIManager.addText("Guest: " + new String(information));
		}
		else if(type == CommsReceiver.FILE_TYPE) {
			
			JOptionPane.showConfirmDialog(null, "Received file " + meta + ".\nWould you like to save it?");
			
			JFileChooser fc = new JFileChooser();
			fc.setSelectedFile(new File(meta));
			int returnVal = fc.showSaveDialog(null);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	try {
		            File file = fc.getSelectedFile();
		            FileOutputStream fos = new FileOutputStream(file);
		            fos.write(information);
		            fos.flush();
		            fos.close();
	        	}
	        	catch(IOException ioe) {
	        		System.err.println("Error downloading file.");
	        	}
	        }
			
		}
	}

	/*
	 * Listener for textfield and button actions.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if(actionCommand.equals("textfield")) {
			
			//send message
			String toOutput = GUIManager.input.getText();
			manager.send(toOutput.getBytes(), CommsReceiver.STRING_TYPE, null);
			GUIManager.input.setText("");
			GUIManager.addText("Me: " + toOutput);
		}
		else if(actionCommand.equals("file")) {
			
			//save file option
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(null);

	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	try {
		            File file = fc.getSelectedFile();
		            byte[] bucket = new byte[300];
		            
		            FileInputStream fis = new FileInputStream(file);
		            ByteArrayOutputStream baos = new ByteArrayOutputStream();
		            
		            int read = 0;
		            while(read != -1) {
		            	read = fis.read(bucket);
		            	baos.write(bucket, 0, bucket.length);
		            }
		            
					manager.send(baos.toByteArray(), CommsReceiver.FILE_TYPE, file.getName());
					GUIManager.addText("Uploaded  " + file.getName());
	        	}
	        	catch(IOException ioe) {
	        		System.err.println("Error uploading file.");
	        	}
	        }
			
			
		}
		
	}

}
