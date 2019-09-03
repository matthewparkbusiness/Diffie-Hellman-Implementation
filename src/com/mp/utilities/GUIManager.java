package com.mp.utilities;


/**
 * Matthew Park
 * 7/8/19
 * Regulates all GUI in both client and server. Responsible for swing
 * frameworks and event handling.
 * 
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class GUIManager {

	private static JEditorPane textPane;
	
	public static JTextField input;
	public static JTextField variablesField;
	public static JButton fileButton;
	public static JButton imageButton;
	
	static {
		
		JFrame frame = new JFrame();
		frame.setSize(400, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		textPane = new JEditorPane();
		textPane.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		frame.add(scrollPane, BorderLayout.CENTER);
		
		input = new JTextField();
		input.setActionCommand("textfield");
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		panel.add(input, BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 1));
		
		fileButton = new JButton("File Trans");
		fileButton.setActionCommand("file");
		buttonsPanel.add(fileButton);
		panel.add(buttonsPanel, BorderLayout.EAST);
		
		frame.add(panel, BorderLayout.SOUTH);
		frame.setVisible(true);
		
		variablesField = new JTextField();
		variablesField.setEditable(false);
		
		frame.add(variablesField, BorderLayout.NORTH);
	}
	
	public static void addText(String text) {
		textPane.setText(textPane.getText() + text + "\n");
	}
	
	public static void setHeader(long p, long g, long key, long code) {
		variablesField.setText("p=" + p + ", g=" + g + ", key=" + key + ", code=" + code);
	}
	
	public static void main(String[] args) {
		new GUIManager();
	}
	
}
