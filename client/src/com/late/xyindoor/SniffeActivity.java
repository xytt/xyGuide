package com.late.xyindoor;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONObject;

import com.late.xyindoor.util.Finger;
import com.late.xyindoor.util.HttpRequest;
import com.late.xyindoor.util.WifiManage;
import com.late.xyindoor.util.WifiSniffer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SniffeActivity extends Activity implements Runnable{

	private ProgressBar progressSniffe;
	// private Timer timer = new Timer();
	private int process = 0;
	private WifiManage wm;
	private Finger finger;
	private String url;
	private Context context;
	private int MaxSniffe = 30; // 一个坐标上采集的指纹数
	private WifiSniffer ws;

	// private String infoSniffed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sniffe);
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);

		progressSniffe = (ProgressBar) findViewById(R.id.sniffe_progress);
		progressSniffe.setMax(MaxSniffe);
		wm = new WifiManage(this);
		url = getResources().getString(R.string.server_info_collect);
		context = this;
		
		Intent intent = getIntent();
		String buildingId = intent.getStringExtra("buildingId");
		String floor = intent.getStringExtra("floor");
		String x = intent.getStringExtra("x");
		String y = intent.getStringExtra("y");

		finger = new Finger(buildingId, floor, x, y);
//		Log.d("url", url);
		//SniffeWorker();
		
//		Thread thread = new Thread(this);
//		thread.start();
		
		ws = new WifiSniffer(this, handler);
		ws.setFinger(finger);
		ws.setCnt(MaxSniffe);
		ws.Start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ws.Stop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ws.Start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public SniffeActivity() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * 采集wifi信息，3s采集300次
	 */
	public void SniffeWorker() {
		Intent intent = getIntent();
		String buildingId = intent.getStringExtra("buildingId");
		String floor = intent.getStringExtra("floor");
		String x = intent.getStringExtra("x");
		String y = intent.getStringExtra("y");

		finger = new Finger(buildingId, floor, x, y);

		Log.d("finger", finger.getJsonString());
		// new Thread(new Sniffe()).start();
		int cnt = 0;
		while (cnt < MaxSniffe) {
			wm.startScan();
			finger.setTimestamp(wm.getScanTimestamp());
			finger.setRSSIS(wm.getWifiList());
			Log.d("finger2", finger.getJsonString());
			postSniffeInfo(url, finger.getJsonString());

			cnt++;

			Message message = new Message();
			message.what = 0x123;
			message.arg1 = cnt;
			handler.sendMessage(message);
		}
		
		Toast.makeText(context, "上传成功", Toast.LENGTH_LONG).show();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 处理UI，更新进度条
			if(msg.what == 0x123){
				int progress = msg.arg1;
				progressSniffe.setProgress(progress);
//				Toast.makeText(context,String.valueOf(progress) + progressSniffe.getMax(), Toast.LENGTH_LONG).show();
				if(progress == MaxSniffe){
					finish();
					Toast.makeText(context,"采集完毕", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	public void postSniffeInfo(String url, String data) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("content", data);
		Log.d("post", params.toString());
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] arg1,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				// 上传成功
				if (statusCode == 200) {
					Log.d("http", new String(responseBody));
					//Toast.makeText(context, "上传成功", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, "上传失败", Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				// 上传失败
				Toast.makeText(context, "上传成功", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		wm.startScan();
		finger.setTimestamp(wm.getScanTimestamp());
		finger.setRSSIS(wm.getWifiList());
		
		Log.d("finger", finger.getJsonString());
	}
}
