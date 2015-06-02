package com.late.xyindoor;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.late.xyindoor.indoormap.IndoorMapView;
import com.late.xyindoor.indoormap.MarkerView;
import com.late.xyindoor.util.HttpRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 信息采集Fragment的界面
 * 
 * 
 * @author abel
 */
public class BuildFragment extends Fragment implements OnTouchListener {

	private View buildLayout;
	// private ImageView indoorImage;
	private IndoorMapView indoorImage;
	private MarkerView markerView;
	private Button btnStartSniffe;
	private RelativeLayout layout;

	private TextView text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (buildLayout == null) {
			buildLayout = inflater.inflate(R.layout.fragment_build, null);
			layout = (RelativeLayout) buildLayout
					.findViewById(R.id.layout_build_map);
			indoorImage = (IndoorMapView) buildLayout
					.findViewById(R.id.indoor_build_map);
			markerView = (MarkerView) buildLayout.findViewById(R.id.marker);
			btnStartSniffe = (Button) buildLayout
					.findViewById(R.id.start_sniffe);
			btnStartSniffe.setOnClickListener(btnStartSniffeClickListener);

			indoorImage.setImageResource(R.drawable.building_east_flat);
			indoorImage.addMarker(markerView);

			text = (TextView) buildLayout.findViewById(R.id.location);
			indoorImage.setTextView(text);

//			MarkerView m = new MarkerView(getActivity());
//			m.setXY(123f, 234f);
//			layout.addView(m);
//			m.invalidate();

		} else {
			if (buildLayout.getParent() != null) {
				((ViewGroup) buildLayout.getParent()).removeView(buildLayout);
			}
		}
		return buildLayout;
	}

	public void onResume() {
		super.onResume();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private OnClickListener btnStartSniffeClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			// 进入采集信息页面

			Intent intent = new Intent(getActivity(), SniffeActivity.class);
			intent.putExtra("buildingId", "building_east_0");
			intent.putExtra("floor", "F1");
			intent.putExtra("x", markerView.getInMapX());
			intent.putExtra("y", markerView.getInMapY());

			startActivity(intent);
		}
	};

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// TODO Auto-generated method stub
		// return false;
		// final TextView text = (TextView) buildLayout
		// .findViewById(R.id.location);

		text.setText(markerView.getInMapX() + ",  " + markerView.getInMapY());

		return true;
	}

	public void getAllPosCollected() {

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("buildingId", "building_east_1");
			jsonObj.put("floor", "F1");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RequestParams param = new RequestParams();
		param.add("conent", jsonObj.toString());
		String url = this.getResources().getString(R.string.server_getallpos);
		AsyncHttpClient client = new AsyncHttpClient();

		client.post(url, param, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] arg1,
					byte[] strResponse) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "test2", Toast.LENGTH_LONG).show();
				if (statusCode == 200) {
					try {
						Log.d("pos", strResponse.toString());
						JSONObject jsonObj = new JSONObject(strResponse
								.toString());
						if (jsonObj.getInt("code") == 0) {
							
							JSONArray jsonArr = jsonObj.getJSONArray("detail");
							for (int i = 0; i < jsonArr.length(); i++) {
								JSONObject pos = jsonArr.getJSONObject(i);
								float x = Float.valueOf(pos.getString("x"));
								float y = Float.valueOf(pos.getString("y"));
								addMarkerToMap(x, y);
							}
						} else {
								
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.d("test","json");
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void addMarkerToMap(float x, float y) {
		MarkerView marker = new MarkerView(getActivity());
		marker.setXY(x*indoorImage.getWidth(), y*indoorImage.getHeight());
		layout.addView(marker);
		marker.invalidate();
	}
}
