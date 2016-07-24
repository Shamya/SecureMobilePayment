package com.example.loginserver;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLServerSocket;

public interface SecureProtocol {
	public SSLServerSocket createSocket(KeyManagerFactory kmf, int socket);
}
