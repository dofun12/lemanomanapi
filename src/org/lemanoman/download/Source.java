package org.lemanoman.download;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class Source extends GenericDownload implements SourceFactory {
    private String baseUrl = "";
    private String regex = "";
    private boolean linkValido=false;
    private List<StringReplace> replaces;
    private MediaType mediaType;
    private String toDownload;

    public Source(String baseUrl,String regex,List<StringReplace> replaces) {
	this.baseUrl = baseUrl;
	this.regex = regex;
	this.replaces = replaces;
    }

    @Override
    public String searchContent() {
	try {
	    boolean founded = false;
	    int tries = 0;
	    while (!founded && tries < 20) {
		HttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(baseUrl);
		httpGet.setHeader(HttpHeaders.USER_AGENT,
			"Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16");
		HttpResponse response = httpclient.execute(httpGet);

		if (response.getStatusLine().getStatusCode() == 200) {
		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		    String line = "";

		    StringBuilder html = new StringBuilder();
		    while ((line = rd.readLine()) != null) {
			html.append(line);

			if (line.matches(regex)) {
			    System.out.println("[ENCONTRADO] :"+line);
			    if(replaces!=null){
				for(StringReplace replace:replaces){
				    String novoValor = line.replaceAll(replace.getOldValue(),replace.getNewValue());
				    System.out.println(replace.getOldValue()+":\t\t[INICIO_STRING]"+novoValor+"[FIM_STRING]");
				    line = novoValor;
        			}
			    }
			    toDownload = line;
			    testeLink(httpclient, toDownload);
			    founded = true;
			    break;
			} else {

			}
		    }
		}

		tries++;
	    }
	} catch (Exception e) {

	}
	return toDownload;

    }
    
    public void testeLink(HttpClient httpclient,String link){
	try{
	    HttpGet httpGet = new HttpGet(link);
	    HttpResponse response = httpclient.execute(httpGet);
	    StatusLine statusLine = response.getStatusLine();
	    if(statusLine.getStatusCode()==200){
		linkValido = true;
	    }else{
		linkValido = false;
	    }	    
	    System.out.println("[LINK-"+statusLine.getStatusCode()+"]"+link+"["+statusLine.getReasonPhrase()+"]");
	}catch(Exception e){
	    e.printStackTrace();
	    linkValido = false;
	}
    }

    @Override
    public void download(String filename) {
	if(linkValido){
	    HttpClient httpclient = HttpClients.createDefault();
	    try {
		this.download(httpclient, toDownload , filename);
	    } catch (UnsupportedOperationException e) {
		e.printStackTrace();
	    } catch (ClientProtocolException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}    
    }
}
