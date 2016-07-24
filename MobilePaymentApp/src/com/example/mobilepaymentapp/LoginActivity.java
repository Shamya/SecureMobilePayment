package com.example.mobilepaymentapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {

	public static final String SOCKET_SERVICE = "Socket Service";
	public static final String LOGIN_SUCCESS = "login success";
	public static final String LOGIN_FAILURE = "login failure";
	public static final String IPADDRESS = "ip address";
	public static final String PORTNUMBER = "port";
	public static final String MESSAGE = "message";
	public static final String salt = "TRUSTWORTHYMOBILEPAYMENT"; 
	public static final String LOGIN_IP = "10.0.0.96";
	public static final int LOGIN_PORT = 8888;
	
	EditText UserId;
	EditText Password;
	TextView txtReg;
	Button btn_Login;
    private static MessageDigest md;
    private String userid, pw;
    SocketResReciever socketresrec;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        UserId = (EditText)findViewById(R.id.edtTxt_UsrName);
        Password = (EditText)findViewById(R.id.edtTxt_Password);
        txtReg = (TextView)findViewById(R.id.textRegister);
        txtReg.setPaintFlags(txtReg.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
        IntentFilter filter = new IntentFilter(SocketService.BROADCAST_RES);
        socketresrec = new SocketResReciever();
        registerReceiver(socketresrec, filter);
        
		
        txtReg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(), RegisterUserActivity.class);
				startActivity(myIntent);
			}
		});
        
        btn_Login = (Button)findViewById(R.id.btn_Login);
        btn_Login.setOnClickListener(new View.OnClickListener() {
        	
        	/*Description: On Login Button Click
        	 * 1. Check if userid and password not empty
        	 * 2. If valid, create a message with userid and double hashed password
        	 * 3. Send the information to Socket in order to send to server for verification.
        	 */
			@Override
			public void onClick(View v) {

				userid = UserId.getText().toString();
				pw = Password.getText().toString();
				
				if(!userid.equals("") && !pw.equals("")){
					String msg = userid + " - " + hashPW(hashPW(pw));
					Intent myIntent = new Intent(getApplicationContext(), SocketService.class);
			        
					Bundle myBundle = new Bundle();
	        		myBundle.putString(LoginActivity.IPADDRESS, LOGIN_IP);
	        		myBundle.putInt(LoginActivity.PORTNUMBER, LOGIN_PORT);
	        		myBundle.putString(LoginActivity.MESSAGE, msg);
	        		
					myIntent.putExtras(myBundle);
			        startService(myIntent);
				}
			}
		});
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	UserId.setText("");
    	Password.setText("");
    	UserId.requestFocus();
    }
     
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }    
    
    
    /*Description: hash function
	 * 1. Create a message digest of SHA-512
	 * 2. Generate salt and append with the password.
	 * 3. update digest and create hash result.
	 */
    public static String hashPW(String pw){
    	try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    	//Generate salt
        //String salt = generateSalt();      
    	String pwplussalt = pw + salt;
    	
    	//Add password and salt bytes to digest
        md.update(pwplussalt.getBytes());
        //Get the hash's bytes
        byte[] bytes = md.digest();
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

    	return sb.toString();
    }
    
    /*Salt generation function*/
    @SuppressLint("TrulyRandom") 
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String s = new String(bytes);
        return s;
    }
    
    /*Description: Socket Result receiver
	 * 1. Receives the broadcasted result (LOGIN_SUCCESS or LOGIN_FAILURE) from Socket service
	 * 2. Check for LOGIN_SUCCESS or LOGIN_FAILURE
	 * 3. If LOGIN_SUCCESS, go to Shopping Activity, else stay in Login Screen.
	 */
    public class SocketResReciever extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String result = intent.getStringExtra(SocketService.SOCKET_SERVER_RES);
			if(result.equals(LOGIN_SUCCESS)){
				Log.i("LOGIN_ACTIVITY", result);
				Intent myIntent = new Intent(getApplicationContext(), ShoppingActivity.class);
				myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(myIntent);
			}
			else if(result.equals(LOGIN_FAILURE)){
				Log.i("LOGIN_ACTIVITY", result);
				Toast.makeText(getApplicationContext(), "Login Failed. Try again", Toast.LENGTH_SHORT).show(); 
			}
			SocketService.msg = "";
		}
    }
    
    /*Quit the app on back press in login screen*/
    @Override
	public void onBackPressed(){
		//do nothing
    	moveTaskToBack(true);
	}
}
