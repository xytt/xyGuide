package com.late.xyindoor.util;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpRequest {

	private DefaultHttpClient httpClient;
	private List<NameValuePair> postList;
	private String strResponse;
	private Handler mHandler;

	public HttpRequest() {
		// TODO Auto-generated constructor stub
		// httpClient = new DefaultHttpClient();
	}

	public void postRequest(String url, String data, Handler handler) {
		this.mHandler = handler;
		AsyncHttpClient client = new AsyncHttpClient();
		client.setTimeout(8000);
//		client.addHeader("Content-Type", "application/x-www-form-urlencoded");
		RequestParams params = new RequestParams();
		params.put("content", data);

		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] arg1,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				if (statusCode == 200) {
					// 发送成功
					
					strResponse = new String(responseBody);
					Log.d("response", strResponse);
					Message msg = mHandler.obtainMessage();
					msg.what = 0x124;
//					JSONObject json = new JSONObject();
//					try {
//						json.put("code", 0);
//						json.put("detail", new JSONObject().put("x", 0.2345).put("y", 0.5234));
//						strResponse = json.toString();
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
					msg.obj = strResponse;
					mHandler.sendMessage(msg);

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				// 发送失败
				
			}
		});
	}

	public String getResponseContent() {
		return this.strResponse;
	}
}
