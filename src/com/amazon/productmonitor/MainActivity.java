package com.amazon.productmonitor;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.status);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				String product_name = bundle.getString(MonitorService.PRODUCT_NAME);
				String resultCode = bundle.getString(MonitorService.AVAILABLE);
				Log.i("RESPONSE",resultCode);
				if (resultCode.equalsIgnoreCase("yes")) {
					//Toast.makeText(MainActivity.this,"Check done. Download URI: " + product_name,Toast.LENGTH_LONG).show();
					textView.setText(product_name + "is available");
				} else {
					//Toast.makeText(MainActivity.this, "Check failed", Toast.LENGTH_LONG).show();
					textView.setText(product_name + " is NOT available");
				}
			}
		}
	};
	
	  @Override
	  protected void onResume() {
	    super.onResume();
	    registerReceiver(receiver, new IntentFilter(MonitorService.NOTIFICATION));
	  }
	  @Override
	  protected void onPause() {
	    super.onPause();
	    unregisterReceiver(receiver);
	  }

	  public void onClick(View view) {

	    Intent intent = new Intent(this, MonitorService.class);
	    // add infos for the service which file to download and where to store
	    intent.putExtra(MonitorService.PRODUCT_ID, "B00BT9DU26");
	    startService(intent);
	    textView.setText("Service started");
	  }
}
