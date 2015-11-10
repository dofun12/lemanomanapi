package org.lemanoman.download;

import java.io.File;

public class Progress extends Thread{
	private boolean start = true;
	private long size;
	private String filepath;	
	private long lastSize = 0;
	public Progress(long size,String filepath) {
		this.size = size;
		this.filepath = filepath;
	}
	
	
	@Override
	public void run() {
		long atualSize = 0;
		while (size>atualSize && start) {
			try {
				Thread.sleep(1000);
				File file = new File(filepath);
				if(file.exists()){
					atualSize = file.length();
					float percent = (((float)atualSize/(float)size)*100);
					long rate = (atualSize-lastSize)/1024;
					lastSize = atualSize;
					System.out.println(filepath+": ("+percent+"%)"+rate+" kbs");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	public void stopProgress() {
		start = false;
	}
}
