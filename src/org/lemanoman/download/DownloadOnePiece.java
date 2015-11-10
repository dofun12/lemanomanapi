package org.lemanoman.download;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DownloadOnePiece {

	public static void main(String[] args) throws UnsupportedOperationException, IOException {
		new DownloadOnePiece();
	}

	private File infos = new File("info.json");
	private ObjectMapper mapper = new ObjectMapper();
	private ArrayNode entrys = null; 

	public ObjectNode loadInfo(){
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			if(infos.exists()){
				return mapper.readValue(infos,ObjectNode.class);
			}else{
				ObjectNode node = mapper.createObjectNode();
				mapper.writeValue(infos, node);
				return mapper.createObjectNode();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}	
	}

	public DownloadOnePiece() throws UnsupportedOperationException, ClientProtocolException, IOException{
		searchAllMaxMin(716,717,true);
		//downloadAll(693, 715);
		

	}

	public void downloadAll(Integer min,Integer max){
		ObjectNode nodeInfo = loadInfo();
		entrys = mapper.convertValue(nodeInfo.get("entrys"),ArrayNode.class);
		HttpClient httpclient = HttpClients.createDefault();
		for(int i=min;i<=max;i++){
			try {
				ObjectNode node = getEntry(i);
				download(httpclient, node.get("link").asText(), node.get("filename").asText());
			} catch (UnsupportedOperationException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void searchAllMaxMin(Integer min,Integer max,boolean update){
		ObjectNode nodeInfo = loadInfo();
		if(nodeInfo.get("entrys")==null){
			entrys = mapper.createArrayNode();
		}else{
			entrys = mapper.convertValue(nodeInfo.get("entrys"),ArrayNode.class);
		}
		for(int i=min;i<=max;i++){
			try {
				search("http://shogun.onepieceex.com.br/?numero="+i+"&midia=1",i,update);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		nodeInfo.set("entrys", entrys);
		saveInfo(nodeInfo);
	}

	public ObjectNode getEntry(Integer numero){
		if(entrys != null){
			for(int i=0;i<entrys.size();i++){
				ObjectNode node = mapper.convertValue(entrys.get(i),ObjectNode.class);
				if(node.get("ep") != null && numero.equals(node.get("ep").asInt())){
					return node;
				}
			}		
		}
		return null;
	} 

	public void search(String url,int numero,boolean update) throws ClientProtocolException, IOException{
		
		boolean founded = false;
		int tries = 0;
		while(!founded && tries<20){
			HttpClient httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader(HttpHeaders.USER_AGENT,"Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16");

			String toDownload = "";

			HttpResponse response = httpclient.execute(httpGet);
			StringBuffer result = new StringBuffer();
			ObjectNode entry = getEntry(numero);
			if(entry==null){
				entry = mapper.createObjectNode();
				entry.put("ep",numero);
			}

			if(response.getStatusLine().getStatusCode()==200){
				BufferedReader rd = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));

				String line = "";

				if(entry.get("link")==null || update){
					StringBuilder html = new StringBuilder();
					System.out.println("numero: "+numero+" -  Resposta: "+response.getStatusLine().getStatusCode());
					while ((line = rd.readLine()) != null) {
						html.append(line);

						if(line.matches(".*shogun.onepieceex.com.br.*")){
							System.out.println(line);
							toDownload = line.replaceAll(".*http", "http");
							toDownload = toDownload.replaceAll("\".*", "");

							entry.put("link",toDownload);
							entry.put("filename", "OnePiece_"+numero+".mp4");
							founded = true;
							HttpClient httpclient2 = HttpClients.createDefault();
							download(httpclient2, toDownload,  "OnePiece_"+numero+".mp4");
							break;
						}else{
							
						}
					}
				}	



			}else{
				entry.put("status",response.getStatusLine().getStatusCode());
				entry.put("statusName",response.getStatusLine().getReasonPhrase());
			}
			entrys.add(entry);
			tries++;
		}

	}
	public void saveInfo(ObjectNode node){
		try{
			mapper.writeValue(infos, node);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void download(HttpClient client,String url,String filename) throws UnsupportedOperationException, ClientProtocolException, IOException{
		HttpGet httpGet = new HttpGet(url);

		client.execute(httpGet).getEntity();
		//filename = filename.replaceAll("[^A-z0-9]", "_");
		HttpEntity entity = client.execute(httpGet).getEntity();
		long size = entity.getContentLength();
		System.out.println("Downloading "+filename+" Tamanho: " + (size/1024/1024));

		Progress progress = new Progress(size,filename);
		progress.start();

		InputStream is = entity.getContent();
		String filePath = filename;
		FileOutputStream fos = new FileOutputStream(new File(filePath));

		int inByte;
		while((inByte = is.read()) != -1){
			fos.write(inByte);
		}	
		System.out.println("\n");
		is.close();
		fos.close();


		System.out.println(filename+" Done");
		progress.stopProgress();
	}
}
