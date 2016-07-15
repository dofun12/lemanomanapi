package org.lemanoman.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

public class GenericDownload implements DownloadFactory {

	public void download(HttpClient client, String url, String filename)
			throws UnsupportedOperationException, ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(url);

		client.execute(httpGet).getEntity();
		// filename = filename.replaceAll("[^A-z0-9]", "_");
		HttpEntity entity = client.execute(httpGet).getEntity();
		long size = entity.getContentLength();
		System.out.println("Downloading " + filename + " Tamanho: " + (size / 1024 / 1024));

		Progress progress = new Progress(size, filename+".part");
		progress.start();

		InputStream is = entity.getContent();
		String filePath = filename;
		FileOutputStream fos = new FileOutputStream(new File(filePath+".part"));

		int inByte;
		while ((inByte = is.read()) != -1) {
			fos.write(inByte);
		}
		System.out.println("\n");
		is.close();
		fos.close();

		System.out.println(filename + " Done");
		
		File src = new File(filePath+".part");
		File dest = new File(filePath);
		FileUtils.copyFile(src, dest,true);
		src.delete();
		progress.stopProgress();
	}

}
