package com.late.xyindoor.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiManage {

	private WifiManager wm;
	private long scanTimestamp;
	private List<ScanResult> scanResultList;
//	private boolean running = false;

	public WifiManage(Context context) {
		// TODO Auto-generated constructor stub

		wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

	}

	public boolean getWifiStatus() {
		return wm.isWifiEnabled();
	}
	
	public long getScanTimestamp(){
		return this.scanTimestamp;
	}
	
	public void startScan(){
		wm.startScan();
		scanTimestamp = System.currentTimeMillis();
		scanResultList = wm.getScanResults();
		
//		String str = null;
//		for(int i=0; i<scanResultList.size(); i++){
//			str += scanResultList.get(i).BSSID + scanResultList.get(i).level + ", ";
//		}
//		Log.d("rssi", str);
	}
	
	/**
	 * 获取信号强度最强的前八个wifi信号
	 * @return
	 */
	public Map<String, Integer> getWifiList() {
		sortWifiList();
		Map<String, Integer> map = new HashMap<String, Integer>();
		int cnt = 0;
		for(int i=0; i<scanResultList.size(); i++){
			map.put(scanResultList.get(i).BSSID, scanResultList.get(i).level);
			if(++cnt >= 8){
				break;
			}
		}
		return map;
	}

	/**
	 * 按信号强度从大到小排序
	 */
	private void sortWifiList(){
		Collections.sort(scanResultList, new Comparator<ScanResult>() {
			public int compare(ScanResult a, ScanResult b) {
				if(a.level <= b.level){
					return 1;
				}
				return 0;
			}
		});
	}
}
