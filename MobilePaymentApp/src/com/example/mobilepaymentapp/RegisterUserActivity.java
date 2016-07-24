package com.example.mobilepaymentapp;


import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterUserActivity extends ActionBarActivity {

	EditText txt_Name, txt_UsrName, txt_PW, txt_CPW;
	Button btn_Register;
	SocketReciever socketrec;
	public static final String REGISTRATION_SUCCESS = "registration success";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		txt_Name = (EditText)findViewById(R.id.editText_Name);
		txt_UsrName = (EditText)findViewById(R.id.editText_UsrName);
		txt_PW = (EditText)findViewById(R.id.editText_PW);
		txt_CPW = (EditText)findViewById(R.id.editText_CPW);
		
		IntentFilter filter = new IntentFilter(SocketService.BROADCAST_RES);
		socketrec = new SocketReciever();
        registerReceiver(socketrec, filter);
        
		btn_Register = (Button)findViewById(R.id.button_Register);
		btn_Register.setOnClickListener(new View.OnClickListener() {
			
			/*Description: On Register Button Click
        	 * 1. Check if all details are valid.
        	 * 2. If valid, create a message with username, userid and double hashed password.
        	 * 3. Send the information to Socket in order to store to server as valid user.
        	 */
			@Override
			public void onClick(View v) {
				if(txt_Name.getText().toString().equals("") || txt_UsrName.getText().toString().equals("") ||
						txt_PW.getText().toString().equals("") || txt_CPW.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "Few Details Missing", Toast.LENGTH_SHORT).show();
					return;
				}
				else if(!txt_PW.getText().toString().equals(txt_CPW.getText().toString())){
					Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
					return;
				}
				String msg = txt_Name.getText().toString() + " - " + txt_UsrName.getText().toString() + " - " + LoginActivity.hashPW(LoginActivity.hashPW(txt_PW.getText().toString()));
				Intent myIntent = new Intent(getApplicationContext(), SocketService.class);

				Bundle myBundle = new Bundle();
        		myBundle.putString(LoginActivity.IPADDRESS, LoginActivity.LOGIN_IP);
        		myBundle.putInt(LoginActivity.PORTNUMBER, LoginActivity.LOGIN_PORT);
        		myBundle.putString(LoginActivity.MESSAGE, msg);
        		myIntent.putExtras(myBundle);
		        startService(myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_user, menu);
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
	
	/*Description: Socket Result receiver
	 * 1. Receives the broadcasted result (REGISTRATION_SUCCESS or REGISTRATION_FAILURE) from Socket service
	 * 2. Check for REGISTRATION_SUCCESS or REGISTRATION_FAILURE
	 * 3. If REGISTRATION_SUCCESS, go to Login Activity, else stay in Registration Screen.
	 */
    public class SocketReciever extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String result = intent.getStringExtra(SocketService.SOCKET_SERVER_RES);
			if(result.equals(REGISTRATION_SUCCESS)){
				Log.i("REGISTRATION_ACTIVITY", result);
				Toast.makeText(getApplicationContext(), "Registration Success. Try Login", Toast.LENGTH_LONG).show();
				Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(myIntent);
			}
			SocketService.msg = "";
		}
    }
}   	
