# Diffie-Hellman-Implementation

Implementation of the Diffie-Hellman encryption algorithm through a chat application. This application is capable of sending both string and file messages that are encrypted over a network and decrypted when received. The primary idea behind the Diffie-Hellman encryption is that the message cannot be decrypted without any knowledge of the private keys due to the properties of prime numbers in modular arithmetic. The encryption I used uses small prime numbers, however, larger prime numbers should be used in place of them. The idea behind the idea is very simple. 

Suppose the server has a secret m and wishes to send this value to client. The server and client must publicly agree on prime numbers p and e. The server then determines its own secret integer s, and the client determines its own secret integer c. The server computes e^s (mod p) and sends this value (let's call this value a) to the client. The client then computes e^c (mod p) and sends this value (let's call this value b) to the server. When the server computes b^s (mod p) and the client computes a^c (mod p), they both share a secret due to the nature of exponents in GF(p). Now that both sides share a secret number, an encryption rule can be used with this number to encrypt messages. 

Application Preview:

