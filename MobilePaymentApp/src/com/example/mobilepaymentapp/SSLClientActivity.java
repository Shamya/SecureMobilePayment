package com.example.mobilepaymentapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.SSLSocketFactory;

import java.security.cert.Certificate;
  
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
 
public class SSLClientActivity extends Activity {
    /** Called when the activity is first created. */
 
    private EditText mText;
    private Button mSend;
    private EditText mIPaddress;
    private EditText mPort;
 
    // port to use
    private String ip_address, temp;
    private int port = 8888;
    private SSLSocket socket = null;
    private PrintWriter out = null;
    private final String TAG = "TAG";
    private char keystorepass[] = "javablog.fr".toCharArray();
    private char keypassword[] = "javablog.fr".toCharArray();
    InputStream keyin;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sslclient);
        mText = (EditText) findViewById(R.id.edtTxt_Text);
        mIPaddress = (EditText) findViewById(R.id.edtTxt_IPAddr);
        mPort = (EditText) findViewById(R.id.edtTxt_Port);
        mSend = (Button) findViewById(R.id.button_Send);
         
        mText.setText("xyz");
        mIPaddress.setText("10.0.0.96");
        mPort.setText("8888");
        
        mSend.setClickable(true);
        mSend.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                if (mIPaddress.getText().toString().equals(null) || mPort.getText().toString().equals(null)){
                    Toast.makeText(v.getContext(), "Please enter an IP address or Port number", Toast.LENGTH_LONG).show();
                }
                else{
                	temp = mText.getText().toString();
                    if (temp == null){
                        temp = "No text was entered";
                        return;
                    }

                    Log.i(TAG,"makes it to here");

                    port = Integer.parseInt(mPort.getText().toString());
                    ip_address = mIPaddress.getText().toString();
                    Thread clientThread = new Thread(new ClientSocketThread());
                    clientThread.start();
                }
            }
        });
 
    }
    
    public class ClientSocketThread implements Runnable{

		@Override
		public void run() {
			try{
				if(socket == null){
					connectToSecureSocket();
				}
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println(temp);
            } catch (UnknownHostException e) {
                Log.i(TAG,"Unknown host");
                System.exit(1);
            } catch  (IOException e) {
                Log.i(TAG,"No I/O");
                e.printStackTrace();
                System.exit(1);
            } 
		}
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
    		System.exit(-1);
    	} catch (NoSuchAlgorithmException e) {
    		Log.i(TAG,"No such algorithm for ks.load");
    		e.printStackTrace();
    		System.exit(-1);
    	} catch (CertificateException e) {
    		Log.i(TAG,"certificate missing");
    		e.printStackTrace();
    		System.exit(-1);
    	} catch (UnrecoverableKeyException e) {
    		e.printStackTrace();
    	} catch (KeyManagementException e) {
    		Log.i(TAG,"key management exception");
    		e.printStackTrace();
    		System.exit(-1);
    	} catch  (IOException e) {
    		Log.i(TAG,"No I/O");
    		e.printStackTrace();
    		System.exit(1);
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
            System.exit(-1);
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
}