package org.lemanoman.download;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.HeaderGroup;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class Download {

	public static void main(String[] args) throws UnsupportedOperationException, IOException {
		/**
		String[] urls = new String[]{
		};
		
		for(String url:urls ){
			search(url);
		}
		**/

			BufferedReader br = null;

			try {

				String sCurrentLine;

				br = new BufferedReader(new FileReader("todownload.txt"));
				List<ThreadDownload> ths = new ArrayList<ThreadDownload>();
				while ((sCurrentLine = br.readLine()) != null) {
					if(sCurrentLine.matches("http.*video.*")){
						System.out.println(sCurrentLine);
						ThreadDownload th = new ThreadDownload(sCurrentLine);
						ths.add(th);
					}
					
				}
				
				for(ThreadDownload t:ths){
					Thread thread = new Thread(t);
					thread.start();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

	}
	
	public static void search(String url) throws ClientProtocolException, IOException{
		HttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader(HttpHeaders.USER_AGENT,"Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16");

		String toDownload = "";
		
		HttpResponse response = httpclient.execute(httpGet);
		StringBuffer result = new StringBuffer();
		if(response.getStatusLine().getStatusCode()==200){
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			
			String line = "";
			while ((line = rd.readLine()) != null) {
				if(line.matches(".*fakepicture_nopoppup.*content.*3gp.*")){
					toDownload = line.replaceAll(".*http://content.xvideos.com", "http://content.xvideos.com");
					toDownload = toDownload.replaceAll("', '',.*", "");
				}else if(line.matches(".*fakepicture_nopoppup.*3gp.*")){
					toDownload = line.replaceAll(".*http://porn.im.", "http://porn.im.");
					toDownload = toDownload.replaceAll("', '',.*", "");
					
				}
				result.append(line);
			}
			//System.out.println(result);
		}	
		String html = result.toString();
		Document doc = Jsoup.parse(html);
		String title = doc.getElementsByTag("title").get(0).text();
		System.out.println(title+" - "+toDownload);
		//System.out.println(html);
		download(httpclient, toDownload,title);
		/**
		GetMethod method = new GetMethod(resource_url);
		try {
		    int statusCode = client.executeMethod(method);
		    if (statusCode != HttpStatus.SC_OK) {
		        logger.error("Get method failed: " + method.getStatusLine());
		    }       
		    org.apache.commons.io.FileUtils.copyInputStreamToFile(
		        method.getResponseBodyAsStream(), new File(resource_file));
		    } catch (HttpException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally {
		    method.releaseConnection();
		}
	**/
	}
	public static void download(HttpClient client,String url,String filename) throws UnsupportedOperationException, ClientProtocolException, IOException{
		HttpGet httpGet = new HttpGet(url);
		client.execute(httpGet).getEntity();
		filename = filename.replaceAll("[^A-z0-9]", "_")+".mp4";
		InputStream is = client.execute(httpGet).getEntity().getContent();
		String filePath = filename;
		FileOutputStream fos = new FileOutputStream(new File(filePath));
		int inByte;
		while((inByte = is.read()) != -1)
		     fos.write(inByte);
		is.close();
		fos.close();
		System.out.println(filename+" Done");
	}
}
