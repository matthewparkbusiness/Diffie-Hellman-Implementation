package com.mp.utilities;

/**
 * Matthew Park
 * 7/6/19
 * Cipher that allows both Diffie-Hellman and XOR encryption. Once passed
 * a message, this cipher can both encrypt and decrypt using a symmetric, one-way
 * encryption rule. This class is subject to updates.
 * 
 */

import java.util.Random;

public class XORCipher {

	public long publicMod = 23;
	public long publicBase = 5;
	
	private long currentPrivateKey;
	private long currentSecret;
	
	public long getPrivateKey() {
		return currentPrivateKey;
	}
	
	public long getSecret() {
		return currentSecret;
	}
	
	/*
	 * Determines both E(x) and D(E(x))
	 */
	public long determineExchange() {
		long keyThreshold = (int)(Math.log(Long.MAX_VALUE)/Math.log(publicBase))-1;
		currentPrivateKey = (long)(Math.random()*keyThreshold/2)+1;
		
		return (long) (Math.pow(publicBase, currentPrivateKey) % publicMod);
	}
	
	/*
	 * Encrypts or decrypts using XOR cipher
	 */
	public void determineSecret(long exchange) {
		currentSecret = (long) (Math.pow(exchange, currentPrivateKey) % publicMod);
	}
	
	public byte processByte(byte b) {
		return (byte) (b ^ currentSecret);
	}
	
	public byte[] processByteArray(byte[] b) {
		byte[] encoded = new byte[b.length];
		for(int i=0;i<b.length;i++) {
			encoded[i] = processByte(b[i]);
		}
		return encoded;
	}
	
	
}
