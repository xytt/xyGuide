package com.late.xyindoor;

import org.json.JSONException;
import org.json.JSONObject;

import com.late.xyindoor.indoormap.MarkerView;
import com.late.xyindoor.util.Finger;
import com.late.xyindoor.util.WifiSniffer;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class IndoorActivity extends Activity implements OnClickListener {

	private ImageView indoorMap;
	private Button btnLocating;
	private boolean isLocating = false;
	private WifiSniffer ws;
	private Finger finger;
	private int MaxSniffe = 5;
	private MarkerView marker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_indoor);
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		
		indoorMap = (ImageView)findViewById(R.id.indoor_map);
		indoorMap.setImageResource(R.drawable.building_east_flat);
		indoorMap.setOnClickListener(this);
		
		marker = (MarkerView) findViewById(R.id.marker);
		btnLocating = (Button) findViewById(R.id.locating);
		btnLocating.setOnClickListener(btnLocatingOnClickListener);
		
		finger = new Finger("building_east_0", "F1", "", "");
		
		ws = new WifiSniffer(this, handler);
		ws.setOp(0);
		ws.setFinger(finger);
		ws.setCnt(MaxSniffe);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			return true;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private OnClickListener btnLocatingOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!isLocating){
				isLocating = true;
				btnLocating.setText("定位中");
				
				ws.Start();
			}else {
				isLocating = false;
				btnLocating.setText("实时定位");
				
				ws.Stop();
			}
		}
	};
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 处理UI，更新进度条
			if(msg.what == 0x124){
				// 动态定位，绘制点
				msgHandlerLocation((String)msg.obj);
			}
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		ws.Stop();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		ws.Stop();
	}

	/**
	 * 处理定位返回消息
	 * @param position 返回的定位信息
	 */
	private void msgHandlerLocation(String position){
		try {
			JSONObject jsonObj = new JSONObject(position);
//			Toast.makeText(this, jsonObj.toString(), Toast.LENGTH_SHORT).show();
			if(jsonObj.getInt("code") == 0){
			//	Toast.makeText(this, jsonObj.getJSONObject("detail").toString(), Toast.LENGTH_SHORT).show();
				String x = jsonObj.getJSONObject("detail").getString("x");
				String y = jsonObj.getJSONObject("detail").getString("y");
				
				reDrawMarker(Float.valueOf(x), Float.valueOf(y));
			} else {
				Toast.makeText(this, "服务器无法定位", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "返回数据格式有错误", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	/**
	 * 进行坐标计算，重绘marker，实现动态定位功能
	 * @param x 在地图中的x坐标
	 * @param y 在地图中的y坐标
	 */
	private void reDrawMarker(float x, float y){
		marker.setXY(indoorMap.getWidth()*x, indoorMap.getHeight()*y);
		marker.invalidate();
	}
}
