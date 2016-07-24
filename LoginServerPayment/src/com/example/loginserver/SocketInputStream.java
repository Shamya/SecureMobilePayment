package com.example.loginserver;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

public class SocketInputStream implements InputStream {

	private SSLServerSocket serverSocket = null;
	private SSLSocket client;
	private SecureProtocol protocol;

	SocketInputStream(SecureProtocol s){
		this.protocol = s;
	}

	@Override
	public void init() {
		int socket = Integer.parseInt("8085");
		String keystore = "server_key.jks";
		char keystorepass[] = "javablog.fr".toCharArray();
		char keypassword[] = "javablog.fr".toCharArray();
		java.lang.System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
		while(true){
			try {
				KeyStore ks = KeyStore.getInstance("JKS");
				ks.load(new FileInputStream(keystore),keystorepass);
				KeyManagerFactory kmf = 
						KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				kmf.init(ks, keypassword);

				serverSocket = this.protocol.createSocket(kmf, socket);

				return;

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Could not listen on port "+socket);
				//System.exit(-1);
			} catch (KeyStoreException e) {
				System.out.println("Could not get key store");
				//System.exit(-1);
			} catch (NoSuchAlgorithmException e) {
				System.out.println("There is no algorithm in ks.load");
				e.printStackTrace();
				//System.exit(-1);
			} catch (CertificateException e) {
				e.printStackTrace();
				//System.exit(-1);
			} catch (UnrecoverableKeyException e) {
				System.out.println("kmf.init() no key");
				//System.exit(-1);
			}
		}

	}

	@Override
	public String read() {

		while(true){
			try {
				this.client = (SSLSocket) this.serverSocket.accept();
				System.out.println("client connected");

				// Get input from the client
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

				String input = in.readLine();
				return input;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void write(String op) {
		try {
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			out.println(op);

		} catch (IOException e) {
			//System.out.println("Accept failed on "+socket);
			e.printStackTrace();
			//System.exit(-1);
		}

	}

}