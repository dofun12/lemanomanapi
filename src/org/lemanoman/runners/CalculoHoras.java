package org.lemanoman.runners;

public class CalculoHoras {

	public static void main(String[] args) {
	
		int cargaSemanal = 40;
		int cargaMensal  = (cargaSemanal*4);
		Double sal = 4000D;
		Double valHora = sal/cargaMensal;
		
		/** Horas trabalhadas **/
		Double horas = 33d;
		Double minutos = 34d;
		
		horas = horas+(60/minutos);
		
		/** Adicionando 75% **/
		horas = (horas+(horas*0.75));
		
		
		System.out.println(valHora*horas);

	}

}
