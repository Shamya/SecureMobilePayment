package com.example.mobilepaymentapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class SocketService extends Service{

	public static final String BROADCAST_RES = "broadcast result";
	public static final String SOCKET_SERVER_RES = "Socket server result";
	public static String msg = "";
	private String ip_address = "";
	private int port = 0;
    private static SSLSocket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private final String TAG = "TAG";
    private char keystorepass[] = "javablog.fr".toCharArray();
    private char keypassword[] = "javablog.fr".toCharArray();
    InputStream keyin;

	@Override
	public IBinder onBind(Intent intent) {
		//TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startid){

		Bundle mybundle = intent.getExtras();
		ip_address = mybundle.getString(LoginActivity.IPADDRESS);
		port = mybundle.getInt(LoginActivity.PORTNUMBER);
		msg = mybundle.getString(LoginActivity.MESSAGE);
		
		socket = null;
		Thread socketThread = new Thread(new ClientSocketThread());
		socketThread.start();

		return Service.START_NOT_STICKY;
	}

	private void connectToSecureSocket(){
    	try{
    		KeyStore keystore = KeyStore.getInstance("BKS");
    		keyin = getResources().openRawResource(R.raw.client_key);
    		keystore.load(keyin, keystorepass);
    		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    		keyManagerFactory.init(keystore, keypassword);

    		SSLContext sslContext = SSLContext.getInstance("TLS");
    		KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

    		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    		tmf.init(keystore);

    		sslContext.init(keyManagers, tmf.getTrustManagers(), null);
    		SSLSocketFactory socketFactory = sslContext.getSocketFactory();

    		socket = (SSLSocket)socketFactory.createSocket(ip_address, port);
    		socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());
    		socket.startHandshake();

    		printServerCertificate(socket);
    		printSocketInfo(socket);
    	} catch (KeyStoreException e) {
    		Log.i(TAG,"Keystore ks error");
    		//System.exit(-1);
    	} catch (NoSuchAlgorithmException e) {
    		Log.i(TAG,"No such algorithm for ks.load");
    		e.printStackTrace();
    		//System.exit(-1);
    	} catch (CertificateException e) {
    		Log.i(TAG,"certificate missing");
    		e.printStackTrace();
    		//System.exit(-1);
    	} catch (UnrecoverableKeyException e) {
    		e.printStackTrace();
    	} catch (KeyManagementException e) {
    		Log.i(TAG,"key management exception");
    		e.printStackTrace();
    		//System.exit(-1);
    	} catch  (IOException e) {
    		Log.i(TAG,"No I/O");
    		e.printStackTrace();
    		//System.exit(1);
    	}
    }
    
    private void printServerCertificate(SSLSocket socket) {
        try {
            Certificate[] serverCerts =
                socket.getSession().getPeerCertificates();
            for (int i = 0; i < serverCerts.length; i++) {
                Certificate myCert = serverCerts[i];
                Log.i(TAG,"====Certificate:" + (i+1) + "====");
                Log.i(TAG,"-Public Key-\n" + myCert.getPublicKey());
                Log.i(TAG,"-Certificate Type-\n " + myCert.getType());

                System.out.println();
            }
        } catch (SSLPeerUnverifiedException e) {
        	Log.i(TAG,"Could not verify peer");
            e.printStackTrace();
            //System.exit(-1);
        }
    }

    private void printSocketInfo(SSLSocket s) {
        Log.i(TAG,"Socket class: "+s.getClass());
        Log.i(TAG,"   Remote address = "
                +s.getInetAddress().toString());
        Log.i(TAG,"   Remote port = "+s.getPort());
        Log.i(TAG,"   Local socket address = "
                +s.getLocalSocketAddress().toString());
        Log.i(TAG,"   Local address = "
                +s.getLocalAddress().toString());
        Log.i(TAG,"   Local port = "+s.getLocalPort());
        Log.i(TAG,"   Need client authentication = "
                +s.getNeedClientAuth());
        SSLSession ss = s.getSession();
        Log.i(TAG,"   Cipher suite = "+ss.getCipherSuite());
        Log.i(TAG,"   Protocol = "+ss.getProtocol());
    }

    public class ClientSocketThread implements Runnable{
		@Override
		public void run() {
				try{
					if(socket == null){
						connectToSecureSocket();
						out = new PrintWriter(socket.getOutputStream(), true);
						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					}
					
					if(!msg.equals("")){
						out.println(msg);
						String input = in.readLine();
						if (input != null && !input.equals(".")) {
							Log.i("SERVER_RESP", input);
							Intent intent = new Intent(BROADCAST_RES);
							intent.putExtra(SOCKET_SERVER_RES, input);
							sendBroadcast(intent);
						}
						msg = "";
					}
				} catch (UnknownHostException e) {
					Log.i(TAG,"Unknown host");
				} catch  (IOException e) {
					Log.i(TAG,"No I/O");
					e.printStackTrace();
				}
		}
    }
}
