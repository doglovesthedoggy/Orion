package com.rsorion.plugin.screenshot;


import com.rsorion.api.util.Browser;
import com.rsorion.client.notification.Notification;
import com.rsorion.client.notification.NotificationsManager;
import com.rsorion.client.rsi.ClientStore;
import com.rsorion.util.Configuration;
import com.rsorion.util.Debug;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.*;

public final class ScreenShot implements Runnable {

	//Used for storing the files locally
	private static final File ssDir = new File(Configuration.STORAGE + File.separator + "Screenshots");
	static {
		if (!ssDir.exists() && ssDir.mkdir())
			Debug.println("ScreenShot", "Created storage directory.");
	}

	//Stores the image in memory
	private BufferedImage capture;

	public void run() {
		try {
			Thread.sleep(100);
			final Rectangle captureLocation = new Rectangle(ClientStore.applet().getLocationOnScreen().x, ClientStore.applet().getLocationOnScreen().y,
					ClientStore.applet().getWidth(), ClientStore.applet().getHeight());
			final BufferedImage original = new Robot().createScreenCapture(captureLocation);
			//TODO: Edit image (by putting a watermark for Orion)
			capture = original;


			final String saveLoc = ssDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".png";
			final File out = new File(saveLoc);
			ImageIO.write(capture, "png", out);

			final HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			final HttpPost httppost = new HttpPost("http://tldr.me/index.php?act=uploader&display=link");
			httppost.setHeader("User-Agent", "Orion");

			final File file = out;

			final MultipartEntity mpEntity = new MultipartEntity();
			final ContentBody cbFile = new FileBody(file, "image/png");
			mpEntity.addPart("Filedata", cbFile);

			httppost.setEntity(mpEntity);
			HttpResponse response;
			try {
				response = httpclient.execute(httppost);
				final HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					StringSelection selection = new StringSelection(EntityUtils.toString(resEntity));
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
					NotificationsManager.add(new Notification("Orion", "Your image has been uploaded successfully!", TrayIcon.MessageType.INFO));
				}
				if (resEntity != null) {
					resEntity.consumeContent();
				}

				httpclient.getConnectionManager().shutdown();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
