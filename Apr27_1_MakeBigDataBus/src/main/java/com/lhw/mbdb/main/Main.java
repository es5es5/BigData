package com.lhw.mbdb.main;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
	public static void main(String[] args) {
		FileOutputStream fos = null;
		
		try {
			
			fos = new FileOutputStream("C:\\Users\\soldesk\\Desktop\\lhw\\BigData\\Apr27_1_MakeBigDataBus/bus.csv", true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
			BufferedWriter bw = new BufferedWriter(osw);
			
			URL u = null;
			HttpURLConnection huc = null;
			InputStream is = null;
			String json = null;
			JSONParser jp = new JSONParser();
			JSONObject jo = null;
			JSONObject cbssn = null;
			JSONArray row = null;
			int i = 0;
			JSONObject busStop = null;
			StringBuffer sb = null;
			int start = 0;
			String busURL = null;
			int day = 0;
			
			for (int month = 1; month <= 12; month++) {
				for (day = 1; day <= 31; day++) {
					for (start = 1; start < 37001; start += 1000) {
						busURL = String.format(
								"http://openapi.seoul.go.kr:8088/575a4655496b636839386f58586542/json/CardBusStatisticsServiceNew/%d/%d/2015%02d%02d",
								start, start + 999, month, day);

						u = new URL(busURL);
						huc = (HttpURLConnection) u.openConnection();

						is = huc.getInputStream();
						json = MyConverter.convertToString(is);

						// 파싱 시작해서 그 전체 데이터를 jo에 담음
						jo = (JSONObject) jp.parse(json); // {로 시작

						cbssn = (JSONObject) jo.get("CardBusStatisticsServiceNew");

						if (cbssn != null) {

							row = (JSONArray) cbssn.get("row");

							for (i = 0; i < row.size(); i++) {
								sb = new StringBuffer();
								busStop = (JSONObject) row.get(i);
								sb.append(busStop.get("USE_DT") + ",");
								sb.append(busStop.get("BUS_ROUTE_NM") + ",");
								sb.append(busStop.get("BUS_STA_NM") + ",");

								sb.append((busStop.get("RIDE_PASGR_NUM") + ",").replace(".0", ""));
								sb.append((busStop.get("ALIGHT_PASGR_NUM") + "\r\n").replace(".0", ""));

								System.out.print(sb.toString());
								bw.write(sb.toString());
								bw.flush();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
