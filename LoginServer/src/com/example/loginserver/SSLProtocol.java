package com.example.loginserver;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

public class SSLProtocol implements SecureProtocol {

	@Override
	public SSLServerSocket createSocket(KeyManagerFactory kmf, int socket) {
		SSLServerSocket serverSocket = null;
		try {
			SSLContext sslcontext = 
					SSLContext.getInstance("TLS");

			sslcontext.init(kmf.getKeyManagers(), null, null);

			ServerSocketFactory ssf = 
					sslcontext.getServerSocketFactory();

			serverSocket = (SSLServerSocket)ssf.createServerSocket(socket);
			serverSocket.setEnabledCipherSuites(serverSocket.getSupportedCipherSuites());

			System.out.println("Starting login server...");
			return serverSocket;
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not listen on port "+socket);
			//System.exit(-1);
		} catch (KeyManagementException e) {
			System.out.println("sslcontext.init keymanagementexception");
			//System.exit(-1);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("There is no algorithm in ks.load");
			e.printStackTrace();
			//System.exit(-1);
		}
		return null;
	}
}
