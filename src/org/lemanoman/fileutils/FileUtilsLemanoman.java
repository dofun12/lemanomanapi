package org.lemanoman.fileutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtilsLemanoman {
    public static List<String> readFile(String path){
	List<String> lines = new ArrayList<String>();
	try (BufferedReader br = new BufferedReader(new FileReader(path)))
	{

		String sCurrentLine;

		while ((sCurrentLine = br.readLine()) != null) {
			lines.add(sCurrentLine);
		}

	} catch (IOException e) {
		e.printStackTrace();
	}
	return lines;
    }
}
