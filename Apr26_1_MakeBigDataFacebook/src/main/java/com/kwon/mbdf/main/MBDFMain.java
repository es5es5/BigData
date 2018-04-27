package com.kwon.mbdf.main;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MBDFMain {
	public static void main(String[] args) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("D:\\Kwon\\�ڿ�\\for BigData/facebook.txt", true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
			BufferedWriter bw = new BufferedWriter(osw);

			URL u = new URL(
					"https://graph.facebook.com/me?fields=posts&access_token=EAACEdEose0cBAFwzyM12jzfBy74cg1u4aDr18vQdILCr3p5EdNhlCEAK4ZBBXUswMnDp5Jk5efUdARmya2EnX7T9EM3tjK8lZA7687NoGxJ6MICXBcAVuZBEXVGZBvNyAVx9vsNzBZBLpibaXESVJyqAD4SMczZC6vJodQ2yRFpthCR9ftbajsF4YTl69ZATVEZD");
			HttpsURLConnection huc = (HttpsURLConnection) u.openConnection();
			InputStream is = huc.getInputStream();

			// ���ڷ�
			String str = MyConverter.convertToString(is);

			// �ļ�
			JSONParser jp = new JSONParser();

			// �Ľ� �����ؼ� �� ��ü �����͸� jo�� ����
			JSONObject jo = (JSONObject) jp.parse(str); // {�� ����
			// JSONArray ja = (JSONArray) jp.parse(str); // [�� ����

			// var data = jo.posts.data
			JSONObject posts = (JSONObject) jo.get("posts");
			JSONArray data = (JSONArray) posts.get("data");
			// var t = null;
			// $.each(data, function(i, p){
			// t = p.story;
			// if(t == null){
			// t = p.message
			// }
			// alert(t);
			// });
			JSONObject p = null;
			String t = null;
			for (int i = 0; i < data.size(); i++) {
				p = (JSONObject) data.get(i);

				t = (String) p.get("story");
				if (t == null) {
					t = (String) p.get("message");
				}
				System.out.println(t);
				bw.write(t + "\r\n");
				bw.flush();
			}

			JSONObject paging = (JSONObject) posts.get("paging");
			String nextPageUrl = (String) paging.get("next");

			while (true) {
				u = new URL(nextPageUrl);
				huc = (HttpsURLConnection) u.openConnection();
				is = huc.getInputStream();
				str = MyConverter.convertToString(is);

				jo = (JSONObject) jp.parse(str);
				data = (JSONArray) jo.get("data");

				if (data.size() == 0) {
					break;
				}

				// for

				paging = (JSONObject) jo.get("paging");
				nextPageUrl = (String) paging.get("next");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
