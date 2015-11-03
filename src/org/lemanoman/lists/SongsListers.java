package org.lemanoman.lists;

import java.io.File;

public class SongsListers {

	public static void main(String[] args) {
		File songs = new File("/home/kevim/Música");
		for(File song:songs.listFiles()){
			String songName = song.getName();
			String novoSongName = "";
			int lengthName = songName.length();
			if(songName.contains("-")){
				for(char c:songName.toCharArray()){
					boolean foundChar=false;
					if(c=='-' && !foundChar){
						c='_';
						foundChar=true;
					}
					novoSongName = novoSongName+c;
				}
				songName = novoSongName;
			}	
			System.out.println(songName.replace("www_musicasparabaixar_org_",""));
		}

	}

}
