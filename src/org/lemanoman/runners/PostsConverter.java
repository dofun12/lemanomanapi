package org.lemanoman.runners;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.lemanoman.fileutils.FileUtilsLemanoman;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PostsConverter {

	
	
	public static void main(String[] args) {
		String dateHoje = new SimpleDateFormat("YYYY-MM-dd").format(new Date());
		String[] dates = {dateHoje};
		
		for(String d:dates){
			convertTxtToJson(d);	
		}
		
		convertJsonToSql();

	}
	public static void convertTxtToJson(String date){
		try {
			ObjectMapper mapper = new ObjectMapper();
			//List<String> lines = FileUtilsLemanoman.readFile("/home/kevim/workspace-php/raspsite/posts.json");
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			
			String dateHoje = date;
			//String dateHoje = "2016-07-18";
			File postJsonFile = new File("/home/kevim/workspace-php/raspsite/posts.json");
			if(!postJsonFile.exists()){
				System.out.println("Arquivo nao encontrado");
				return;
			}
			
			List<String> lines = FileUtilsLemanoman.readFile("/home/kevim/posts/posts-"+dateHoje+".txt");
			ArrayNode posts = mapper.readValue(postJsonFile,ArrayNode.class);
			
			for(int p=0;p<posts.size();p++){
				JsonNode post = posts.get(p);
				if(post.get("date").textValue().contains(dateHoje)){
					System.out.println("Ja existe posts na data: "+dateHoje);
					return;
				}
			}
			
			ArrayNode newPosts = mapper.createArrayNode();
			ArrayNode videoPosts = mapper.createArrayNode();
			for(String l:lines){
				newPosts.add(l);
				/**if(l.matches("^http.*")){
					if(!l.contains("youtube")){
						newPosts.add(l);
					}else{
						videoPosts.add(l);
					}
				}**/
			}
			ObjectNode nodePost = mapper.createObjectNode();
			nodePost.put("date",dateHoje);
			nodePost.set("posts",newPosts);
			nodePost.set("videos",videoPosts);
		
			
			posts.add(nodePost);
			
			mapper.writeValue(postJsonFile, posts);
			
			
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void convertJsonToSql(){
		ObjectMapper mapper = new ObjectMapper();
		File postJsonFile = new File("/home/kevim/workspace-php/raspsite/posts.json");
		try {
			ArrayNode posts = mapper.readValue(postJsonFile, ArrayNode.class);
			for(Iterator<JsonNode> iterator = posts.iterator();iterator.hasNext();){
				JsonNode post = iterator.next();
				String date = post.get("date").asText();
				ArrayNode links = mapper.convertValue(post.get("posts"),ArrayNode.class);
				for(Iterator<JsonNode> linkIt = links.iterator();linkIt.hasNext();){
					JsonNode link = linkIt.next();
					String linkTxt = link.asText();
					
					System.out.println("insert into posts (id,dia,link) values (null,'"+date+"','"+linkTxt+"' );");
				}
				
			}
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
