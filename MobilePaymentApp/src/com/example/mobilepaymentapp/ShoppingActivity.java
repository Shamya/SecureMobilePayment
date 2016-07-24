package com.example.mobilepaymentapp;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ShoppingActivity extends ActionBarActivity {

	Button btn_buy, btn_logout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);
		btn_logout = (Button)findViewById(R.id.button_Logout);
		btn_logout.setOnClickListener(new View.OnClickListener() {
			
			/*Description: On Logout Button Click for shopping 
        	 * 1. Go to Login screen.
        	 */
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(myIntent);
			}
		});
		btn_buy = (Button)findViewById(R.id.btn_buy);
		btn_buy.setOnClickListener(new View.OnClickListener() {
			
			/*Description: On Buy Button Click for shopping 
        	 * 1. Go to payment screen.
        	 */
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(getApplicationContext(), PaymentActivity.class);
				startActivity(myIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shopping, menu);
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
	
	@Override
	public void onBackPressed(){
		//do nothing
	}
}
