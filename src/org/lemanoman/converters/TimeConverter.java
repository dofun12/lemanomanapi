package org.lemanoman.converters;

import java.util.Calendar;

public class TimeConverter {
	public static String getTempo(long millis){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		String resposta = "";
		if(millis<1000){
			resposta = calendar.get(Calendar.MILLISECOND)+" milliseconds";	
		}else if(millis>1000 && millis<60000){
			resposta = calendar.get(Calendar.SECOND)+" seconds "+calendar.get(Calendar.MILLISECOND)+" milliseconds";
		}else if(millis>60000 && millis<360000){
			resposta = calendar.get(Calendar.MINUTE)+" minutes "+calendar.get(Calendar.SECOND)+" seconds "+calendar.get(Calendar.MILLISECOND)+" milliseconds";
		}
		return resposta;
		
	}
}
