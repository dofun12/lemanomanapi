package org.lemanoman.download;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

public interface DownloadFactory {
    public void download(HttpClient client,String url,String filename) throws UnsupportedOperationException, ClientProtocolException, IOException;
}
