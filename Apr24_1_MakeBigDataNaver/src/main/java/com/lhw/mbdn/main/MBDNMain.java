package com.lhw.mbdn.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

// 1. 주제 선정(가설 설정)
//		지하철 2호선이 정말 제일 사람 많나?
//		삼국지에서 가장 많이 언급되는 사람은?
//		요즘 가장 핫한 뉴스 주제는?
//		...
//		현재 날씨 추이로 볼때 내일 기온은?

// 2. 빅데이터를 구하기
//		발로 뛰기
//		인터넷에서 가져오기(XML or JSON or WebCrawling)

// 3. 빅데이터 정리
//		hadoop

// 4. 통계, 시각화
//		R, excel

public class MBDNMain {
	public static void main(String[] args) {
		FileWriter fw = null;
		try {
			
			fw = new FileWriter("C:\\Users\\soldesk\\Desktop\\lhw\\news.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			// 검색어
			String q = "탁구";

			// 야구 -> %2A(UTF-8)
			q = URLEncoder.encode(q, "utf-8");

			// 네이버한테 요청 주소
			URL u = new URL("https://openapi.naver.com/v1/search/news.xml?display=100&query=" + q);

			// 연결
			HttpsURLConnection huc = (HttpsURLConnection) u.openConnection();

			// 키 값
			huc.addRequestProperty("X-Naver-Client-Id", "X9HvNHRmZ5U2NsZrh3fw");
			huc.addRequestProperty("X-Naver-Client-Secret", "3T5pYJndkn");

			// 다운받기
			InputStream is = huc.getInputStream();

			// System.out.println(MyConverter.convertToString(is));

			XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();

			XmlPullParser xpp = xppf.newPullParser();
			xpp.setInput(is, "utf-8");

			// 현재 위치에 있는게 뭐 <aaa> or ㅋㅋㅋ or </aaa>
			int type = xpp.getEventType();
			String tagName = null;
			boolean aNews = false;
			//$(xml).find("단수형").each(function(i, ii){
			//		$(ii).find("속성").text();
			//});
			String data = null;
			while (type != XmlPullParser.END_DOCUMENT) {
				if (type == XmlPullParser.START_TAG) {
					tagName = xpp.getName();
					if (tagName.equals("item")) { // 단수형
						aNews = true;
					}
				} else if (type == XmlPullParser.TEXT) {
					if (aNews && tagName.equals("title")) { // 속성명
						data = xpp.getText();
						data = data.replace("<b>", "");
						data = data.replace("</b>", "");
						data = data.replace("&quot;", "");
						System.out.println(data);
						bw.write(data+"\r\n");
						bw.flush();
					}
				} else if (type == XmlPullParser.END_TAG) {
					if(xpp.getName().equals("item")  ) { // 단수형
						aNews = false;
					}
				}
				type = xpp.next(); // 다음으로
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

