package org.lemanoman.runners;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StatService {
	private ObjectMapper mapper;
	private boolean isrunning = true;
	
	public StatService(){
		mapper = new ObjectMapper();
		while(isrunning){
			long start = System.currentTimeMillis();
			
			ObjectNode response = mapper.createObjectNode();
			response.set("memory",getMemoryUsage());
			response.set("disk",getDiskUsage());
			try {
				mapper.writeValue(new File("response.json"), response);
			} catch (IOException e1) {
				isrunning = false;
				e1.printStackTrace();
			}
			
			long end = (System.currentTimeMillis() - start); 
			
			//System.out.println("Time: "+end);
			try {
				Thread.sleep(1000-end);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
	
	public ObjectNode getDiskUsage(){
		ObjectNode diskUsage = mapper.createObjectNode();
		for (Path root : FileSystems.getDefault().getRootDirectories()) {

		    try {
		        FileStore store = Files.getFileStore(root);
		        diskUsage.put("directory", root.toString());
		        diskUsage.put("available", store.getUsableSpace());
		        diskUsage.put("total", store.getTotalSpace());
		    } catch (IOException e) {
		        System.out.println("error querying space: " + e.toString());
		    }
		}
		return diskUsage;
	}
	
	public ObjectNode getMemoryUsage(){
		ObjectNode node = mapper.createObjectNode();
		try {
			
			Process p = Runtime.getRuntime().exec("free");
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			String[] headers = new String[6];
			String[] values  = new String[6];
			try {
				int count = 0;
                while ((line = input.readLine()) != null) {
                	if(count==0){
                		headers = line.replaceAll("[^A-z]{1,}",";").split(";");
                	}
                	if(line.matches("^Mem.*")){
                		values = line.replaceAll("[^0-9]{1,}",";").split(";");
                	}
                	count++;
                }
                int i = 0;
                for(String h:headers){
                	if(!h.isEmpty()){
                		node.put(h, values[i]);
                	}
                	i++;
                }
                return node;
            } catch (IOException e) {
                e.printStackTrace();
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return node;
	}
	
	public static void main(String[] args) {
		new StatService();

	}

}
