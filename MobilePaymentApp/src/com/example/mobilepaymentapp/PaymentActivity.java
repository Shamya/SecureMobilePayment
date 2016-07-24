package com.example.mobilepaymentapp;

import java.io.InputStream;

import android.support.v7.app.ActionBarActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PaymentActivity extends ActionBarActivity {

	EditText edtTxt_Name,cardno, expmnth, expyr,cvv;
	Button makepayment;
	SocketPayReciever socketrec;
	

	private String name, cardnum, expdate, cvvnum;
	private String ip_address ="10.0.0.95";
    private int port = 8080;
    InputStream keyin;
    private String encryptedText = "";
	public static final String PAYMENT_SUCCESS = "payment success";

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		edtTxt_Name = (EditText)findViewById(R.id.editText_Name);
		edtTxt_Name.requestFocus();
        cardno = (EditText)findViewById(R.id.editText_CardNumber);
        expmnth = (EditText)findViewById(R.id.editText_Month);
        expyr = (EditText)findViewById(R.id.editText_Year);
        cvv = (EditText)findViewById(R.id.editText_CVV);
        makepayment = (Button)findViewById(R.id.button_Pay);
		
		IntentFilter filter = new IntentFilter(SocketService.BROADCAST_RES);
		socketrec = new SocketPayReciever();
        registerReceiver(socketrec, filter);
        
        makepayment.setOnClickListener(new View.OnClickListener() {
		
        /*Description: On payment click
         * 1. Check for the valid details of the card holder.
         * 2. If valid details, encrypt all the details using AES Encryption algorithm.
         * 3. Send the encrypted data to Socket service in order to send the data to server.
         */
		@Override
		public void onClick(View v) {
			name = edtTxt_Name.getText().toString();
			cardnum = cardno.getText().toString();
			expdate = expmnth.getText().toString() + expyr.getText().toString();
			cvvnum = cvv.getText().toString();
					        
			if(name.equals("") || cardnum.equals("") || expdate.equals("") || cvvnum.equals("")){
				Toast.makeText(getApplicationContext(), "Few Details Missing", Toast.LENGTH_SHORT).show();
				return;
			}	    		
			
			AESEncryption d = new AESEncryption();
	        String inputtoencrypt = name + "-" + cardnum + "-" + expdate + "-" + cvvnum;
			try {
				encryptedText = d.encrypt(inputtoencrypt);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Intent myIntent = new Intent(getApplicationContext(), SocketService.class);

			Bundle myBundle = new Bundle();
    		myBundle.putString(LoginActivity.IPADDRESS, ip_address);
    		myBundle.putInt(LoginActivity.PORTNUMBER, port);
    		myBundle.putString(LoginActivity.MESSAGE, encryptedText);
    		myIntent.putExtras(myBundle);
	        startService(myIntent);
		}
	});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.payment, menu);
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

	
	/*Description: Socket Payment receiver
	 * 1. Receives the broadcasted result (PAYMENT_SUCCESS or PAYMENT_FAILURE) from Socket service
	 * 2. Check for PAYMENT_SUCCESS or PAYMENT_FAILURE
	 * 3. If PAYMENT_SUCCESS, go to Shopping Activity for more shopping, else stay in Payment Screen.
	 */
	public class SocketPayReciever extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String result = intent.getStringExtra(SocketService.SOCKET_SERVER_RES);
			if(result.equals(PAYMENT_SUCCESS)){
				Log.i("LOGIN_ACTIVITY", result);
				Toast.makeText(getApplicationContext(), "Payment Success. Shop more", Toast.LENGTH_LONG).show();
				Intent myIntent = new Intent(getApplicationContext(), ShoppingActivity.class);
				startActivity(myIntent);
			}
			SocketService.msg = "";
		}
	}

}



