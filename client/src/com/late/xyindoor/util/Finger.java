package com.late.xyindoor.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Finger {

	private long timestamp;
	private String buildingId;
	private String floor;
	private String x;
	private String y;
	private Map<String, Integer> rssis = null;
	private String direction;

	public Finger(String buildingId, String floor, String x, String y) {
		// TODO Auto-generated constructor stub
		this.buildingId = buildingId;
		this.floor = floor;
		this.x = x;
		this.y = y;

		rssis = new HashMap<String, Integer>();
	}

	public void setTimestamp(long timestamp){
		this.timestamp = timestamp;
	}
	
	public void addRSSI(String mac, int rssi) {
		this.rssis.put(mac, rssi);
	}

	public void setRSSIS(Map<String, Integer> map){
		this.rssis = map;
	}
	
	public String getJsonString() {

		JSONObject jsonObj = new JSONObject();
		JSONArray jsonRssiArray = new JSONArray();

		try {
			jsonObj.put("position", new JSONArray().put(x).put(y));
			jsonObj.put("buildingId", buildingId);
			jsonObj.put("floor", floor);
			jsonObj.put("timestamp", timestamp);
			jsonObj.put("direction", direction);
			for (String key : rssis.keySet()) {
				JSONObject jsonRssiObj = new JSONObject();
				jsonRssiObj.put("mac", key).put("rssi", rssis.get(key));
	
				jsonRssiArray.put(jsonRssiObj);
			}
			jsonObj.put("rssis", jsonRssiArray);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return "";
		}

		return jsonObj.toString();
	}
	
	public String getLocationJsonString() {
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonRssiArray = new JSONArray();

		try {
			jsonObj.put("buildingId", buildingId);
			jsonObj.put("floor", floor);
			jsonObj.put("timestamp", timestamp);
			jsonObj.put("direction", direction);
			for (String key : rssis.keySet()) {
				JSONObject jsonRssiObj = new JSONObject();
				jsonRssiObj.put("mac", key).put("rssi", rssis.get(key));
	
				jsonRssiArray.put(jsonRssiObj);
			}
			jsonObj.put("rssis", jsonRssiArray);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return "";
		}

		return jsonObj.toString();
	}
}
