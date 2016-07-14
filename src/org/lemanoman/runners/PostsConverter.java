package org.lemanoman.runners;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.lemanoman.fileutils.FileUtilsLemanoman;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class PostsConverter {

	
	
	public static void main(String[] args) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			//List<String> lines = FileUtilsLemanoman.readFile("/home/kevim/workspace-php/raspsite/posts.json");
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			
			String dateHoje = new SimpleDateFormat("YYYY-MM-dd").format(new Date());
			File postJsonFile = new File("/home/kevim/workspace-php/raspsite/posts.json");
			List<String> lines = FileUtilsLemanoman.readFile("/home/kevim/posts/posts-"+dateHoje+".txt");
			ArrayNode posts = mapper.readValue(postJsonFile,ArrayNode.class);
			
			boolean exists = false;
			for(int p=0;p<posts.size();p++){
				JsonNode post = posts.get(p);
				if(post.get("date").textValue().contains(dateHoje)){
					exists = true;
					break;
				}
			}
			ArrayNode newPosts = mapper.createArrayNode();
			ArrayNode videoPosts = mapper.createArrayNode();
			for(String l:lines){
				if(l.matches("^http.*")){
					if(!l.contains("youtube")){
						newPosts.add(l);
					}else{
						videoPosts.add(l);
					}
				}
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

}
