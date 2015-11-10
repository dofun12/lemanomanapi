package org.lemanoman.download;

public interface SourceFactory{
    public String searchContent();
    public void download(String filename);
}
