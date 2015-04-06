package com.rsorion.api.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class Browser {

	private URL url;
	private HttpURLConnection connection;

	public Browser(final String url) throws IOException {
		this.url = new URL(url);
		connection = (HttpURLConnection) this.url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "image/png");
		connection.setRequestProperty("User-Agent", "Orion");
		connection.setDoOutput(true);
		connection.setDoInput(true);
	}

	public Browser(URL url) throws IOException {
		this.url = url;
		connection = (HttpURLConnection) this.url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connection.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
		connection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
		connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
		connection.setRequestProperty("Connection", "keep-alive");
		connection.setRequestProperty("User-Agent", getUserAgent());
	}

	public HttpURLConnection connection() {
		return this.connection;
	}

	public URL url() {
		return this.url;
	}

	public void readFully() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(getInputSteam()));
		while (br.readLine() != null) {
		}
		Thread.sleep(5000);
	}

	public InputStream getInputSteam() throws IOException {
		return this.connection.getInputStream();
	}

	public OutputStream getOutputStream() throws Exception {
		return this.connection.getOutputStream();
	}

	private String getUserAgent() {
		Random random = new Random();
		String agent = user_agents[random.nextInt(user_agents.length)];
		return agent;
	}

	public String source() throws Exception {
		String source = "";
		String line;
		final BufferedReader reader = new BufferedReader(new InputStreamReader(getInputSteam()));
		while ((line = reader.readLine()) != null) {
			source += line;
		}
		return source;
	}

	private final String[] user_agents = {
			"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/18.6.872.0 Safari/535.2 UNTRUSTED/1.0 3gpp-gba UNTRUSTED/1.0",
			"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.12 Safari/535.11",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.8 (KHTML, like Gecko) Chrome/17.0.940.0 Safari/535.8",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7xs5D9rRDFpg2g",
			"Mozilla/5.0 (Windows NT 5.2; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:9.0) Gecko/20100101 Firefox/9.0",
			"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0a2) Gecko/20110613 Firefox/6.0a2",
			"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
			"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)",
			"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/5.0)",
			"Mozilla/4.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/5.0)",
			"Mozilla/1.22 (compatible; MSIE 10.0; Windows 3.1)",
			"Mozilla/5.0 (Windows; U; MSIE 9.0; WIndows NT 9.0; en-US))",
			"Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)",
			"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 7.1; Trident/5.0)",
			"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; Media Center PC 6.0; InfoPath.3; MS-RTC LM 8; Zune 4.7)",
			"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; Media Center PC 6.0; InfoPath.3; MS-RTC LM 8; Zune 4.7",
			"Opera/9.80 (Windows NT 6.1; U; es-ES) Presto/2.9.181 Version/12.00",
			"Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52"
	};

}
