package com.mp.diffiehellmantesting;

/**
 * Matthew Park
 * 7/8/19
 * JUnit test for chat encryption rule. Tests validity of EncryptedSocketManager.
 * 
 */

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mp.utilities.XORCipher;

class CipherTest {

	XORCipher clientCipher;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		clientCipher = new XORCipher();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		// Confirm whether cipher fulfills the requirements of an encryption rule
		// 1) Must be correct. As in D(E(x)) = x must be valid for all inputs.
		// 2) Must be secure. No third-parties should be able to retreive x based on E(x)
		
		long clientExchangeInformation = clientCipher.determineExchange();
		
		XORCipher serverCipher = new XORCipher();
		long serverExchangeInformation = serverCipher.determineExchange();
		
		clientCipher.determineSecret(serverExchangeInformation);
		serverCipher.determineSecret(clientExchangeInformation);

		assertEquals(clientCipher.getSecret(), serverCipher.getSecret(), "Client and server secrets must be equal.");
		
	}

}
