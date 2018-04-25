package com.lhw.mbds.main;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class MBDSMain {
	public static void main(String[] args) {

		// 파일에 utf-8 인코딩으로 적혀야
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream("C:\\Users\\soldesk\\Desktop\\lhw\\BigData/subway.csv", true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");

			BufferedWriter bw = new BufferedWriter(osw);

			String day = null;
			
			for (int i = 1; i <= 31; i++) {
				day = String.format("%02d", i);
				// 주소
				URL u = new URL(
						"http://openapi.seoul.go.kr:8088/575a4655496b636839386f58586542/xml/CardSubwayStatsNew/1/600/201803" +day);

				// 연결
				HttpURLConnection huc = (HttpURLConnection) u.openConnection();

				// 헤더(x)

				// 다운받아
				InputStream is = huc.getInputStream();

				// 글자로 바꿔서 콘솔에 출력
				// System.out.println(MyConverter.convertToString(is));

				XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
				XmlPullParser xpp = xppf.newPullParser();
				xpp.setInput(is, "utf-8");

				int type = xpp.getEventType();
				String tagName = null;
				boolean aStation = false;

				while (type != XmlPullParser.END_DOCUMENT) {
					if (type == XmlPullParser.START_TAG) {
						tagName = xpp.getName();
						if (tagName.equals("row")) {
							aStation = true;
						}
					} else if (type == XmlPullParser.TEXT) {
						if (aStation) {
							if (tagName.equals("USE_DT") || tagName.equals("LINE_NUM") || tagName.equals("SUB_STA_NM")
									|| tagName.equals("RIDE_PASER_NUM")) {
								System.out.print(xpp.getText() + ",");
								bw.write(xpp.getText() + ",");
								bw.flush();
							} else if (tagName.equals("ALIGHT_PASGR_NUM")) {
								System.out.print(xpp.getText() + "\n");
								bw.write(xpp.getText() + "\r\n");
								bw.flush();
							}
						}
					} else if (type == XmlPullParser.END_TAG) {
						if (aStation) {
							if (tagName.equals("USE_DT") || tagName.equals("LINE_NUM") || tagName.equals("SUB_STA_NM")
									|| tagName.equals("RIDE_PASER_NUM") || tagName.equals("ALIGHT_PASGR_NUM")) {
								tagName = "";
							}
						}
						if (xpp.getName().equals("row")) {
							aStation = false;
						}
					}
					type = xpp.next();
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
