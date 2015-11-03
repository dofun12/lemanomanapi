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
	
	/**
	 * 
	 * <b>Usage:</b> "time period" <br />
	 * <b>Exemple:</b>  10 mins,20 days,10 hours
	 **/
	
	public static long getMilliseconds(String value){
		Integer tempo = Integer.parseInt(value.split(" ")[0]); 
		String periodo = value.split(" ")[1].toLowerCase();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(0);
		
		if(tempo!=null && periodo!=null && periodo!=""){
			if(periodo.contains("day")){
				c.set(Calendar.DAY_OF_YEAR, tempo);
			}else if(periodo.contains("week")){
				c.set(Calendar.WEEK_OF_YEAR, tempo);
			}else if(periodo.contains("hour") || periodo.contains("hrs")){
				c.set(Calendar.HOUR, tempo);
			}else if(periodo.contains("minute") || periodo.contains("mins")){
				c.set(Calendar.MINUTE, tempo);
			}else if(periodo.contains("second") || periodo.contains("secs")){
				c.set(Calendar.SECOND, tempo);
			}
			return c.getTimeInMillis();
		}else{
			return 0;	
		}
		
		
	}
	
}
