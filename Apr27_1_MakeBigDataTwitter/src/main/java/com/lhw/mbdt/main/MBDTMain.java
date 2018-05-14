package com.lhw.mbdt.main;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MBDTMain {

	public static void main(String[] args) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("/home/lhw/myBigData/gs.txt", true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
			BufferedWriter bw = new BufferedWriter(osw);
			
			// html문서 불러오기
			Document d = Jsoup.connect("https://twitter.com/funGS25").get();

			// css 선택자로 찾아온 DOM 객체들
			Elements es = d.select("p.TweetTextSize.TweetTextSize--normal.js-tweet-text.tweet-text");

			for (Element e : es) {
				System.out.println(e.text());
				bw.write(e.text() + "\r\n");
				bw.flush();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
