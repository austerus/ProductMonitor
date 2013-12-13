package com.amazon.productmonitor;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MonitorService extends IntentService {
	private static final String baseURL = "http://aws.dragonflame.org/index.php";
	public static final String PRODUCT_ID = "";
	public static final String PRODUCT_NAME = "";
	public static final String AVAILABLE = "";
	public static final String PRICE = "";
	public static final String INFO = "";
	public static final String NOTIFICATION = "com.amazon.productmonitor";

	public MonitorService() {
		super("MonitorService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		String product_id = intent.getStringExtra(PRODUCT_ID);
		String strURL = MonitorService.baseURL + "?method=check&pid=" + product_id;
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(strURL);
		String text = null;
		String product_name = "";
		String available = "";
		String info = "";
		String price = "";
		
		try {
			Log.i("XXXX",strURL);
			HttpResponse response = httpClient.execute(httpGet, localContext);
			HttpEntity entity = response.getEntity();
			text = getASCIIContentFromEntity(entity);
			Log.i("XXXX", text);
			
			String[] split = text.split("::");
			
			if(split.length == 5) {
				product_name = split[1];
				available = split[2];
				price = split[3];
				info = split[4];
			}
			Log.i("XXXX","success!");
			Log.i("AV",product_name);
		} catch (Exception e) {
			info = e.getLocalizedMessage();
			Log.i("XXXX", info);
		}

		publishResults(product_id,product_name,available,price,info);
	}

	protected String getASCIIContentFromEntity(HttpEntity entity)
			throws IllegalStateException, IOException {
		InputStream in = entity.getContent();
		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n > 0) {
			byte[] b = new byte[4096];
			n = in.read(b);
			if (n > 0)
				out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	/**
	 * @return the baseurl
	 */
	public static String getBaseurl() {
		return baseURL;
	}

	private void publishResults(String product_id, String product_name,
			String available, String price, String info) {
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(PRODUCT_ID, product_id);
		intent.putExtra(PRODUCT_NAME, product_name);
		intent.putExtra(AVAILABLE, available);
		intent.putExtra(PRICE, price);
		intent.putExtra(INFO, info);
		sendBroadcast(intent);
	}

}
