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

// 1. ���� ����(���� ����)
//		����ö 2ȣ���� ���� ���� ��� ����?
//		�ﱹ������ ���� ���� ��޵Ǵ� �����?
//		���� ���� ���� ���� ������?
//		...
//		���� ���� ���̷� ���� ���� �����?

// 2. �����͸� ���ϱ�
//		�߷� �ٱ�
//		���ͳݿ��� ��������(XML or JSON or WebCrawling)

// 3. ������ ����
//		hadoop

// 4. ���, �ð�ȭ
//		R, excel

public class MBDNMain {
	public static void main(String[] args) {
		FileWriter fw = null;
		try {
			
			fw = new FileWriter("C:\\Users\\soldesk\\Desktop\\lhw\\news.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			// �˻���
			String q = "Ź��";

			// �߱� -> %2A(UTF-8)
			q = URLEncoder.encode(q, "utf-8");

			// ���̹����� ��û �ּ�
			URL u = new URL("https://openapi.naver.com/v1/search/news.xml?display=100&query=" + q);

			// ����
			HttpsURLConnection huc = (HttpsURLConnection) u.openConnection();

			// Ű ��
			huc.addRequestProperty("X-Naver-Client-Id", "X9HvNHRmZ5U2NsZrh3fw");
			huc.addRequestProperty("X-Naver-Client-Secret", "3T5pYJndkn");

			// �ٿ�ޱ�
			InputStream is = huc.getInputStream();

			// System.out.println(MyConverter.convertToString(is));

			XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();

			XmlPullParser xpp = xppf.newPullParser();
			xpp.setInput(is, "utf-8");

			// ���� ��ġ�� �ִ°� �� <aaa> or ������ or </aaa>
			int type = xpp.getEventType();
			String tagName = null;
			boolean aNews = false;
			//$(xml).find("�ܼ���").each(function(i, ii){
			//		$(ii).find("�Ӽ�").text();
			//});
			String data = null;
			while (type != XmlPullParser.END_DOCUMENT) {
				if (type == XmlPullParser.START_TAG) {
					tagName = xpp.getName();
					if (tagName.equals("item")) { // �ܼ���
						aNews = true;
					}
				} else if (type == XmlPullParser.TEXT) {
					if (aNews && tagName.equals("title")) { // �Ӽ���
						data = xpp.getText();
						data = data.replace("<b>", "");
						data = data.replace("</b>", "");
						data = data.replace("&quot;", "");
						System.out.println(data);
						bw.write(data+"\r\n");
						bw.flush();
					}
				} else if (type == XmlPullParser.END_TAG) {
					if(xpp.getName().equals("item")  ) { // �ܼ���
						aNews = false;
					}
				}
				type = xpp.next(); // ��������
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

